package io.sunshower.lang.common.hash.ints;

import io.sunshower.lang.common.hash.HashFunction;
import io.sunshower.lang.common.hash.integers.Murmur3;
import io.sunshower.lang.common.hash.integers.StringHash;
import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

/**
 * Created by haswell on 4/4/16.
 */
public class Murmur3Test {

    @Test
    public void ensureHashingNonCollidingDifferentStringsProducesDifferentValues() {
        final String fst = "Hello";
        final String snd = "World";
        assertThat(HashFunction.murmur3(3).apply(fst), is(not(snd)));
    }

    @Test
    public void testPerformance() {
        for(int k = 0; k < 5; ++k) {
            final Murmur3 murmur3 = (Murmur3) HashFunction.murmur3(1341234);
            String testString = generate(100000);
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < 10000; ++i) {
                int j = murmur3.apply(testString);
            }
            long t2 = System.currentTimeMillis();

            System.out.println("Average time millis: " + (t2 - t1));

            char[] ch = testString.toCharArray();
            t1 = System.currentTimeMillis();
            for (int i = 0; i < 10000; ++i) {
                hashCode(ch);
            }
            t2 = System.currentTimeMillis();

            System.out.println("Average time millis: " + (t2 - t1));
        }

    }

    int hashCode(char[] value) {
        int h = 0;
        if (h == 0 && value.length > 0) {
            char val[] = value;
            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
        }
        return h;
    }


    private String generate(int length) {
        final char[] chs = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOP".toCharArray();
        final Random rand = new Random();

        StringBuilder b = new StringBuilder(length);
        for(int i = 0; i < length; ++i) {
            b.append(chs[rand.nextInt(i == 0 ? 1 : i) % chs.length]);
        }
        return b.toString();
    }

}