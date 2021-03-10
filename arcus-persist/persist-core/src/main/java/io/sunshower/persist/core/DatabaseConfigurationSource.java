package io.sunshower.persist.core;

public interface DatabaseConfigurationSource {

  String driverClass();

  boolean baseline();

  String version();

  String jndiPath();

  String url();

  String username();

  String password();

  //
  //    public boolean useLocation() {
  //        return nullOrEmpty(jndiPath);
  //    }
  //
  //    private void check() {
  //        if(!(nullOrEmpty(jndiPath) || nullOrEmpty(username))) {
  //            throw new IllegalStateException("Only one of jndi-path or username may be set");
  //        }
  //    }
  //
  //    private boolean nullOrEmpty(String value) {
  //        if(value == null || value.trim().equals("")) {
  //            return true;
  //        }
  //        return false;
  //    }
  //
  //    public void validate() {
  //        check();
  //    }

  //
  //    private String url;
  //
  //    private String username;
  //
  //    private String password;
  //
  //    @Value("${jdbc.driverClass:}")
  //    private String driverClass;
  //
  //    @Value("${jdbc.baseline:0}")
  //    private boolean baseline;
  //
  //    @Value("${jdbc.baseline-version:-1}")
  //    private String version;
  //
  //
  //    @Value("${jdbc.jndi-location:}")
  //    private String jndiPath;
  //
  //
  //
  //
  //    public String getUrl() {
  //        return url;
  //    }
  //
  //    public void setUrl(String url) {
  //        this.url = url;
  //    }
  //
  //    public String getUsername() {
  //        return username;
  //    }
  //
  //    public void setUsername(String username) {
  //        this.username = username;
  //    }
  //
  //    public String getPassword() {
  //        return password;
  //    }
  //
  //    public void setPassword(String password) {
  //        this.password = password;
  //    }
  //
  //    public String getDriverClass() {
  //        return driverClass;
  //    }
  //
  //    public boolean isBaseline() {
  //        return baseline;
  //    }
  //
  //    public boolean baselineVersion() {
  //        return baseline && !(version == null || version.trim().isEmpty() ||
  // "-1".equals(version));
  //    }
  //
  //
  //    public String getJndiPath() {
  //        return jndiPath;
  //    }
  //
  //    public void setJndiPath(String jndiPath) {
  //        this.jndiPath = jndiPath;
  //    }
  //
  //    public void setBaseline(boolean baseline) {
  //        this.baseline = baseline;
  //    }
  //
  //    public String getVersion() {
  //        return version;
  //    }
  //
  //    public void setVersion(String version) {
  //        this.version = version;
  //    }
  //    public void setDriverClass(String driverClass) {
  //        this.driverClass = driverClass;
  //    }
  //
  //    public HikariConfig toNative() {
  //        final HikariConfig cfg = new HikariConfig();
  //        cfg.setJdbcUrl(this.url);
  //        cfg.setUsername(this.username);
  //        cfg.setPassword(this.password);
  //        cfg.setDriverClassName(this.driverClass);
  //        return cfg;
  //    }
  //
  //    public boolean useLocation() {
  //        return nullOrEmpty(jndiPath);
  //    }
  //
  //    private void check() {
  //        if(!(nullOrEmpty(jndiPath) || nullOrEmpty(username))) {
  //            throw new IllegalStateException("Only one of jndi-path or username may be set");
  //        }
  //    }
  //
  //    private boolean nullOrEmpty(String value) {
  //        if(value == null || value.trim().equals("")) {
  //            return true;
  //        }
  //        return false;
  //    }
  //
  //    public void validate() {
  //        check();
  //    }

}
