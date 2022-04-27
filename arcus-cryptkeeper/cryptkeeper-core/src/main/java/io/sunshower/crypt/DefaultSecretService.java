package io.sunshower.crypt;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.sunshower.arcus.condensation.Condensation;
import io.sunshower.crypt.core.EncryptionServiceFactory;
import io.sunshower.crypt.core.Lease;
import io.sunshower.crypt.core.LeaseExpiredException;
import io.sunshower.crypt.core.LeaseReaper;
import io.sunshower.crypt.core.LeaseRequest;
import io.sunshower.crypt.core.Secret;
import io.sunshower.crypt.core.SecretCollection;
import io.sunshower.crypt.core.SecretLease;
import io.sunshower.crypt.core.SecretService;
import io.sunshower.crypt.core.Vault;
import io.sunshower.crypt.core.VaultException;
import io.sunshower.crypt.core.VaultLease;
import io.sunshower.crypt.core.VaultManager;
import io.sunshower.crypt.vault.FileBackedVaultManager;
import io.sunshower.persistence.id.Identifier;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.val;

@SuppressFBWarnings
@SuppressWarnings("PMD")
public class DefaultSecretService implements SecretService {

  private final LeaseReaper leaseReaper;
  private final VaultManager vaultManager;


  private final Map<Identifier, Vault> openVaults;


  protected DefaultSecretService(@NonNull VaultManager vaultManager) {
    this.vaultManager = vaultManager;
    this.openVaults = new HashMap<>();
    this.leaseReaper = new ConcurrentLeaseReaper(this);
  }


  public DefaultSecretService(@NonNull final File file, @NonNull Condensation condensation, @NonNull
      EncryptionServiceFactory factory) {
    this(new FileBackedVaultManager(file, condensation, factory));
  }


  public DefaultSecretService(File file, Condensation condensation) {
    this(file, condensation, new DefaultEncryptionServiceFactory());
  }

  public DefaultSecretService(File root) {
    this(root, Condensation.create("json"));
  }


  @Override
  public SecretCollection list(@NonNull Identifier vaultId, @NonNull LeaseRequest request) {
    val date = new Date();
    val closed = new AtomicBoolean(false);
    val delegate = new ArrayList<Secret>();
    val vault = this.lease(vaultId, request);
    val lease = new SecretCollection() {
      @Override
      public int size() {
        check(closed);
        return delegate.size();
      }


      @Override
      public boolean isEmpty() {
        check(closed);
        return delegate.isEmpty();
      }

      @Override
      public boolean contains(Object o) {
        check(closed);
        return delegate.isEmpty();
      }

      @Override
      public Iterator<Secret> iterator() {
        check(closed);
        return delegate.iterator();
      }

      @Override
      public Object[] toArray() {
        check(closed);
        return delegate.toArray();
      }

      @Override
      public <T> T[] toArray(T[] ts) {
        check(closed);
        return delegate.toArray(ts);
      }

      @Override
      public boolean add(Secret secret) {
        check(closed);
        val result = delegate.add(secret);
        vault.save(secret);
        return result;
      }

      @Override
      public boolean remove(Object o) {
        check(closed);
        val result = delegate.remove(o);
        vault.delete(((Secret) o).getId());
        return result;
      }

      @Override
      public boolean containsAll(Collection<?> collection) {
        boolean contains = true;
        for (val c : collection) {
          contains &= contains(c);
        }
        return contains;
      }

      @Override
      public boolean addAll(Collection<? extends Secret> collection) {
        boolean contains = true;
        for (val c : collection) {
          contains &= add(c);
        }
        return contains;
      }

      @Override
      public boolean removeAll(Collection<?> collection) {
        boolean contains = true;
        for (val c : collection) {
          contains &= remove(c);
        }
        return contains;
      }

      @Override
      public boolean retainAll(Collection<?> collection) {
        boolean contains = true;
        for (val c : collection) {
          if (!contains(c)) {
            contains &= remove(c);
          }
        }
        return contains;
      }

      @Override
      public void clear() {
        for (val c : delegate) {
          remove(c);
        }
        delegate.clear();
      }

      @Override
      public boolean closed() {
        return closed.get();
      }

      @Override
      public Date getExpiration() {
        return request.getExpiration();
      }

      @Override
      public Date getLeaseDate() {
        return date;
      }

      @Override
      public SecretCollection get() throws LeaseExpiredException {
        return this;
      }

      @Override
      public void close() {
        closed.set(true);
        vault.close();
      }

      private void check(AtomicBoolean closed) {
        if (closed.get()) {
          throw new VaultException("Collection closed!");
        }
      }
    };
    leaseReaper.schedule(lease);
    return lease;
  }

  @Override
  public void close() {
    vaultManager.close();
  }

  @Override
  public VaultLease lease(Identifier identifier, LeaseRequest request) {
    synchronized (openVaults) {
      val vault = new AtomicReference<>(vaultManager.unlock(identifier, request.password()));
      val date = new Date();
      val vaultClosed = new AtomicBoolean(false);
      val lease = new VaultLease() {
        @Override
        public List<Secret> list() {
          return vault.get().getSecrets().stream().map(secret -> new Secret() {
            @Override
            public CharSequence getName() {
              return secret.getName();
            }

            @Override
            public CharSequence getDescription() {
              return secret.getDescription();
            }

            @Override
            public Identifier getId() {
              return secret.getId();
            }
          }).collect(Collectors.toList());
        }

        @Override
        public Lease<Secret> lease(Identifier id) {
          val secret = vault.get().getSecret(id);
          val closed = new AtomicBoolean(false);
          val lease = new SecretLease() {
            @Override
            public Date getExpiration() {
              if (closed.get()) {
                throw new VaultException("Secret closed");
              }
              return new Date(date.getTime() + request.getExpiration().getTime());
//              return new Date(date.)
            }

            @Override
            public Date getLeaseDate() {
              if (closed.get()) {
                throw new VaultException("Secret closed");
              }
              return date;
            }

            @Override
            public Secret get() throws LeaseExpiredException {
              if (closed.get()) {
                throw new VaultException("Secret closed");
              }
              return secret;
            }

            @Override
            public void close() {
              closed.set(true);
            }
          };
          leaseReaper.schedule(lease);
          return lease;
        }

        @Override
        public void close(SecretLease lease) {
          if (vaultClosed.get()) {
            throw new VaultException("Vault closed");
          }
          lease.close();
        }

        @Override
        public Identifier save(@NonNull Secret secret) {
          if (vaultClosed.get()) {
            throw new VaultException("Vault closed");
          }
          return vault.get().addSecret(secret);
        }

        @Override
        public boolean delete(Identifier identifier) {
          if (vaultClosed.get()) {
            throw new VaultException("Vault closed");
          }
          return vault.get().deleteSecret(identifier);
        }

        @Override
        public Date getExpiration() {
          if (vaultClosed.get()) {
            throw new VaultException("Vault closed");
          }
          return new Date(date.getTime() + request.getExpiration().getTime());
        }

        @Override
        public Date getLeaseDate() {
          if (vaultClosed.get()) {
            throw new VaultException("Vault closed");
          }
          return date;
        }

        @Override
        public Vault get() throws LeaseExpiredException {
          if (vaultClosed.get()) {
            throw new VaultException("Vault closed");
          }
          return vault.get();
        }

        @Override
        public void close() {
          try {
            vault.get().close();
          } finally {
            vaultClosed.set(true);
          }
        }
      };
      leaseReaper.schedule(lease);
      return lease;
    }
  }

  @Override
  public Identifier add(Vault vault, CharSequence password) {
    return vaultManager.addVault(vault, password).getId();
  }
}
