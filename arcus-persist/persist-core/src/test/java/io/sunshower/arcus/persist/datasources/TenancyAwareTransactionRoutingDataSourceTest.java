package io.sunshower.arcus.persist.datasources;


import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PersistenceCoreTestConfiguration.class)
class TenancyAwareTransactionRoutingDataSourceTest {}
