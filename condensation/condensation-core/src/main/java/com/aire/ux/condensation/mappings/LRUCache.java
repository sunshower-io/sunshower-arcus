package com.aire.ux.condensation.mappings;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LRUCache<K, V> extends LinkedHashMap<K, V> {

  private final int capacity;

  public LRUCache(int capacity) {
    this.capacity = capacity;
  }

  @Override
  protected boolean removeEldestEntry(Entry<K, V> eldest) {
    return size() >= capacity;
  }
}
