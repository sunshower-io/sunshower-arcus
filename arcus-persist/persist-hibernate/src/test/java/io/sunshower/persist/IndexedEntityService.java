package io.sunshower.persist;

import javax.persistence.EntityManager;
import org.hibernate.search.jpa.FullTextEntityManager;

public interface IndexedEntityService {

  EntityManager entityManager();

  FullTextEntityManager getFtEntityManager();
}
