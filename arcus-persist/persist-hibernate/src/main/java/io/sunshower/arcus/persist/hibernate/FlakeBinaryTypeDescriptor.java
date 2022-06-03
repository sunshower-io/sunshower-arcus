package io.sunshower.arcus.persist.hibernate;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaTypeDescriptor;
import org.hibernate.type.descriptor.sql.BasicBinder;
import org.hibernate.type.descriptor.sql.BasicExtractor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

public class FlakeBinaryTypeDescriptor implements SqlTypeDescriptor {

  public static final SqlTypeDescriptor INSTANCE = new FlakeBinaryTypeDescriptor();

  @Override
  public int getSqlType() {
    return Types.BINARY;
  }

  @Override
  public boolean canBeRemapped() {
    return true;
  }

  @Override
  public <X> ValueBinder<X> getBinder(JavaTypeDescriptor<X> javaTypeDescriptor) {
    return new FlakeValueBinder<>(javaTypeDescriptor, this);
  }

  @Override
  public <X> ValueExtractor<X> getExtractor(JavaTypeDescriptor<X> javaTypeDescriptor) {
    return new FlakeValueExtractor<>(javaTypeDescriptor, this);
  }

  private static final class FlakeValueBinder<X> extends BasicBinder<X> implements ValueBinder<X> {

    FlakeValueBinder(
        final JavaTypeDescriptor<X> typeDescriptor, SqlTypeDescriptor sqlTypeDescriptor) {
      super(typeDescriptor, sqlTypeDescriptor);
    }

    protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
        throws SQLException {
      st.setBytes(index, (byte[]) getJavaDescriptor().unwrap(value, byte[].class, options));
    }

    protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
        throws SQLException {
      st.setBytes(name, (byte[]) getJavaDescriptor().unwrap(value, byte[].class, options));
    }
  }

  private static final class FlakeValueExtractor<X> extends BasicExtractor<X> {

    public FlakeValueExtractor(final JavaTypeDescriptor<X> descriptor, SqlTypeDescriptor host) {
      super(descriptor, host);
    }

    //    @Override
    //    protected X doExtract(ResultSet rs, int paramIndex, WrapperOptions options)
    //        throws SQLException {
    //      return getJavaDescriptor().wrap(rs.getBytes(paramIndex), options);
    //    }

    protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
      return getJavaDescriptor().wrap(rs.getBytes(name), options);
    }

    protected X doExtract(CallableStatement statement, int index, WrapperOptions options)
        throws SQLException {
      return getJavaDescriptor().wrap(statement.getBytes(index), options);
    }

    protected X doExtract(CallableStatement statement, String name, WrapperOptions options)
        throws SQLException {
      return getJavaDescriptor().wrap(statement.getBytes(name), options);
    }
  }
}
