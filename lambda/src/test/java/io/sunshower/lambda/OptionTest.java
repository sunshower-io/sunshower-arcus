package io.sunshower.lambda;

import org.junit.Test;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.sunshower.lambda.Option.none;
import static io.sunshower.lambda.Option.some;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by haswell on 3/23/16.
 */
public class OptionTest {


    @Test
    public void ensureSomeOfAnItemIsSome() {
        assertThat(new Option.Some<>("hello").isSome(), is(true));
    }

    @Test
    public void ensureSomeIsNotNone() {
        assertThat(new Option.Some<>("hello").isNone(), is(false));
    }


    @Test
    public void ensureSizeOfSomeIs1() {
        assertThat(new Option.Some<>("l").size(), is(1));
    }

    @Test
    public void ensureSomeIsNotEmpty() {
        assertThat(new Option.Some<>("").isEmpty(), is(false));
    }

    @Test
    public void ensureSomeDoesNotContainAnItemThatIsNotItsItem() {
        assertThat(new Option.Some<>("1").contains("2"), is(false));
    }

    @Test
    public void ensureSomeContainsAnItemThatIsStructurallyItsItem() {
        assertThat(new Option.Some<>("1").contains("1"), is(true));
    }

    @Test
    public void ensureSomeContainsAnItemThatIsReferentiallyItsItem() {
        Object o = new Object();
        assertThat(new Option.Some<>(o).contains(o), is(true));
    }

    @Test
    public void ensureIteratorIteratesOnceOverSome() {

        int count = 0;
        Object contents = null;
        for(Object o : new Option.Some<>("hello")) {
            count++;
            contents = o;
        }
        assertThat(count, is(1));
        assertThat(contents, is("hello"));

    }

    @Test
    public void ensureIterationIsItempotent() {
        List<Object> results = new ArrayList<>();
        Object obj = new Object();
        Option<Object> o = new Option.Some<>(obj);
        for(int i = 0; i < 5; ++i) {
            for(Object a : o) {
                results.add(a);
            }
        }
        assertThat(results.size(), is(5));
    }

    @Test
    public void ensureToArrayProducesObject() {
        Option.Some<String> a = new Option.Some<>("a");
        Object[] o = a.toArray();
        assertThat(o.length, is(1));
        assertThat(o[0], is("a"));
    }

    @Test
    public void ensureToArrayOnEmptyArrayProducesCorrectArray() {
        Option.Some<String> a = new Option.Some<>("a");
        String[] ar = new String[0];
        String[] o = a.toArray(ar);
        assertThat(o.length, is(1));
        assertThat(o[0], is("a"));
        assertThat(ar, is(not(o)));
    }

    @Test
    public void ensureToArrayOnArrayOfSizeOneProducesSameArray() {

        Option.Some<String> a = new Option.Some<>("a");
        String[] ar = new String[1];
        String[] o = a.toArray(ar);
        assertThat(o.length, is(1));
        assertThat(o[0], is("a"));
        assertTrue(o == ar);
    }

    @Test
    public void ensureToArrayOnSomeOnArrayOfSizeGreaterThanOneProducesArrayWithElementAtPositionOneEqualToNull() {
        Option.Some<String> a = new Option.Some<>("a");
        String[] ar = new String[]{"1", "2"};
        String[] results = a.toArray(ar);
        assertThat(results[0], is("a"));
        assertThat(results[1], is(nullValue()));
        assertTrue(ar == results);
    }


    @Test
    public void ensureSomeCanAddStructurallyEquivalentObject() {
        assertTrue(new Option.Some<>("a").add("a"));
    }

    @Test
    public void ensureSomeCanAddReferentiallyEquivalentObject() {
        String o = "a";
        assertTrue(new Option.Some<>(o).add(o));
    }


    @Test
    public void ensureRemoveReturnsFalseForStructurallyDifferentObjects() {
        assertFalse(new Option.Some<>(new Object()).remove(new Object()));
    }

    @Test
    public void ensureRemoveReturnsFalseForStructurallyEquivalentObjects() {
        assertFalse(new Option.Some<>("a").remove("a"));
    }

    @Test
    public void ensureRemoveReturnsFalseForReferentiallyEquivalentObjects() {
        String a = "a";
        assertFalse(new Option.Some<>(a).remove(a));
    }

    @Test
    public void ensureSomeContainsAllOfASingletonCollectionOfAStructurallyEquivalentObject() {
        assertTrue(new Option.Some<>("a").containsAll(Collections.singleton("a")));
    }

    @Test
    public void ensureSomeContainsAllOfASingletonCollectionOfAReferentiallyEquivalentObject() {
        String a = "a";
        assertTrue(new Option.Some<>(a).containsAll(Collections.singleton(a)));
    }

    @Test
    public void ensureSomeContainsNoneOfAnEmptyCollection() {
        assertFalse(new Option.Some<>("a").containsAll(Collections.emptyList()));
    }

    @Test
    public void ensureSomeContainsNonOfANonEmptyCollectionWithMoreThanOneElement() {
        assertFalse(new Option.Some<>("a").containsAll(Arrays.asList("1", "a")));
    }

    @Test
    public void ensureRemoveAllReturnsFalse() {
        assertFalse(new Option.Some<>("a").removeAll(Arrays.asList("a")));
    }

    @Test
    public void ensureRetainAllReturnsFalseForSetWithMoreThanOneElement() {
        assertFalse(new Option.Some<>("a").removeAll(Arrays.asList("a", "b")));
    }

    @Test
    public void ensureRetainAllReturnsTrueForSingleElementContainingItsItem() {
        assertTrue(new Option.Some<>("a").retainAll(Arrays.asList("a")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void ensureClearThrowsUnsupportedOperationException() {
        new Option.Some<>("a").clear();
    }

    @Test
    public void ensureToOptionalProducesCorrectOptional() {
        Optional<String> a = new Option.Some<>("a").toOptional();
        assertThat(a.get(), is("a"));
    }

    @Test
    public void ensureGetOrElsReturnsItemOnSome() {
        assertThat(new Option.Some<>("a").getOrElse(() -> "b"), is("a"));
    }

    @Test
    public void ensureLiftMapsItemCorrectly() {
        Option<Integer> a = some("1").lift(Integer::parseInt);
        assertThat(a.isSome(), is(true));
        assertThat(a.get(), is(1));
    }

    @Test
    public void ensureFmapMapsItemCorrectly() {

        Option<Integer> a = some("1").fmap(Integer::parseInt);
        assertThat(a.isSome(), is(true));
        assertThat(a.get(), is(1));
    }

    @Test
    public void ensureSomeReturnsSome() {
        assertTrue(some("a").isSome());
    }

    @Test
    public void ensureNoneReturnsNone() {
        assertTrue(none().isNone());
    }

    @Test(expected = NoSuchElementException.class)
    public void ensureNoneGetThrowsNoSuchElementException() {
        none().get();
    }

    @Test
    public void ensureNoneIsNone() {
        assertTrue(none().isNone());
    }

    @Test
    public void ensureToOptionalReturnsNone() {
        Optional<Integer> a = Option.<Integer>none().toOptional();
        assertThat(a.isPresent(), is(false));
    }

    @Test
    public void ensureGetOrElseNullReturnsNull() {
        assertThat(none().getOrElse(null), is(nullValue()));
    }

    @Test
    public void ensureGetOrElseWithSupplierReturnsSupplierValue() {
        Object a = "a";
        assertThat(none().getOrElse(() -> a), is(a));
    }

    @Test
    public void ensureNoneIsLiftedToNone() {
        assertThat(none().lift(t -> "a").isNone(), is(true));
    }

    @Test
    public void ensureNoneIsFMappedToNone() {
        assertThat(none().fmap(t -> "a").isNone(), is(true));
    }

    @Test
    public void ensureNoneHasZeroSize() {
        assertThat(none().size(), is(0));
    }

    @Test
    public void ensureNoneIsEmpty() {
        assertThat(none().isEmpty(), is(true));
    }

    @Test
    public void ensureNoneContainsNothing() {
        assertThat(none().contains("a"), is(false));
    }

    @Test
    public void ensureIteratorHasNoNExt() {
        assertThat(none().iterator().hasNext(), is(false));
    }

    @Test(expected = NoSuchElementException.class)
    public void ensureNoneIteratorNextThrowsException() {
        none().iterator().next();
    }

    @Test
    public void ensureToArrayReturnsArray() {
        String[] a = {"h"};
        String[] strings = none().toArray(a);
        assertThat(strings, is(a));
    }

    @Test
    public void ensureToArrayReturnsEmptyArray() {
        assertThat(none().toArray().length, is(0));
    }

    @Test
    public void ensureSetOperationsAllReturnFalse() {
        assertFalse(none().add("a"));
        assertFalse(none().remove("a"));
        assertFalse(none().containsAll(Arrays.asList("a")));
        assertFalse(none().addAll(Arrays.asList("a")));
        assertFalse(none().removeAll(Arrays.asList("a")));
        assertFalse(none().retainAll(Arrays.asList("a")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void ensureNoneClearThrowsUnsupportedOperationException() {
        none().clear();
    }

    @Test
    public void ensureOfOnNullReturnsNull() {
        assertTrue(Option.of(null).isNone());
    }

    @Test
    public void ensureOptionOfNonNullReturnsSome() {
        assertThat(Option.of("a").isSome(), is(true));
    }

    @Test
    public void ensureSomeReferentialEqualityWorks() {
        Option<String> a = some("a");
        assertTrue(a.equals(a));
    }

    @Test
    public void ensureStructuralEqualityOnSomeHolds() {
        assertThat(some("a"), is(some("a")));
    }

    @Test
    public void ensureSomeIsNotEqualIfContentsAreNotEqual() {
        assertThat(some("a"), is(not(some("b"))));
    }

    @Test
    public void ensureContainsAllWorksOnSome() {
        some("a").addAll(Arrays.asList("a"));
    }

    @Test(expected = NoSuchElementException.class)
    public void ensureSomeIteratorThrowsNoSuchElementExceptionWhenCalledTooManyTimes() {
        Iterator<String> a = some("a").iterator();
        a.next();
        a.next();
    }

    @Test
    public void ensureOptionTestWorks() {
        new OptionTest();

    }

    @Test
    public void ensureSomeIsNotEqualToNull() {
        assertFalse(some("a").equals(null));
    }

    @Test
    public void ensureSomeIsNotEqualToObject() {
        assertFalse(some("a").equals(new Object()));
    }


    @Test
    public void ensureSomeCanBeUsedAsAKeyForASet() {
        final Set<Object> os = new HashSet<>();
        os.add(some("cool"));
        assertTrue(os.contains(some("cool")));
    }

    @Test
    public void ensureNoneIsEqualToNone() {
        assertThat(none(), is(none()));
    }

    @Test
    public void ensureNoneCanBeUsedAsKeyForSet() {
        final Set<Object> o = new HashSet<>();
        o.add(none());
        assertTrue(o.contains(none()));
    }

    @Test
    public void ensureNoneIsNotSome() {
        assertFalse(none().isSome());
    }

    @Test
    public void ensureFlatMapMakesSense() {
        List<String> c = Option.of("a").stream().flatMap(a ->
                Option.bind("b", "c")).collect(Collectors.toList());
        assertThat(c.size(), is(2));
        assertThat(c.contains("b"), is(true));

    }


}