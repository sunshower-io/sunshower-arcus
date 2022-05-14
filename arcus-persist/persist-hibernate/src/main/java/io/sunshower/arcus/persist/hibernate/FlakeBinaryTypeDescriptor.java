package io.sunshower.arcus.persist.hibernate;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.hibernate.type.descriptor.ValueBinder;
import org.hibernate.type.descriptor.ValueExtractor;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.jdbc.BasicBinder;
import org.hibernate.type.descriptor.jdbc.BasicExtractor;
import org.hibernate.type.descriptor.jdbc.JdbcType;


public class FlakeBinaryTypeDescriptor implements JdbcType {

  public static final JdbcType INSTANCE = new FlakeBinaryTypeDescriptor();


  @Override
  public int getJdbcTypeCode() {
    return Types.BINARY;
  }

  @Override
  public <X> ValueBinder<X> getBinder(JavaType<X> javaTypeDescriptor) {
    return new FlakeValueBinder<>(javaTypeDescriptor, this);
  }

  @Override
  public <X> ValueExtractor<X> getExtractor(JavaType<X> javaTypeDescriptor) {
    return new FlakeValueExtractor<>(javaTypeDescriptor, this);
  }


  private static final class FlakeValueBinder<X> extends BasicBinder<X> implements ValueBinder<X> {

    FlakeValueBinder(final JavaType<X> typeDescriptor,
        JdbcType sqlTypeDescriptor) {
      super(typeDescriptor, sqlTypeDescriptor);
    }

    protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
        throws SQLException {
      st.setBytes(index, (byte[]) getJavaType().unwrap(value, byte[].class, options));
    }

    protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
        throws SQLException {
      st.setBytes(name, (byte[]) getJavaType().unwrap(value, byte[].class, options));
    }
  }

  private static final class FlakeValueExtractor<X> extends BasicExtractor<X> {

    public FlakeValueExtractor(final JavaType<X> descriptor, JdbcType host) {
      super(descriptor, host);
    }

    @Override
    protected X doExtract(ResultSet rs, int paramIndex, WrapperOptions options)
        throws SQLException {
      return getJavaType().wrap(rs.getByte(paramIndex), options);
    }

    protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
      return getJavaType().wrap(rs.getBytes(name), options);
    }

    protected X doExtract(CallableStatement statement, int index, WrapperOptions options)
        throws SQLException {
      return getJavaType().wrap(statement.getBytes(index), options);
    }

    protected X doExtract(CallableStatement statement, String name, WrapperOptions options)
        throws SQLException {
      return getJavaType().wrap(statement.getBytes(name), options);
    }
  }

}