package io.sunshower.persist;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.lang.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = SpringAnnotationTest.Ctx.class)
public class SpringAnnotationTest {

  @Inject private ApplicationContext applicationContext;

  @Test
  void ensureFindingBeanByCustomAnnotationWorks() {
    final List<String> beansWithAnnotation =
        new AnnotatedBeanLocator(applicationContext).getBeansWithAnnotation(CustomAnnotation.class);
    assertThat(beansWithAnnotation.size(), is(1));
  }

  public static class Ctx {

    @Bean
    @CustomAnnotation
    public String getSweetString() {
      return "Hello";
    }
  }

  public class AnnotatedBeanLocator {

    private final ConfigurableApplicationContext applicationContext;

    public AnnotatedBeanLocator(ApplicationContext applicationContext) {
      this.applicationContext = (ConfigurableApplicationContext) applicationContext;
    }

    public List<String> getBeansWithAnnotation(Class<? extends Annotation> type) {
      Predicate<Map<String, Object>> filter = t -> true;
      return getBeansWithAnnotation(type, filter);
    }

    public List<String> getBeansWithAnnotation(
        Class<? extends Annotation> type, Predicate<Map<String, Object>> attributeFilter) {

      List<String> result = new ArrayList<>();

      ConfigurableListableBeanFactory factory = applicationContext.getBeanFactory();
      for (String name : factory.getBeanDefinitionNames()) {
        BeanDefinition bd = factory.getBeanDefinition(name);

        System.out.println(bd.getSource());
        if (bd.getSource() instanceof StandardMethodMetadata) {
          StandardMethodMetadata metadata = (StandardMethodMetadata) bd.getSource();

          Map<String, Object> attributes = metadata.getAnnotationAttributes(type.getName());
          if (null == attributes) {
            continue;
          }

          if (attributeFilter.test(attributes)) {
            result.add(name);
          }
        }
      }
      return result;
    }
  }

  @Target({ElementType.TYPE, ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  public @interface CustomAnnotation {}
}
