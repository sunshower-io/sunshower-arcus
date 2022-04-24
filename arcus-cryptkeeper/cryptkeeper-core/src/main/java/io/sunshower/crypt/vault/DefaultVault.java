package io.sunshower.crypt.vault;

import io.sunshower.crypt.core.DecryptedValue;
import io.sunshower.crypt.core.EncryptedValue;
import io.sunshower.crypt.core.Secret;
import io.sunshower.crypt.core.Vault;
import io.sunshower.crypt.core.VaultManager;
import io.sunshower.persistence.id.Identifier;
import io.sunshower.persistence.id.Identifiers;
import io.sunshower.persistence.id.Sequence;
import java.util.List;
import lombok.NonNull;

@SuppressWarnings("PMD")
public class DefaultVault implements Vault {

  static final Sequence<Identifier> idSequence;

  static {
    idSequence = Identifiers.newSequence(true);
  }

  private String icon;
  private String name;
  private Identifier id;
  private String description;
  private Vault delegate;

  public DefaultVault() {
    this(idSequence.next());
  }

  public DefaultVault(Identifier id) {
    this.id = id;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public Identifier getId() {
    return id;
  }

  @Override
  public VaultManager getManager() {
    check();
    return delegate.getManager();
  }

  @Override
  public DecryptedValue open(Identifier id) {
    check();
    return delegate.open(id);
  }

  @Override
  public List<EncryptedValue> getSecrets() {
    check();
    return delegate.getSecrets();
  }

  @Override
  public String getIcon() {
    return icon;
  }

  @Override
  public void setIcon(String icon) {
    this.icon = icon;
  }

  @Override
  public Identifier addSecret(Secret secret) {
    check();
    return delegate.addSecret(secret);
  }

  @Override
  public boolean deleteSecret(Identifier id) {
    check();
    return delegate.deleteSecret(id);
  }

  @Override
  public <T extends Secret> T getSecret(Identifier id) {
    check();
    return delegate.getSecret(id);
  }

  @Override
  public void close() throws Exception {
    check();
    delegate.setName(name);
    delegate.setIcon(icon);
    delegate.setDescription(description);
    delegate.close();
    delegate = null;
  }

  private void check() {
    if (delegate == null) {
      throw new IllegalStateException("Error: this vault is not open");
    }
  }

  final void setDelegate(@NonNull Vault delegate) {
    this.delegate = delegate;
  }
}
