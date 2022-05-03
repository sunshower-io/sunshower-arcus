package io.sunshower.crypt.vault;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.sunshower.arcus.condensation.Condensation;
import io.sunshower.crypt.core.EncryptedValue;
import io.sunshower.crypt.core.EncryptionService;
import io.sunshower.crypt.core.EncryptionServiceFactory;
import io.sunshower.crypt.core.EncryptionServiceSet;
import io.sunshower.crypt.core.NoSuchVaultException;
import io.sunshower.crypt.core.Secret;
import io.sunshower.crypt.core.Vault;
import io.sunshower.crypt.core.VaultManager;
import io.sunshower.persistence.id.Identifier;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import lombok.Data;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.val;

@Log
@SuppressFBWarnings
@SuppressWarnings("PMD")
public class FileBackedVaultManager extends AbstractVaultManager implements VaultManager {

  private final File directory;

  public FileBackedVaultManager(
      @NonNull final File directory,
      @NonNull final Condensation condensation,
      @NonNull EncryptionServiceFactory encryptionServiceFactory) {
    super(encryptionServiceFactory, condensation);
    this.directory = directory;
    validate();
  }

  @Override
  public Vault unlock(Identifier id, CharSequence password) {
    synchronized (lock) {
      val vaultFile = locate(id);
      val vaultDescriptor = readVaultDescriptor(vaultFile);
      val encryptionService =
          encryptionServiceFactory.create(
              vaultDescriptor.getSalt(), vaultDescriptor.getInitializationVector(), password);
      return readVault(vaultDescriptor, encryptionService, password);
    }
  }

  @Override
  public Vault addVault(Vault vault, CharSequence password) {
    synchronized (lock) {
      val serializedVault = new SerializedVault(vault.getId());
      serializedVault.setVaultManager(this);

      val salt = encoding.encode(generateRandom(64));
      val initializationVector = generateRandom(16);
      val vaultDescriptor = new VaultDescriptor();

      val encryptionService =
          encryptionServiceFactory.create(
              salt, encoding.encode(initializationVector), password, encoding);
      vaultDescriptor.setDescription(vault.getDescription());
      vaultDescriptor.setName(vault.getName());
      vaultDescriptor.setIcon(vault.getIcon());
      vaultDescriptor.setId(vault.getId());
      vaultDescriptor.setInitializationVector(encoding.encode(initializationVector));
      vaultDescriptor.setSalt(salt);
      vaultDescriptor.setVaultPath(UUID.randomUUID() + ".vault");

      serializedVault.setDescriptor(vaultDescriptor);
      serializedVault.setIcon(vault.getIcon());
      serializedVault.setName(vault.getName());
      serializedVault.setDescription(vault.getDescription());
      serializedVault.setPassword(password);

      encryptAndClose(serializedVault, vaultDescriptor, encryptionService);
      return unlock(vault.getId(), password);
    }
  }

  @Override
  @SneakyThrows
  public boolean deleteVault(Vault vault, CharSequence password) {
    synchronized (lock) {
      unlock(vault.getId(), password);
      val vaultDescriptor = getOpenVault(vault.getId());
      try {
        Files.delete(new File(directory, vaultDescriptor.getVaultPath()).toPath());
      } finally {
        Files.delete(locate(vault.getId()).toPath());
      }
      return openVaults.remove(vault.getId()) != null;
    }
  }

  @Override
  public EncryptionServiceSet createEncryptionServiceSet() {
    val salt = generateRandom(64);
    val iv = generateRandom(16);
    val tempPassword = encoding.encode(generateRandom(32));

    val factory =
        encryptionServiceFactory.create(encoding.encode(salt), encoding.encode(iv), tempPassword);
    return new EncryptionServiceSet() {
      @Override
      public byte[] getSalt() {
        return salt;
      }

      @Override
      public byte[] getInitializationVector() {
        return iv;
      }

      @Override
      public byte[] getPassword() {
        return encoding.decode(tempPassword);
      }

      @Override
      public EncryptionService getEncryptionService() {
        return factory;
      }
    };
  }

  /**
   * since we just serialize the vault back out to the file-system, writing its secrets at that
   * time, we just need to flush this manager
   *
   * @param vault
   * @param secret
   * @return
   */
  @Override
  public Vault addSecret(Vault vault, Secret secret) {
    return flush(vault);
  }

  @Override
  public Vault addSecrets(Vault vault, Collection<? extends Secret> secrets) {
    return flush(vault);
  }

  @Override
  @SneakyThrows
  public <T extends Secret> T decrypt(Vault owner, EncryptedValue encryptedValue) {
    if ((!(owner instanceof SerializedVault))) {
      throw new IllegalArgumentException("Bad use");
    }
    val svault = ((SerializedVault) owner);
    val objectBytes = encoding.decode(new String(encoding.decode(encryptedValue.getCipherText())));
    val salt = encryptedValue.getSalt();
    val iv = encryptedValue.getInitializationVector();
    val service = encryptionServiceFactory.create(salt, iv, svault.getPassword());

    return service.decryptFromBytes(objectBytes);
  }

  @Override
  @SneakyThrows
  public Vault flush(Vault vault) {
    if (!(vault instanceof SerializedVault)) {
      throw new IllegalArgumentException("Bad use");
    }
    val svault = ((SerializedVault) vault);
    lock(svault);
    return unlock(svault.getId(), svault.getPassword());
  }

  @Override
  public void close() {
    synchronized (lock) {
      for (val open : new HashMap<>(openVaults).values()) {
        lock(open.vault);
      }
      openVaults.clear();
    }
  }

  @SneakyThrows
  private VaultDescriptor readVaultDescriptor(File vaultFile) {
    return condensation.read(VaultDescriptor.class, Files.readString(vaultFile.toPath()));
  }

  private File locate(Identifier id) {
    val vaultFile = new File(directory, String.format("%s.crypt", id));
    if (!vaultFile.exists()) {
      throw new NoSuchVaultException(
          "No vault with id: %s exist in this vault manager (%s)".formatted(id, directory));
    }
    return validateFile(vaultFile);
  }

  @SneakyThrows
  private File validateFile(File vaultFile) {
    if (!(vaultFile.exists() || vaultFile.createNewFile())) {
      throw new NoSuchVaultException(
          "No file %s exist in this vault manager (%s)".formatted(vaultFile, directory));
    }
    if (!vaultFile.isFile()) {
      throw new IllegalStateException("Error: vault file %s is not a file!".formatted(vaultFile));
    }
    if (!(vaultFile.canRead())) {
      throw new IllegalStateException("Error: Vault file '%s' cannot be read".formatted(vaultFile));
    }
    if (!(vaultFile.canWrite())) {
      throw new IllegalStateException(
          "Error: Vault file '%s' cannot be written".formatted(vaultFile));
    }
    return vaultFile;
  }

  private void validate() {
    if (!(directory.exists() || directory.mkdirs())) {
      throw new IllegalStateException(
          "Error: directory '%s' does not exist and cannot be created".formatted(directory));
    }
    if (!directory.isDirectory()) {
      throw new IllegalStateException("Error: File '%s' is not a directory".formatted(directory));
    }

    if (!directory.canRead()) {
      throw new IllegalStateException(
          "Error: Directory '%s' is not readable ".formatted(directory));
    }
    if (!directory.canWrite()) {
      throw new IllegalStateException("Error: Directory '%s' is not writable".formatted(directory));
    }
  }

  @Override
  protected VaultDescriptor getOpenVault(Identifier id) {
    synchronized (lock) {
      val result = openVaults.get(id);
      if (result == null) {
        throw new NoSuchVaultException("No vault opened with id: '%s'".formatted(id));
      }
      return result.getDescriptor();
    }
  }

  @Override
  @SneakyThrows
  protected void encryptAndClose(
      Vault vault, VaultDescriptor vaultDescriptor, EncryptionService encryptionService) {

    synchronized (lock) {
      writeVaultDescriptor(vaultDescriptor);
      val file = validateFile(new File(directory, vaultDescriptor.getVaultPath()));

      val text = condensation.write(SerializedVault.class, (SerializedVault) vault);
      val encryptedText = encryptionService.encrypt((CharSequence) text);
      val encodedText = encoding.encode(encryptedText);
      try (val channel =
          FileChannel.open(
              file.toPath(),
              StandardOpenOption.CREATE,
              StandardOpenOption.TRUNCATE_EXISTING,
              StandardOpenOption.DSYNC,
              StandardOpenOption.WRITE)) {
        val bytes = ByteBuffer.wrap(encodedText.toString().getBytes(StandardCharsets.UTF_8));
        channel.write(bytes);
      }
    }
  }

  @SneakyThrows
  private void writeVaultDescriptor(VaultDescriptor vaultDescriptor) {
    synchronized (lock) {
      val vaultFile =
          validateFile(new File(directory, String.format("%s.crypt", vaultDescriptor.getId())));
      val result = condensation.write(VaultDescriptor.class, vaultDescriptor);
      try (val channel =
          FileChannel.open(
              vaultFile.toPath(),
              StandardOpenOption.CREATE,
              StandardOpenOption.TRUNCATE_EXISTING,
              StandardOpenOption.DSYNC,
              StandardOpenOption.WRITE)) {
        val bytes = ByteBuffer.wrap(result.getBytes(StandardCharsets.UTF_8));
        channel.write(bytes);
      }
    }
  }

  @SneakyThrows
  private Vault readVault(
      VaultDescriptor vaultDescriptor, EncryptionService encryptionService, CharSequence password) {
    synchronized (lock) {
      val vaultFile = validateFile(new File(directory, vaultDescriptor.getVaultPath()));
      val fileContents = encoding.decode(Files.readString(vaultFile.toPath()));
      val plaintextContext = encryptionService.decrypt(new String(fileContents));
      if (!isJson(plaintextContext)) {
        throw new AuthenticationFailedException("Error: bad password");
      }
      val serializedVault = condensation.read(SerializedVault.class, plaintextContext);
      serializedVault.setPassword(password);
      serializedVault.setDescriptor(vaultDescriptor);
      serializedVault.setVaultManager(this);
      openVaults.put(serializedVault.getId(), new DescriptorPair(serializedVault, vaultDescriptor));
      return serializedVault;
    }
  }

  private boolean isJson(CharSequence plaintextContext) {
    return plaintextContext.toString().startsWith("{\"id\":");
  }

  @Override
  public String toString() {
    return "Vault Manager{\n" + "provider: File\n" + "root directory: " + directory + "\n" + "}";
  }

  @Data
  static final class DescriptorPair {

    final Vault vault;
    final VaultDescriptor descriptor;
  }
}
