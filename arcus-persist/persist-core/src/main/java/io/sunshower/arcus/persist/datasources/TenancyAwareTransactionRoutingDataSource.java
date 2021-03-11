package io.sunshower.arcus.persist.datasources;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class TenancyAwareTransactionRoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return null;
    }
}
