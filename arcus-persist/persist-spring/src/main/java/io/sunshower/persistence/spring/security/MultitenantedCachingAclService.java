package io.sunshower.persistence.spring.security;

import javax.sql.DataSource;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;

public class MultitenantedCachingAclService extends IdentifierJdbcMutableAclService {

    public MultitenantedCachingAclService(
            DataSource dataSource,
            LookupStrategy lookupStrategy,
            AclCache cache

    ) {
        super(dataSource, lookupStrategy, cache);
    }
}