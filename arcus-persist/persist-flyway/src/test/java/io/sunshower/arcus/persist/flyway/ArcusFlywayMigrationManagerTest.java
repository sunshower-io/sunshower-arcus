package io.sunshower.arcus.persist.flyway;


import io.sunshower.arcus.persist.hibernate.ArcusPersistenceTest;
import org.springframework.test.context.ContextConfiguration;

@ArcusPersistenceTest
@ContextConfiguration(classes = FlywayTestConfiguration.class)
class ArcusFlywayMigrationManagerTest {}
