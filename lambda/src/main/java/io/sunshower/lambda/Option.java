package io.sunshower.lambda;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

/**
 * Created by haswell on 3/23/16.
 *
 * Because Java 8's Optional type is stupid
 */
public abstract class Option<T> implements Collection<T>, Serializable {


    private Option() {

    }

    public static final class Some<T> extends Option<T>  {
        final T item;

        public Some(T item) {
            this.item = item;
        }

        public T get() {
            return item;
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return Objects.equals(o, item);
        }

        @Override
        public Iterator<T> iterator() {
            return new SomeIterator<>(item);
        }

        @Override
        public Object[] toArray() {
            return new Object[]{item};
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T1> T1[] toArray(final T1[] a) {
            if(a.length < 1) {
                return (T1[]) Arrays.copyOf(toArray(), 1, a.getClass());
            }
            System.arraycopy(new Object[]{item}, 0, a, 0, 1);
            if(a.length > 1) {
                a[1] = null;
            }
            return a;
        }

        @Override
        public boolean add(T t) {
            return contains(t);
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return c.size() == 1 && c.contains(item);
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            return containsAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return containsAll(c);
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Some.clear() is not supported as Some() is immutable");
        }

        @Override
        public boolean isSome() {
            return true;
        }

        @Override
        public boolean isNone() {
            return false;
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.of(item);
        }

        @Override
        public T getOrElse(Supplier<T> t) {
            return item;
        }

        @Override
        public <U> Option<U> lift(Function<T, U> f) {
            return Option.some(f.apply(item));
        }


        @Override
        public <U> Option<U> fmap(Function<T, U> f) {
            return lift(f);
        }


        public int hashCode() {
            return item == null ? 0 : item.hashCode();
        }

        public boolean equals(Object o) {
            if (o == this) return true;
            if (o == null) return false;
            return Some.class.equals(o.getClass()) && Objects.equals(item, ((Some) o).item);
        }

        private static class SomeIterator<T> implements Iterator<T> {
            private final T item;
            private boolean advanced;
            SomeIterator(T k) {
                this.item = k;
                this.advanced = false;
            }

            @Override
            public boolean hasNext() {
                return !advanced;
            }

            @Override
            public T next() {
                if(!advanced) {
                    advanced = true;
                    return item;
                }
                throw new NoSuchElementException("Some() contains a single element!");
            }
        }
    }


    public static final class None<T> extends Option<T> {
        static final Option<?> NONE = new None<>();

        private None() {}

        @Override
        public T get() {
            throw new NoSuchElementException("None() has nothing to get");
        }

        @Override
        public boolean isSome() {
            return false;
        }

        @Override
        public boolean isNone() {
            return true;
        }

        @Override
        public Optional<T> toOptional() {
            return Optional.empty();
        }

        @Override
        public T getOrElse(Supplier<T> t) {
            return t == null ? null : t.get();
        }

        @Override
        public <U> Option<U> lift(Function<T, U> f) {
            return none();
        }

        @Override
        public <U> Option<U> fmap(Function<T, U> f) {
            return none();
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<T> iterator() {
            return Collections.<T>emptySet().iterator();
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T1> T1[] toArray(T1[] a) {
            return a;
        }

        @Override
        public boolean add(T t) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        public boolean equals(Object o) {
            return o == NONE;
        }

        public int hashCode() {
            return 0;
        }
    }


    public abstract T get();

    public abstract boolean isSome();

    public abstract boolean isNone();

    public abstract Optional<T> toOptional();

    public abstract T getOrElse(Supplier<T> t);

    public abstract <U> Option<U> lift(Function<T, U> f);

    public abstract <U> Option<U> fmap(Function<T, U> f);


    public static <T> Option<T> some(T u) {
        return new Some<>(u);
    }

    @SafeVarargs
    public static <T> Stream<T> bind(T...items) {
        return (items == null || items.length == 0) ?
                Stream.empty() :
                Arrays.stream(items).flatMap(i ->
                        i == null ? Stream.empty() : Stream.of(i));
    }

    @SafeVarargs
    public static <T> Stream<T> stream(Optional<T>...options) {
        return Arrays.stream(options).flatMap(
                opt -> opt.isPresent() ? Stream.of(opt.get()) : Stream.empty());
    }

    @SuppressWarnings("unchecked")
    public static <T> Option<T> none() {
        return (Option<T>) None.NONE;
    }


    public static <T> Option<T> of(T t) {
        return t == null ? none() : some(t);
    }




}
