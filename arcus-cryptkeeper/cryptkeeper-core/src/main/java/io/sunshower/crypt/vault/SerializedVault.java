package io.sunshower.crypt.vault;

import io.sunshower.arcus.condensation.Attribute;
import io.sunshower.arcus.condensation.Convert;
import io.sunshower.arcus.condensation.Element;
import io.sunshower.arcus.condensation.RootElement;
import io.sunshower.crypt.core.DecryptedValue;
import io.sunshower.crypt.core.EncryptedValue;
import io.sunshower.crypt.core.LockedVaultException;
import io.sunshower.crypt.core.Secret;
import io.sunshower.crypt.core.Vault;
import io.sunshower.crypt.core.VaultManager;
import io.sunshower.persistence.id.Identifier;
import io.sunshower.persistence.id.Identifiers;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.val;

@RootElement
@ToString(exclude = "password")
@EqualsAndHashCode(exclude = "password")
@SuppressWarnings("PMD")
public class SerializedVault implements Vault {

  @Attribute
  @Convert(IdentifierConverter.class)
  private Identifier id;

  @Getter @Setter private transient CharSequence password;

  private transient boolean closed;
  private transient VaultManager vaultManager;
  private transient VaultDescriptor vaultDescriptor;

  @Element private String icon;

  @Element
  @Convert(key = IdentifierConverter.class)
  private Map<Identifier, SerializedEncryptedValue> encryptedValues;

  public SerializedVault() {
    this(Identifiers.newSequence().next());
  }

  public SerializedVault(Identifier id) {
    this.id = id;
    this.encryptedValues = new HashMap<>();
  }

  @Override
  public String getName() {
    return vaultDescriptor.getName();
  }

  @Override
  public void setName(String name) {
    vaultDescriptor.setName(name);
  }

  @Override
  public String getDescription() {
    return vaultDescriptor.getDescription();
  }

  @Override
  public void setDescription(String description) {
    vaultDescriptor.setDescription(description);
  }

  @Override
  public Identifier getId() {
    return vaultDescriptor.getId();
  }

  @Override
  public VaultManager getManager() {
    return vaultManager;
  }

  @Override
  public DecryptedValue open(Identifier id) {
    check();
    val secret = encryptedValues.get(id);
    if (secret == null) {
      throw new NoSuchElementException("No secret identified by: %s".formatted(id));
    }
    return ((AbstractVaultManager) vaultManager).decrypt(secret, password);
  }

  @Override
  public List<EncryptedValue> getSecrets() {
    check();
    return List.copyOf(encryptedValues.values());
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
    val encryptedValue = vaultManager.encrypt(this, secret);
    encryptedValues.put(secret.getId(), new SerializedEncryptedValue(encryptedValue));
    vaultManager.addSecret(this, secret);
    return secret.getId();
  }

  @Override
  public boolean deleteSecret(Identifier id) {
    check();
    val result = encryptedValues.remove(id) != null;
    vaultManager.flush(this);
    return result;
  }

  @Override
  public <T extends Secret> T getSecret(Identifier id) {
    check();
    val encryptedValue = locateSecret(id);
    return vaultManager.decrypt(this, encryptedValue);
  }

  @Override
  public void close() {
    vaultManager.lock(this);
    encryptedValues.clear();
    this.password = null;
    this.closed = true;
  }

  void setVaultManager(VaultManager vaultManager) {
    this.vaultManager = vaultManager;
    this.closed = false;
  }

  void setDescriptor(@NonNull VaultDescriptor vaultDescriptor) {
    this.vaultDescriptor = vaultDescriptor;
  }

  final void check() {
    if (closed) {
      throw new LockedVaultException("Error: this vault is locked!");
    }
  }

  private EncryptedValue locateSecret(Identifier id) {
    val result = encryptedValues.get(id);
    if (result == null) {
      throw new NoSuchElementException(
          "No secret identified by '%s' found in this vault".formatted(id));
    }
    return result;
  }
}
