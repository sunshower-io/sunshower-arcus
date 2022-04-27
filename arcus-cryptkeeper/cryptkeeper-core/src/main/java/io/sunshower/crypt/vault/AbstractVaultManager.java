package io.sunshower.crypt.vault;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.sunshower.arcus.condensation.Condensation;
import io.sunshower.crypt.DefaultEncryptedValue;
import io.sunshower.crypt.core.DecryptedValue;
import io.sunshower.crypt.core.EncryptedValue;
import io.sunshower.crypt.core.EncryptionService;
import io.sunshower.crypt.core.EncryptionServiceFactory;
import io.sunshower.crypt.core.Secret;
import io.sunshower.crypt.core.Vault;
import io.sunshower.crypt.core.VaultManager;
import io.sunshower.crypt.vault.FileBackedVaultManager.DescriptorPair;
import io.sunshower.lang.common.encodings.Encoding;
import io.sunshower.lang.common.encodings.Encodings;
import io.sunshower.lang.common.encodings.Encodings.Type;
import io.sunshower.persistence.id.Identifier;
import java.io.ByteArrayInputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.val;

@Log
@SuppressFBWarnings
@SuppressWarnings("PMD")
public abstract class AbstractVaultManager implements VaultManager {

  public static final int DEFAULT_SALT_SIZE = 64;
  public static final int INITIALIZATION_VECTOR_SIZE = 16;
  protected final Object lock = new Object();
  protected final Map<Identifier, DescriptorPair> openVaults;
  protected final EncryptionServiceFactory encryptionServiceFactory;
  protected final Encoding encoding;
  protected final Condensation condensation;

  public AbstractVaultManager(
      @NonNull EncryptionServiceFactory encryptionServiceFactory,
      @NonNull final Condensation condensation) {
    this.openVaults = new HashMap<>();
    this.encryptionServiceFactory = encryptionServiceFactory;
    this.encoding = Encodings.create(Type.Base58);
    this.condensation = condensation;
  }

  @Override
  public void lock(Vault vault) {
    synchronized (lock) {
      log.log(Level.INFO, "Locking vault {0}", vault);
      val vaultDescriptor = getOpenVault(vault.getId());
      val encryptionService =
          encryptionServiceFactory.create(
              vaultDescriptor.getSalt(),
              vaultDescriptor.getInitializationVector(),
              ((SerializedVault) vault).getPassword());
      encryptAndClose(vault, vaultDescriptor, encryptionService);
      openVaults.remove(vault.getId());
      log.log(Level.INFO, "Vault {0} locked", vault);
    }
  }

  protected DecryptedValue decrypt(EncryptedValue secret, CharSequence password) {
    val service =
        encryptionServiceFactory.create(
            secret.getSalt(), secret.getInitializationVector(), password);
    return service.decryptText(secret);
  }

  @Override
  public List<Vault> getOpenVaults() {
    synchronized (lock) {
      val open = openVaults.values();
      val results = new ArrayList<Vault>(open.size());
      for (val o : open) {
        results.add(vaultFrom(o.descriptor));
      }
      return results;
    }
  }

  private Vault vaultFrom(VaultDescriptor o) {
    val vault = new DefaultVault();

    return vault;
  }

  @Override
  public EncryptedValue encrypt(Vault vault, Secret secret) {
    if (!(vault instanceof SerializedVault)) {
      throw new IllegalArgumentException("Bad use");
    }

    val svault = (SerializedVault) vault;
    val salt = generateRandom(DEFAULT_SALT_SIZE);
    val initializationVector = generateRandom(INITIALIZATION_VECTOR_SIZE);
    val service =
        encryptionServiceFactory.create(
            encoding.encode(salt), encoding.encode(initializationVector), svault.getPassword());

    val bytes =
        new ByteArrayInputStream(encoding.encode(service.encryptToBytes(secret)).getBytes());
    val value = new DefaultEncryptedValue(bytes, salt, initializationVector);
    value.setName(secret.getName().toString());
    value.setDescription(secret.getDescription().toString());
    return value;
  }

  @SneakyThrows
  protected byte[] generateRandom(int i) {
    val bytes = new byte[i];
    val instance = SecureRandom.getInstance("SHA1PRNG");
    instance.nextBytes(bytes);
    return bytes;
  }

  protected abstract VaultDescriptor getOpenVault(Identifier id);

  protected abstract void encryptAndClose(
      Vault vault, VaultDescriptor vaultDescriptor, EncryptionService encryptionService);
}
