package io.sunshower.persist.hibernate.types;

import io.sunshower.common.Identifier;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Comparator;
import org.hibernate.HibernateException;
import org.hibernate.engine.jdbc.BinaryStream;
import org.hibernate.engine.jdbc.internal.BinaryStreamImpl;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.*;

public class FlakeSQLTypeDescriptor extends AbstractTypeDescriptor<Identifier> {
  public static final FlakeSQLTypeDescriptor INSTANCE = new FlakeSQLTypeDescriptor();

  public FlakeSQLTypeDescriptor() {
    super(Identifier.class, IdentifierMutabilityPlan.instance);
  }

  public boolean areEqual(Identifier fst, Identifier snd) {
    return fst == snd || fst != null && snd != null && fst.equals(snd);
  }

  public int extractHashCode(Identifier id) {
    if (id == null) {
      return 0;
    }
    byte[] bytes = id.getId();
    int hashCode = 1;
    byte[] var3 = bytes;
    int var4 = bytes.length;

    for (int var5 = 0; var5 < var4; ++var5) {
      byte aByte = var3[var5];
      hashCode = 31 * hashCode + aByte;
    }
    return hashCode;
  }

  public String toString(Identifier bytes) {
    return bytes.toString();
  }

  public String extractLoggableRepresentation(Identifier value) {
    return value == null ? super.extractLoggableRepresentation(null) : String.valueOf(value);
  }

  public Identifier fromString(String string) {
    if (string == null) {
      return null;
    }
    return Identifier.valueOf(string);
  }

  public Comparator<Identifier> getComparator() {
    return Identifier::compareTo;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <X> X unwrap(Identifier value, Class<X> type, WrapperOptions wrapperOptions) {
    if (value == null) {
      return null;
    } else if (Identifier.class.isAssignableFrom(type)) {
      return (X) value;
    } else if (byte[].class.isAssignableFrom(type)) {
      return (X) value.getId();
    } else if (InputStream.class.isAssignableFrom(type)) {
      return (X) new ByteArrayInputStream(value.getId());
    } else if (BinaryStream.class.isAssignableFrom(type)) {
      return (X) new BinaryStreamImpl(value.getId());
    } else if (Blob.class.isAssignableFrom(type)) {
      return (X) wrapperOptions.getLobCreator().createBlob(value.getId());
    } else {
      throw this.unknownUnwrap(type);
    }
  }

  public <X> Identifier wrap(X value, WrapperOptions options) {
    if (value == null) {
      return null;

    } else if (Identifier.class.isInstance(value)) {
      return (Identifier) value;
    } else if (byte[].class.isInstance(value)) {
      return Identifier.valueOf(((byte[]) value));
    } else if (InputStream.class.isInstance(value)) {
      return Identifier.valueOf(DataHelper.extractBytes((InputStream) value));
    } else if (!Blob.class.isInstance(value) && !DataHelper.isNClob(value.getClass())) {
      throw this.unknownWrap(value.getClass());
    } else {
      try {
        return Identifier.valueOf(DataHelper.extractBytes(((Blob) value).getBinaryStream()));
      } catch (SQLException var4) {
        throw new HibernateException("Unable to access lob stream", var4);
      }
    }
  }

  static class IdentifierMutabilityPlan extends MutableMutabilityPlan<Identifier> {

    static IdentifierMutabilityPlan instance = new IdentifierMutabilityPlan();

    @Override
    protected Identifier deepCopyNotNull(Identifier identifier) {
      return Identifier.valueOf(identifier.getBytes());
    }
  }
}
