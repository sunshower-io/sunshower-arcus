package io.sunshower.arcus.persist.hibernate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Retention(RetentionPolicy.RUNTIME)
@ContextConfiguration(classes = TestPersistenceConfiguration.class)
public @interface ArcusPersistenceTest {


}
