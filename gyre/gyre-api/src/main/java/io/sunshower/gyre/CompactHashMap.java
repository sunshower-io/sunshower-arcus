package io.sunshower.gyre;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.*;
import lombok.val;

@SuppressFBWarnings
@SuppressWarnings({"unchecked", "PMD"})
public class CompactHashMap<K, V> implements Map<K, V> {

  private static final float DEFAULT_LOAD_FACTOR = 0.75F;

  private final float loadFactor;

  protected Entry<K, V>[] table;

  /** the size of the table (including unfilled positions) */
  private final int size;

  /** the number of records filled */
  private int filled;

  /** */
  private int maxProbe;

  public CompactHashMap() {
    this(10);
  }

  public CompactHashMap(int initialCapacity) {
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
  }

  public CompactHashMap(int initialCapacity, float loadFactor) {
    this.filled = 0;
    this.maxProbe = 0;
    this.size = initialCapacity;
    this.loadFactor = loadFactor;
    this.table = new Entry[initialCapacity];
  }

  @Override
  public int size() {
    return filled;
  }

  @Override
  public boolean isEmpty() {
    return filled == 0;
  }

  @Override
  public boolean containsKey(Object key) {
    int hashCode = codeFor(key);
    var i = hashCode & (table.length - 1);
    var probe = 0;
    while (probe <= maxProbe) {
      val current = table[i];
      if (current == null || current.probe < probe) {
        return false;
      }
      if (hashCode == current.hashcode
          && (key == current.key || Objects.equals(key, current.key))) {
        return true;
      }
      i = probe(hashCode, probe++);
    }
    return false;
  }

  @Override
  public boolean containsValue(Object value) {
    for (val e : table) {
      if (e != null) {
        if (e.value == value || Objects.equals(e.value, value)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public V get(Object key) {

    int hashCode = codeFor(key);
    int i = hashCode & (table.length - 1);
    int probe = 0;

    while (probe <= maxProbe) {
      val current = table[i];
      if (current == null || current.probe < probe) {
        return null;
      }

      if (hashCode == current.hashcode
          && (key == current.key || Objects.equals(key, current.key))) {
        return current.value;
      }
      i = probe(hashCode, probe++);
    }

    return null;
  }

  @Override
  public V put(K key, V value) {
    if (table.length * loadFactor <= filled) {
      resize();
    }

    return insert(key, value, codeFor(key), null);
  }

  @Override
  public V remove(Object key) {

    int hashcode = codeFor(key);
    int i = mod(hashcode, table.length);
    int probe = 0;
    int index = 0;
    V result = null;

    while (probe <= maxProbe) {
      val current = table[i];

      if (current == null || current.probe < probe) {
        break;
      }

      if (hashcode == current.hashcode
          && (key == current.key || Objects.equals(key, current.key))) {
        index = i;
        result = current.value;
        break;
      }

      i = probe(hashcode, probe++);
    }

    if (result == null) {
      return null;
    }
    filled = filled - 1;

    for (; ; ) {
      i = probe(i, probe);

      val current = table[i];
      if (current == null || current.probe == 0) {
        break;
      }
    }

    int idx = index;
    for (; ; ) {

      int j = probe(idx, probe);

      if (j == i) {
        table[idx] = null;
        break;
      }

      val next = table[j];
      next.probe = next.probe - 1;
      table[idx] = next;
      idx = j;
    }

    return result;
  }

  protected int probe(int idx, int attempt) {
    int len = table.length;
    if (idx == len - 1) {
      return 0;
    }
    val r = mod((1 + (idx + attempt)), len);
    return r;
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
      put(e.getKey(), e.getValue());
    }
  }

  @Override
  public void clear() {
    this.filled = 0;
    this.maxProbe = 0;
    this.table = new Entry[size];
  }

  @Override
  public Set<K> keySet() {
    return new ViewSet();
  }

  @Override
  public Collection<V> values() {
    return new ValueView();
  }

  @Override
  public Set<Map.Entry<K, V>> entrySet() {
    return null;
  }

  private int codeFor(Object key) {
    return key == null ? 0 : key.hashCode();
  }

  private int mod(int x, int y) {
    int result = x % y;
    if (result < 0) {
      result += y;
    }
    return result;
  }

  private V insert(K k, V v, int h, Entry<K, V> cache) {

    K key = k;
    V value = v;
    int hashCode = h;

    V result = null;
    int probe = 0;
    int i = mod(hashCode, table.length);
    for (; ; ) {

      val current = table[i];

      if (current == null) {
        if (cache != null) {
          table[i] = cache;
          cache.hashcode = hashCode;
          cache.key = key;
          cache.value = value;
          cache.probe = probe;
        } else {
          table[i] = new Entry<>(key, value, hashCode, probe);
        }

        ++filled;

        if (probe > maxProbe) {
          maxProbe = probe;
        }
        break;

      } else if (hashCode == current.hashcode
          && (key == current.key || Objects.equals(key, current.key))) {
        result = current.value;
        current.value = value;
        break;
      } else if (current.probe < probe) {
        if (probe > maxProbe) {
          maxProbe = probe;
        }

        val previousKey = current.key;
        val previousValue = current.value;
        val previousProbe = current.probe;
        val previousHash = current.hashcode;

        current.key = key;
        current.probe = probe;
        current.value = value;
        current.hashcode = hashCode;

        key = previousKey;
        hashCode = previousHash;
        value = previousValue;
        probe = previousProbe;
      }
      i = probe(h, probe++);
    }
    return result;
  }

  private void resize() {
    val previousTable = table;
    val previousLength = table.length;

    filled = 0;
    maxProbe = 0;
    table = new Entry[(previousLength == 0 ? 1 : previousLength) * 2];

    int count = 0;
    while (count < previousLength) {
      val e = previousTable[count++];
      if (e != null) {
        insert(e.key, e.value, e.hashcode, e);
      }
    }
  }

  final class KeyIterator implements Iterator<K> {
    int cursor;
    int found;

    KeyIterator() {
      found = 0;
      cursor = 0;
    }

    @Override
    public boolean hasNext() {
      return found < filled;
    }

    @Override
    public K next() {

      if (found < filled) {
        int len = table.length;
        for (int i = cursor; i < len; i++) {
          val next = table[i];
          if (next != null) {
            found++;
            cursor = i + 1;
            return next.key;
          }
        }
      }
      throw new NoSuchElementException("No more elements");
    }
  }

  final class ValueIterator implements Iterator<V> {
    int cursor;
    int found;

    @Override
    public boolean hasNext() {
      return found < filled;
    }

    @Override
    public V next() {
      if (found < filled) {
        int len = table.length;
        for (int i = cursor; i < len; i++) {
          val next = table[i];
          if (next != null) {
            found++;
            cursor = i + 1;
            return next.value;
          }
        }
      }
      throw new NoSuchElementException("No more elements");
    }
  }

  final class ValueView extends AbstractCollection<V> {

    @Override
    public Iterator<V> iterator() {
      return new ValueIterator();
    }

    @Override
    public int size() {
      return filled;
    }
  }

  final class ViewSet extends AbstractSet<K> {

    @Override
    public int size() {
      return filled;
    }

    @Override
    public boolean isEmpty() {
      return isEmpty();
    }

    @Override
    public boolean contains(Object o) {
      return containsKey(o);
    }

    @Override
    public Iterator<K> iterator() {
      return new KeyIterator();
    }

    @Override
    public Object[] toArray() {
      val result = new Object[filled];

      val iterator = iterator();
      int count = 0;
      while (iterator.hasNext()) {
        result[count++] = iterator.next();
      }
      return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
      if (a.length != filled) {
        return (T[]) toArray();
      }
      val iterator = iterator();
      int count = 0;
      while (iterator.hasNext()) {
        a[count++] = (T) iterator.next();
      }
      return a;
    }

    @Override
    public boolean add(K k) {
      throw new UnsupportedOperationException("add() is not supported");
    }

    @Override
    public boolean remove(Object o) {
      return CompactHashMap.this.remove(o) != null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
      if (c == null) {
        return false;
      }

      for (val k : c) {
        if (!containsKey(k)) {
          return false;
        }
      }
      return true;
    }

    @Override
    public boolean addAll(Collection<? extends K> c) {
      throw new UnsupportedOperationException("addAll() is not supported");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      boolean modified = false;
      for (val k : c) {
        // not optimal performing possibly 2 lookups, but incorrect otherwise
        modified |= containsKey(k) && CompactHashMap.this.remove(k) != null;
      }
      return modified;
    }

    @Override
    public void clear() {
      CompactHashMap.this.clear();
    }
  }

  static final class Entry<K, V> implements Map.Entry<K, V> {
    private K key;
    private V value;
    private int probe;
    private int hashcode;

    Entry(K key) {
      this(key, null);
    }

    Entry(K key, V value) {
      this(key, value, 0, 0);
    }

    Entry(K key, V value, int hashcode, int probe) {
      this.key = key;
      this.value = value;
      this.probe = probe;
      this.hashcode = hashcode;
    }

    @Override
    public K getKey() {
      return key;
    }

    @Override
    public V getValue() {
      return value;
    }

    @Override
    public V setValue(V value) {
      val result = this.value;
      this.value = value;
      return result;
    }
  }
}
