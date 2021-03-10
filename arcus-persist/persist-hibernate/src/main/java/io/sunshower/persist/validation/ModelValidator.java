package io.sunshower.persist.validation;

import java.util.Set;
import javax.validation.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ModelValidator
    implements Validator, ConstraintValidatorFactory, InitializingBean, ApplicationContextAware {

  private ApplicationContext applicationContext;
  private javax.validation.Validator validator;

  @Override
  public boolean supports(Class<?> clazz) {
    return true;
  }

  @Override
  public void validate(Object target, Errors errors) {

    Set<ConstraintViolation<Object>> validate = validator.validate(target);
    for (ConstraintViolation<Object> violation : validate) {
      final String path = violation.getPropertyPath().toString();
      final String message = violation.getMessage();
      errors.rejectValue(path, message);
    }
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    ValidatorFactory validatorFactory =
        Validation.byDefaultProvider()
            .configure()
            .constraintValidatorFactory(this)
            .buildValidatorFactory();
    validator = validatorFactory.usingContext().getValidator();
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  @Override
  public <T extends ConstraintValidator<?, ?>> T getInstance(Class<T> key) {
    return applicationContext.getBean(key);
  }

  @Override
  public void releaseInstance(ConstraintValidator<?, ?> instance) {}
}
