package io.sunshower.persist;

import io.sunshower.persistence.search.SearchableIndexAware;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.search.jpa.FullTextEntityManager;

@SearchableIndexAware
public class DefaultIndexedEntityService implements IndexedEntityService {

  @PersistenceContext private EntityManager entityManager;

  @Inject private FullTextEntityManager fullTextEntityManager;

  @Override
  public EntityManager entityManager() {
    return entityManager;
  }

  @Override
  public FullTextEntityManager getFtEntityManager() {
    return fullTextEntityManager;
  }
}
