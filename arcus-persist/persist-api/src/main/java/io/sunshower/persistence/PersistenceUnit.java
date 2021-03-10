package io.sunshower.persistence;

import io.sunshower.persistence.annotations.Persistence;
import java.util.*;
import java.util.stream.Collectors;
import lombok.val;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;

public class PersistenceUnit {
  private final Dialect dialect;
  private final Map<String, PersistenceConfiguration> configurations;

  public PersistenceUnit(final Dialect dialect, ApplicationContext context) {
    this.dialect = dialect;
    configurations = new HashMap<>();
    configure((ConfigurableListableBeanFactory) context.getAutowireCapableBeanFactory());
  }

  public String[] getScannedPackages() {
    return configurations
        .values()
        .stream()
        .flatMap(cfg -> cfg.packagesToScan.stream())
        .collect(Collectors.toSet())
        .toArray(new String[0]);
  }

  public Set<PersistenceConfiguration> configurations() {
    val sorted = new ArrayList<PersistenceConfiguration>(configurations.values());
    Collections.sort(sorted);
    return new LinkedHashSet<>(sorted);
  }

  public void addConfiguration(PersistenceConfiguration cfg) {
    final PersistenceConfiguration existing = configurations.get(cfg.getId());
    if (existing != null) {
      existing.merge(cfg);
    } else {
      configurations.put(cfg.getId(), cfg);
    }
  }

  private void configure(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    final String[] beanNamesForAnnotation =
        beanFactory.getBeanNamesForAnnotation(Persistence.class);
    for (String beanName : beanNamesForAnnotation) {
      try {
        final Set<String> packageNames = new HashSet<>();
        final Set<String> migrationPaths = new HashSet<>();
        final Set<Class<?>> entityTypes = new HashSet<>();
        final Persistence persistence =
            beanFactory.findAnnotationOnBean(beanName, Persistence.class);
        include(packageNames, migrationPaths, entityTypes, persistence);
        final PersistenceConfiguration cfg =
            new PersistenceConfiguration(
                persistence.id(),
                persistence.schema(),
                persistence.order(),
                migrationPaths,
                packageNames,
                entityTypes);
        addConfiguration(cfg);
      } catch (NoSuchBeanDefinitionException ex) {
      }
    }
  }

  private void include(
      Set<String> packageNames,
      Set<String> migrationPaths,
      Set<Class<?>> entityTypes,
      Persistence type) {
    packageNames.addAll(Arrays.asList(type.scannedPackages()));
    migrationPaths.addAll(locations(type.migrationLocations()));
    entityTypes.addAll(Arrays.asList(type.entities()));
  }

  private List<String> locations(String[] strings) {
    return Arrays.stream(strings).map(t -> parseLocation(t, dialect)).collect(Collectors.toList());
  }

  public static String parseLocation(String s, Dialect dialect) {
    return s.replaceAll("\\{dialect}", dialect.key);
  }
}
