package io.sunshower.lang.primitives;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.BitSet;

import static io.sunshower.lang.primitives.Bytes.fromByteArray;
import static io.sunshower.lang.primitives.Bytes.toByteArray;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class BytesTest {


    @Test
    public void ensureToByteArrayProducesExpectedValues() {
        byte[] bytes = toByteArray(45);
        assertThat(fromByteArray(bytes), is(45));
    }



    @Test(expected = UnsupportedOperationException.class)
    public void ensureBytesCannotBeConstructed() throws Throwable {
        final Constructor ctor = Bytes.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        try {
            ctor.newInstance();
        } catch(InvocationTargetException ex) {
            throw ex.getTargetException();
        }
    }

    @Test
    public void ensureSettingViaBooleanWorks() {

        final byte[] bs = {0b1111111, 0b0, 0b1010101};
        assertThat(Bytes.get(bs, 0), is(1));
        Bytes.set(bs, 0, false);
        assertThat(Bytes.get(bs, 0), is(0));
        assertThat(Bytes.get(bs, 16), is(1));
        assertThat(Bytes.get(bs, 18), is(1));
        assertThat(Bytes.get(bs, 17), is(0));
        Bytes.set(bs, 16, false);
        assertThat(Bytes.get(bs, 16), is(0));
        assertThat(Bytes.get(bs, 17), is(0));
        assertThat(Bytes.get(bs, 18), is(1));
    }


    @Test
    public void ensureToBitStringWorks() {
        final byte[] bs = {0b1111111, 0b0, 0b1010101};
        System.out.println(Bytes.toBitString(bs));
        String expected = "111111100000000010101010";
        assertThat(expected, is(Bytes.toBitString(bs)));
    }

    @Test
    public void ensureSetSetsBytesCorrectly() {
        final byte[] bs = {0b1111111, 0b0, 0b1010101};
        assertThat(Bytes.get(bs, 0), is(1));
        Bytes.set(bs, 0, 0);
        assertThat(Bytes.get(bs, 0), is(0));
        assertThat(Bytes.get(bs, 16), is(1));
        assertThat(Bytes.get(bs, 18), is(1));
        assertThat(Bytes.get(bs, 17), is(0));
        Bytes.set(bs, 16, 0);
        assertThat(Bytes.get(bs, 16), is(0));
        assertThat(Bytes.get(bs, 17), is(0));
        assertThat(Bytes.get(bs, 18), is(1));
    }


    @Test
    public void ensureSettingFirstBitResultsInFirstBitBeingSet() {
        byte b = 0b0;
        int expected = 0b1;
        int c = Bytes.set(b, 0);
        assertThat(c, is(expected));
    }

    @Test
    public void ensureByteUnsetClearsByteAtFirstIndex() {
        byte b = 0b1;
        int expected = 0b0;
        assertThat(Bytes.clear(b, 0), is(expected));
    }


    @Test
    public void ensureSettingBitAtSecondPositionResultsInSecondBitBeingSet() {
        byte b = 0b0;
        int expected = 0b10;
        assertThat(Bytes.set(b, 1), is(expected));
    }

    @Test
    public void ensureClearingBitAtSecondPositionResultsInSecondBitBeingCleared() {
        byte b = 0b11;
        int e = 0b1;
        int f = 0b10;
        assertThat(Bytes.clear(b, 1), is(e));
        assertThat(Bytes.clear(b, 0), is(f));
    }


    @Test
    public void ensureGettingBitAtFirstIndexInByteArrayProducesExpectedValue() {
        byte[] b = {0b1, 0b0};
        assertThat(Bytes.get(b, 0), is(1));
    }


    @Test
    public void ensureGettingBitAtSecondIndexInArrayProducesExpectedValue() {
        byte[] b = {0b10, 0b0};
        assertThat(Bytes.get(b, 1), is(1));
        assertThat(Bytes.get(b, 0), is(0));
    }

    @Test
    public void ensureClearingBitsAtLocationsProducesExpectedResults() {
        byte b = 0b1111111;
        byte expected = 0b1010101;
        for(int i = 0; i < 7; ++i) {
            if(i % 2 == 1) {
                b = (byte) Bytes.clear(b, i);
            }
        }
        assertThat(b, is(expected));
    }


    @Test
    public void ensureGettingBitAtIndexWorks() {
        final byte b = 0b1010101;
        for(int i = 0; i < 8; ++i) {
            assertThat(Bytes.get(b, i), is((i + 1) % 2));
        }
    }

    @Test
    public void ensureSettingBitsProducesExpectedResults() {
        byte b = 0;
        for(int i = 0; i < 8; ++i) {
            if(i % 2 == 0) {
                 b = (byte) Bytes.set(b, i);
            }
        }
        for(int i = 0; i < 8; ++i) {
            if(i % 2 == 0) {
                assertThat(Bytes.get(b, i), is(1));
            } else
                assertThat(Bytes.get(b, i), is(0));
        }
    }


    @Test
    public void ensureGettingBitsInByteArrayAtWeirdLocationProducesExpectedResults() {
        byte[] bs = {0b101, 0b10101, 0b111101};
        assertThat(Bytes.get(bs, 2), is(1));
        assertThat(Bytes.get(bs, 1), is(0));
        assertThat(Bytes.get(bs, 8), is(1));
        assertThat(Bytes.get(bs, 1), is(0));
        assertThat(Bytes.get(bs, 0), is(1));
        assertThat(Bytes.get(bs, 10), is(1));
        assertThat(Bytes.get(bs,11), is(0));
        assertThat(Bytes.get(bs, 16), is(1));
        BitSet b = BitSet.valueOf(bs);
        assertThat(b.get(17), is(false));
        assertThat(Bytes.get(bs, 17), is(0));
    }

}