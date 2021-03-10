package io.sunshower.persist.hibernate.types;

import java.sql.*;
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
    return Types.VARBINARY;
  }

  @Override
  public boolean canBeRemapped() {
    return true;
  }

  @Override
  public <X> ValueBinder<X> getBinder(JavaTypeDescriptor<X> javaTypeDescriptor) {

    return new BasicBinder<X>(javaTypeDescriptor, this) {
      protected void doBind(PreparedStatement st, X value, int index, WrapperOptions options)
          throws SQLException {
        st.setBytes(index, (byte[]) javaTypeDescriptor.unwrap(value, byte[].class, options));
      }

      protected void doBind(CallableStatement st, X value, String name, WrapperOptions options)
          throws SQLException {
        st.setBytes(name, (byte[]) javaTypeDescriptor.unwrap(value, byte[].class, options));
      }
    };
  }

  @Override
  public <X> ValueExtractor<X> getExtractor(JavaTypeDescriptor<X> javaTypeDescriptor) {

    return new BasicExtractor<X>(javaTypeDescriptor, this) {
      protected X doExtract(ResultSet rs, String name, WrapperOptions options) throws SQLException {
        return javaTypeDescriptor.wrap(rs.getBytes(name), options);
      }

      protected X doExtract(CallableStatement statement, int index, WrapperOptions options)
          throws SQLException {
        return javaTypeDescriptor.wrap(statement.getBytes(index), options);
      }

      protected X doExtract(CallableStatement statement, String name, WrapperOptions options)
          throws SQLException {
        return javaTypeDescriptor.wrap(statement.getBytes(name), options);
      }
    };
  }
}
