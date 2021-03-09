package io.sunshower.lambda;

import static io.sunshower.lambda.Option.none;
import static io.sunshower.lambda.Option.some;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class OptionTest {

    @Test
    void ensureSomeOfAnItemIsSome() {
        assertEquals(new Option.Some<>("hello").isSome(), (true));
    }

    @Test
    void ensureSomeIsNotNone() {
        assertEquals(new Option.Some<>("hello").isNone(), (false));
    }

    @Test
    void ensureSizeOfSomeIs1() {
        assertEquals(new Option.Some<>("l").size(), (1));
    }

    @Test
    void ensureSomeIsNotEmpty() {
        assertEquals(new Option.Some<>("").isEmpty(), (false));
    }

    @Test
    void ensureSomeDoesNotContainAnItemThatIsNotItsItem() {
        assertEquals(new Option.Some<>("1").contains("2"), (false));
    }

    @Test
    void ensureSomeContainsAnItemThatIsStructurallyItsItem() {
        assertEquals(new Option.Some<>("1").contains("1"), (true));
    }

    @Test
    void ensureSomeContainsAnItemThatIsReferentiallyItsItem() {
        Object o = new Object();
        assertEquals(new Option.Some<>(o).contains(o), (true));
    }

    @Test
    void ensureIteratorIteratesOnceOverSome() {

        int count = 0;
        Object contents = null;
        for (Object o : new Option.Some<>("hello")) {
            count++;
            contents = o;
        }
        assertEquals(count, (1));
        assertEquals(contents, ("hello"));
    }

    @Test
    void ensureIterationIsItempotent() {
        List<Object> results = new ArrayList<>();
        Object obj = new Object();
        Option<Object> o = new Option.Some<>(obj);
        for (int i = 0; i < 5; ++i) {
            for (Object a : o) {
                results.add(a);
            }
        }
        assertEquals(results.size(), (5));
    }

    @Test
    void ensureToArrayProducesObject() {
        Option.Some<String> a = new Option.Some<>("a");
        Object[] o = a.toArray();
        assertEquals(o.length, (1));
        assertEquals(o[0], ("a"));
    }

    @Test
    void ensureToArrayOnEmptyArrayProducesCorrectArray() {
        Option.Some<String> a = new Option.Some<>("a");
        String[] ar = new String[0];
        String[] o = a.toArray(ar);
        assertEquals(o.length, (1));
        assertEquals(o[0], ("a"));
        assertNotEquals(ar, (o));
    }

    @Test
    void ensureToArrayOnArrayOfSizeOneProducesSameArray() {

        Option.Some<String> a = new Option.Some<>("a");
        String[] ar = new String[1];
        String[] o = a.toArray(ar);
        assertEquals(o.length, (1));
        assertEquals(o[0], ("a"));
        assertTrue(o == ar);
    }

    @Test
    void
            ensureToArrayOnSomeOnArrayOfSizeGreaterThanOneProducesArrayWithElementAtPositionOneEqualToNull() {
        Option.Some<String> a = new Option.Some<>("a");
        String[] ar = new String[] {"1", "2"};
        String[] results = a.toArray(ar);
        assertEquals(results[0], ("a"));
        assertNull(results[1]);
        assertTrue(ar == results);
    }

    @Test
    void ensureSomeCanAddStructurallyEquivalentObject() {
        assertTrue(new Option.Some<>("a").add("a"));
    }

    @Test
    void ensureSomeCanAddReferentiallyEquivalentObject() {
        String o = "a";
        assertTrue(new Option.Some<>(o).add(o));
    }

    @Test
    void ensureRemoveReturnsFalseForStructurallyDifferentObjects() {
        assertFalse(new Option.Some<>(new Object()).remove(new Object()));
    }

    @Test
    void ensureRemoveReturnsFalseForStructurallyEquivalentObjects() {
        assertFalse(new Option.Some<>("a").remove("a"));
    }

    @Test
    void ensureRemoveReturnsFalseForReferentiallyEquivalentObjects() {
        String a = "a";
        assertFalse(new Option.Some<>(a).remove(a));
    }

    @Test
    void ensureSomeContainsAllOfASingletonCollectionOfAStructurallyEquivalentObject() {
        assertTrue(new Option.Some<>("a").containsAll(Collections.singleton("a")));
    }

    @Test
    void ensureSomeContainsAllOfASingletonCollectionOfAReferentiallyEquivalentObject() {
        String a = "a";
        assertTrue(new Option.Some<>(a).containsAll(Collections.singleton(a)));
    }

    @Test
    void ensureSomeContainsNoneOfAnEmptyCollection() {
        assertFalse(new Option.Some<>("a").containsAll(Collections.emptyList()));
    }

    @Test
    void ensureSomeContainsNonOfANonEmptyCollectionWithMoreThanOneElement() {
        assertFalse(new Option.Some<>("a").containsAll(Arrays.asList("1", "a")));
    }

    @Test
    void ensureRemoveAllReturnsFalse() {
        assertFalse(new Option.Some<>("a").removeAll(Arrays.asList("a")));
    }

    @Test
    void ensureRetainAllReturnsFalseForSetWithMoreThanOneElement() {
        assertFalse(new Option.Some<>("a").removeAll(Arrays.asList("a", "b")));
    }

    @Test
    void ensureRetainAllReturnsTrueForSingleElementContainingItsItem() {
        assertTrue(new Option.Some<>("a").retainAll(Arrays.asList("a")));
    }

    @Test
    void ensureClearOnSomeResultsInNone() {
        assertThrows(UnsupportedOperationException.class, () -> new Option.Some<>("a").clear());
    }

    @Test
    void ensureToOptionalProducesCorrectOptional() {
        Optional<String> a = new Option.Some<>("a").toOptional();
        assertEquals(a.get(), ("a"));
    }

    @Test
    void ensureGetOrElsReturnsItemOnSome() {
        assertEquals(new Option.Some<>("a").getOrElse(() -> "b"), ("a"));
    }

    @Test
    void ensureLiftMapsItemCorrectly() {
        Option<Integer> a = some("1").lift(Integer::parseInt);
        assertEquals(a.isSome(), (true));
        assertEquals(a.get(), (1));
    }

    @Test
    void ensureFmapMapsItemCorrectly() {

        Option<Integer> a = some("1").fmap(Integer::parseInt);
        assertEquals(a.isSome(), (true));
        assertEquals(a.get(), (1));
    }

    @Test
    void ensureSomeReturnsSome() {
        assertTrue(some("a").isSome());
    }

    @Test
    void ensureNoneReturnsNone() {
        assertTrue(none().isNone());
    }

    @Test
    void ensureNoneGetThrowsNoSuchElementException() {
        assertThrows(NoSuchElementException.class, () -> none().get());
    }

    @Test
    void ensureNoneIsNone() {
        assertTrue(none().isNone());
    }

    @Test
    void ensureToOptionalReturnsNone() {
        Optional<Integer> a = Option.<Integer>none().toOptional();
        assertEquals(a.isPresent(), (false));
    }

    @Test
    void ensureGetOrElseNullReturnsNull() {
        assertNull(none().getOrElse(null), "must be null");
    }

    @Test
    void ensureGetOrElseWithSupplierReturnsSupplierValue() {
        Object a = "a";
        assertEquals(none().getOrElse(() -> a), (a));
    }

    @Test
    void ensureNoneIsLiftedToNone() {
        assertEquals(none().lift(t -> "a").isNone(), (true));
    }

    @Test
    void ensureNoneIsFMappedToNone() {
        assertEquals(none().fmap(t -> "a").isNone(), (true));
    }

    @Test
    void ensureNoneHasZeroSize() {
        assertEquals(none().size(), (0));
    }

    @Test
    void ensureNoneIsEmpty() {
        assertEquals(none().isEmpty(), (true));
    }

    @Test
    void ensureNoneContainsNothing() {
        assertEquals(none().contains("a"), (false));
    }

    @Test
    void ensureIteratorHasNoNExt() {
        assertEquals(none().iterator().hasNext(), (false));
    }

    @Test
    void ensureNoneIteratorNextThrowsException() {
        assertThrows(NoSuchElementException.class, () -> none().iterator().next());
    }

    @Test
    void ensureToArrayReturnsArray() {
        String[] a = {"h"};
        String[] strings = none().toArray(a);
        assertEquals(strings, (a));
    }

    @Test
    void ensureToArrayReturnsEmptyArray() {
        assertEquals(none().toArray().length, (0));
    }

    @Test
    void ensureSetOperationsAllReturnFalse() {
        assertFalse(none().add("a"));
        assertFalse(none().remove("a"));
        assertFalse(none().containsAll(Arrays.asList("a")));
        assertFalse(none().addAll(Arrays.asList("a")));
        assertFalse(none().removeAll(Arrays.asList("a")));
        assertFalse(none().retainAll(Arrays.asList("a")));
    }

    @Test
    void ensureClearOnNoneDoesNothing() {
        assertThrows(UnsupportedOperationException.class, () -> none().clear());
    }

    @Test
    void ensureOfOnNullReturnsNull() {
        assertTrue(Option.of(null).isNone());
    }

    @Test
    void ensureOptionOfNonNullReturnsSome() {
        assertEquals(Option.of("a").isSome(), (true));
    }

    @Test
    void ensureSomeReferentialEqualityWorks() {
        Option<String> a = some("a");
        assertTrue(a.equals(a));
    }

    @Test
    void ensureStructuralEqualityOnSomeHolds() {
        assertEquals(some("a"), (some("a")));
    }

    @Test
    void ensureSomeIsNotEqualIfContentsAreNotEqual() {
        assertNotEquals(some("a"), some("b"));
    }

    @Test
    void ensureContainsAllWorksOnSome() {
        some("a").addAll(Arrays.asList("a"));
    }

    @Test
    void ensureSomeIteratorThrowsNoSuchElementExceptionWhenCalledTooManyTimes() {
        Iterator<String> a = some("a").iterator();
        a.next();
        assertThrows(
                NoSuchElementException.class,
                () -> {
                    a.next();
                },
                "must throw an exception");
    }

    @Test
    void ensureOptionTestWorks() {
        new OptionTest();
    }

    @Test
    void ensureSomeIsNotEqualToNull() {
        assertFalse(some("a").equals(null));
    }

    @Test
    void ensureSomeIsNotEqualToObject() {
        assertFalse(some("a").equals(new Object()));
    }

    @Test
    void ensureSomeCanBeUsedAsAKeyForASet() {
        final Set<Object> os = new HashSet<>();
        os.add(some("cool"));
        assertTrue(os.contains(some("cool")));
    }

    @Test
    void ensureNoneIsEqualToNone() {
        assertEquals(none(), (none()));
    }

    @Test
    void ensureNoneCanBeUsedAsKeyForSet() {
        final Set<Object> o = new HashSet<>();
        o.add(none());
        assertTrue(o.contains(none()));
    }

    @Test
    void ensureNoneIsNotSome() {
        assertFalse(none().isSome());
    }

    @Test
    void ensureFlatMapMakesSense() {
        List<String> c =
                Option.of("a").stream()
                        .flatMap(a -> Option.bind("b", "c"))
                        .collect(Collectors.toList());
        assertEquals(c.size(), (2));
        assertEquals(c.contains("b"), (true));
    }
}
