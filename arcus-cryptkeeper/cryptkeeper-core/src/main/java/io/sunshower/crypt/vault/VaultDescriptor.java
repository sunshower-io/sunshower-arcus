package io.sunshower.crypt.vault;

import io.sunshower.arcus.condensation.Attribute;
import io.sunshower.arcus.condensation.Convert;
import io.sunshower.arcus.condensation.RootElement;
import io.sunshower.persistence.id.Identifier;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@RootElement
@ToString
@EqualsAndHashCode
public class VaultDescriptor {

  @Convert(IdentifierConverter.class)
  @Attribute
  private Identifier id;

  @Attribute private String icon;

  @Attribute private String name;

  @Attribute private String vaultPath;

  @Attribute private String description;

  @Attribute private String salt;

  @Attribute private String initializationVector;

  public Identifier getId() {
    return id;
  }

  public void setId(Identifier id) {
    this.id = id;
  }

  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVaultPath() {
    return vaultPath;
  }

  public void setVaultPath(String vaultPath) {
    this.vaultPath = vaultPath;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getSalt() {
    return salt;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public String getInitializationVector() {
    return initializationVector;
  }

  public void setInitializationVector(String initializationVector) {
    this.initializationVector = initializationVector;
  }
}
