package io.sunshower.gyre;

import java.util.*;
import lombok.val;

public abstract class AbstractTrieMap<K, T, V> implements TrieMap<K, V> {

  /** mutable state */
  private int count;

  /** immutable state */
  final Entry<K, T, V> root;

  final Analyzer<K, T> analyzer;

  public AbstractTrieMap(Analyzer<K, T> analyzer) {
    this.analyzer = analyzer;
    this.root = createRoot();
  }

  public AbstractTrieMap(Analyzer<K, T> analyzer, Map<? extends K, ? extends V> clone) {
    this.analyzer = analyzer;
    this.root = createRoot();
    putAll(clone);
  }

  @Override
  public int size() {
    return count;
  }

  @Override
  public boolean isEmpty() {
    return count == 0;
  }

  @Override
  public boolean containsKey(Object key) {

    K k = (K) key;
    Entry<K, T, V> current = root;

    Iterator<T> segments = analyzer.segments(k);

    while (segments.hasNext()) {
      val next = segments.next();
      val child = current.locate(k, next);
      if (child == null) {
        return false;
      }
      current = child;
    }
    return true;
  }

  public List<V> descendents(K key) {

    Entry<K, T, V> current = root;
    Iterator<T> segments = analyzer.segments(key);
    while (segments.hasNext()) {
      val segment = segments.next();
      val child = current.locate(key, segment);
      if (child == null) {
        return Collections.emptyList();
      }
      current = child;
    }

    if (current == null) {
      return Collections.emptyList();
    }

    List<V> children = new ArrayList<>();
    Stack<Entry<K, T, V>> stack = new Stack<>();

    val iter = current.iterator();
    while (iter.hasNext()) {
      stack.push(iter.next());
    }

    while (!stack.empty()) {
      val next = stack.pop();
      if (!next.internal) {
        children.add(next.value);
      }
      val citer = next.iterator();
      while (citer.hasNext()) {
        stack.push(citer.next());
      }
    }
    return children;
  }

  @Override
  public List<V> level(K key) {

    Entry<K, T, V> current = root;
    Iterator<T> segments = analyzer.segments(key);
    while (segments.hasNext()) {
      val segment = segments.next();
      val child = current.locate(key, segment);
      if (child == null) {
        return Collections.emptyList();
      }
      current = child;
    }

    return current.getChildValues();
  }

  @Override
  public boolean containsValue(Object value) {

    val stack = new Stack<Entry<K, T, V>>();
    stack.push(root);
    while (!stack.isEmpty()) {
      val current = stack.pop();

      if (current.value == value || Objects.equals(value, current.value)) {
        return true;
      }

      val iter = current.iterator();
      while (iter.hasNext()) {
        val child = iter.next();
        stack.push(child);
      }
    }

    return false;
  }

  @Override
  @SuppressWarnings("unchecked")
  public V get(Object key) {

    K k = (K) key;
    Entry<K, T, V> current = root;
    Iterator<T> segments = analyzer.segments(k);

    while (segments.hasNext()) {
      val segment = segments.next();

      val child = current.locate(k, segment);
      if (child == null) {
        return null;
      }
      current = child;
    }

    return current.getValue();
  }

  @Override
  public V put(K key, V value) {
    Entry<K, T, V> current = root;
    Iterator<T> segments = analyzer.segments(key);
    while (segments.hasNext()) {
      T seg = segments.next();
      Entry<K, T, V> child = current.locate(key, seg);
      if (child == null) {
        child = current.create(key, seg);
      }

      current = child;
    }

    if (current.internal) {
      count++;
    }

    //    if(!current.internal) {
    //      count++;
    //    }
    return current.setValue(value);
  }

  @Override
  public V remove(Object key) {
    K k = (K) key;
    Entry<K, T, V> parent = null;
    Entry<K, T, V> current = root;
    Iterator<T> segments = analyzer.segments(k);

    while (segments.hasNext()) {
      T seg = segments.next();
      Entry<K, T, V> child = current.locate(k, seg);

      if (child == null) {
        return null;
      }
      parent = current;
      current = child;
    }
    count--;
    return parent.remove(current);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    for (val e : m.entrySet()) {
      put(e.getKey(), e.getValue());
    }
  }

  @Override
  public void clear() {
    root.clear();
    count = 0;
  }

  @Override
  public Set<K> keySet() {
    return new KeySet();
  }

  @Override
  public Collection<V> values() {
    return new ValueCollection();
  }

  @Override
  public Set<Map.Entry<K, V>> entrySet() {
    return new TrieEntrySet();
  }

  protected abstract Entry<K, T, V> createRoot();

  @Override
  public String toString() {

    StringBuilder result = new StringBuilder();

    write(result, root, "", "");
    return result.toString();
  }

  private void write(StringBuilder result, Entry<K, T, V> node, String prefix, String childPrefix) {
    result.append(prefix);
    result.append(
        String.format(
            "(k:%s -> v:%s)", node.identity == null ? "(root)" : node.identity, node.value));
    result.append("\n");

    val iter = node.iterator();
    while (iter.hasNext()) {
      Entry<K, T, V> next = iter.next();
      if (iter.hasNext()) {
        write(result, next, childPrefix + "├── ", childPrefix + "│   ");
      } else {
        write(result, next, childPrefix + "└── ", childPrefix + "    ");
      }
    }
  }

  final <X> X[] fill(X[] values, Iterator<X> iterator) {
    int c = 0;
    while (c < count) {
      values[c++] = iterator.next();
    }
    return values;
  }

  final class ValueCollection extends AbstractCollection<V> {

    @Override
    @SuppressWarnings("unchecked")
    public Object[] toArray() {
      return fill(new Object[count], (Iterator<Object>) new ValueIterator());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> X[] toArray(X[] fill) {
      if (fill.length < count) {
        return (X[]) toArray();
      }
      return fill(fill, (Iterator<X>) new ValueIterator());
    }

    @Override
    public Iterator<V> iterator() {
      return new ValueIterator();
    }

    @Override
    public int size() {
      return count;
    }
  }

  final class TrieEntrySet extends AbstractSet<Map.Entry<K, V>> {

    @Override
    public Iterator<Map.Entry<K, V>> iterator() {
      return new EntryIterator();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object[] toArray() {
      return fill(new Entry[count], new EntryIterator());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> X[] toArray(X[] a) {
      if (a.length != count) {
        return (X[]) toArray();
      }
      return fill(a, (Iterator<X>) new EntryIterator());
    }

    @Override
    public boolean add(Map.Entry<K, V> kvEntry) {
      return put(kvEntry.getKey(), kvEntry.getValue()) != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
      return AbstractTrieMap.this.remove(((Map.Entry) o).getKey()) != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean containsAll(Collection<?> c) {
      for (val kv : c) {
        val kve = (Map.Entry<K, V>) kv;
        if (!containsKey(kve.getKey())) {
          return false;
        }
      }
      return true;
    }

    @Override
    public boolean addAll(Collection<? extends Map.Entry<K, V>> c) {
      boolean modified = false;
      for (val kv : c) {
        modified |= put(kv.getKey(), kv.getValue()) != null;
      }
      return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      boolean modified = false;
      for (val kv : c) {
        modified |= remove(kv);
      }
      return modified;
    }

    @Override
    public void clear() {
      AbstractTrieMap.this.clear();
    }

    @Override
    public int size() {
      return count;
    }

    @Override
    public boolean isEmpty() {
      return count == 0;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
      if (o instanceof Map.Entry) {
        val e = (Map.Entry<K, V>) o;

        Entry<K, T, V> current = root;
        val key = e.getKey();
        val segments = analyzer.segments(key);
        while (segments.hasNext()) {
          val next = segments.next();
          val child = current.locate(key, next);
          if (child == null) {
            return false;
          }
          current = child;
        }
        return current.value == e.getValue() || Objects.equals(current.value, e.getValue());
      }
      return false;
    }
  }

  final class KeyIterator extends TreeIterator<K> {

    @Override
    K extract(Entry<K, T, V> v) {
      return v.key;
    }
  }

  final class ValueIterator extends TreeIterator<V> {

    @Override
    V extract(Entry<K, T, V> v) {
      return v.value;
    }
  }

  final class EntryIterator extends TreeIterator<Map.Entry<K, V>> {
    @Override
    Map.Entry<K, V> extract(Entry<K, T, V> v) {
      return v;
    }
  }

  abstract class TreeIterator<E> implements Iterator<E> {

    private Entry<K, T, V> current;
    private final Stack<Entry<K, T, V>> stack;
    private final Queue<Entry<K, T, V>> results;

    TreeIterator() {
      current = root;
      stack = new Stack<>();
      stack.push(current);
      results = new LinkedList<>();
      fillLevel();
    }

    protected void fillLevel() {

      while (results.isEmpty() && !stack.empty()) {
        val c = stack.pop();

        val iterator = c.iterator();
        while (iterator.hasNext()) {
          val child = iterator.next();
          stack.push(child);
          if (!child.internal) {
            results.add(child);
          }
        }
      }
    }

    @Override
    public boolean hasNext() {
      return !results.isEmpty();
    }

    abstract E extract(Entry<K, T, V> v);

    @Override
    public E next() {
      val next = results.poll();
      stack.push(next);
      val result = extract(next);
      if (results.isEmpty()) {
        fillLevel();
      }
      return result;
    }
  }

  final class KeySet extends AbstractSet<K> {

    @Override
    public int size() {
      return count;
    }

    @Override
    public boolean isEmpty() {
      return count == 0;
    }

    @Override
    public boolean contains(Object o) {
      return AbstractTrieMap.this.containsKey(o);
    }

    @Override
    public Iterator<K> iterator() {
      return new KeyIterator();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object[] toArray() {
      return fill(new Object[count], (Iterator<Object>) new KeyIterator());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {

      if (a.length != count) {
        return (T[]) toArray();
      }
      return fill(a, (Iterator<T>) new KeyIterator());
    }

    @Override
    public boolean add(K k) {
      throw new UnsupportedOperationException("add() is not supported!");
    }

    @Override
    public boolean remove(Object o) {
      return AbstractTrieMap.this.remove(o) != null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {

      for (val k : c) {
        if (!containsKey(k)) {
          return false;
        }
      }
      return true;
    }

    @Override
    public boolean addAll(Collection<? extends K> c) {
      throw new UnsupportedOperationException("addAll() is not supported!");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
      boolean modified = false;
      for (val k : c) {
        modified |= remove(k);
      }
      return modified;
    }

    @Override
    public void clear() {
      AbstractTrieMap.this.clear();
    }
  }

  protected abstract static class Entry<K, T, V> implements Map.Entry<K, V> {

    protected K key;
    protected V value;
    protected T identity;
    protected boolean internal;

    protected Entry() {
      this.internal = true;
    }

    protected Entry(K key, T identity, V value) {
      this.key = key;
      this.value = value;
      this.internal = true;
      this.identity = identity;
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
      internal = false;
      val result = this.value;
      this.value = value;
      return result;
    }

    @Override
    public String toString() {
      return String.format("%s -> %s", identity, value);
    }

    public abstract Iterator<? extends Entry<K, T, V>> iterator();

    public abstract void clear();

    public abstract List<V> getChildValues();

    public abstract Entry<K, T, V> create(K key, T seg);

    public abstract Entry<K, T, V> locate(K key, T seg);

    public abstract V remove(Entry<K, T, V> current);
  }
}
