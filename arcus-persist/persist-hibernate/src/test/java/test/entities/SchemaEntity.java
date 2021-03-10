package test.entities;

import io.sunshower.persistence.core.DistributableEntity;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "Entity", schema = "TEST_SCHEMA")
public class SchemaEntity extends DistributableEntity {}
