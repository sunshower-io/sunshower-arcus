package io.sunshower.persist.jpa;

import static java.lang.String.format;

import io.sunshower.persistence.search.IndexedPersistenceContext;
import io.sunshower.persistence.search.SearchableIndexAware;
import java.lang.reflect.Field;
import javax.persistence.EntityManagerFactory;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class FullTextPostProcessor implements BeanPostProcessor, ApplicationContextAware {

  private EntityManagerFactory entityManagerFactory;

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    final Class<?> injectedClass = bean.getClass();
    final Class<?> type = AopUtils.getTargetClass(bean);
    if (type.isAnnotationPresent(SearchableIndexAware.class)) {
      try {
        inject(injectedClass, type, bean, beanName);
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    }
    return bean;
  }

  private void inject(Class<?> injectedClass, Class<?> type, Object bean, String beanName)
      throws Exception {
    for (final Field field : type.getDeclaredFields()) {
      if (field.isAnnotationPresent(IndexedPersistenceContext.class)) {
        final Class<?> ftype = field.getType();
        if (FullTextEntityManager.class.isAssignableFrom(ftype)) {
          setUnconditionally(injectedClass, bean, field, beanName);
        } else {
          throw new IllegalStateException(
              format(
                  "Cannot inject IndexedEntityManager "
                      + "on field '%s' of '%s'.  "
                      + "Expected class javax.jpa.EntityManager, got '%s'",
                  field.getName(), type, ftype));
        }
      }
    }
  }

  private void setUnconditionally(Class<?> injectedClass, Object bean, Field field, String beanName)
      throws IllegalAccessException, NoSuchFieldException {

    field.setAccessible(true);
    FullTextEntityManager fullTextEntityManager =
        Search.getFullTextEntityManager(entityManagerFactory.createEntityManager());
    field.set(bean, fullTextEntityManager);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.entityManagerFactory = applicationContext.getBean(EntityManagerFactory.class);
  }
}
