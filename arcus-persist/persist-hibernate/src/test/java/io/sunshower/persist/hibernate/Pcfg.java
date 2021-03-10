package io.sunshower.persist.hibernate;

import io.sunshower.persistence.annotations.Persistence;

@Persistence(
  id = "audit",
  scannedPackages = {
    "test.entities",
    "test.entities.one2one",
    "test.entities.one2many",
    "test.entities.many2many",
  },
  migrationLocations = "classpath:{dialect}"
)
public class Pcfg {}
