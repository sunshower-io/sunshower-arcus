package io.sunshower.lang.primitives;

import static io.sunshower.lang.primitives.Bytes.fromByteArray;
import static io.sunshower.lang.primitives.Bytes.toByteArray;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.BitSet;
import org.junit.jupiter.api.Test;

class BytesTest {

    @Test
    void ensureToByteArrayProducesExpectedValues() {
        byte[] bytes = toByteArray(45);
        assertEquals(fromByteArray(bytes), (45));
    }

    @Test
    void ensureBytesCannotBeConstructed() throws Throwable {
        final Constructor ctor = Bytes.class.getDeclaredConstructor();
        ctor.setAccessible(true);
        assertThrows(
                UnsupportedOperationException.class,
                () -> {
                    try {
                        ctor.newInstance();
                    } catch (InvocationTargetException ex) {
                        throw ex.getTargetException();
                    }
                });
    }

    @Test
    void ensureSettingViaBooleanWorks() {

        final byte[] bs = {0b1111111, 0b0, 0b1010101};
        assertEquals(Bytes.get(bs, 0), (1));
        Bytes.set(bs, 0, false);
        assertEquals(Bytes.get(bs, 0), (0));
        assertEquals(Bytes.get(bs, 16), (1));
        assertEquals(Bytes.get(bs, 18), (1));
        assertEquals(Bytes.get(bs, 17), (0));
        Bytes.set(bs, 16, false);
        assertEquals(Bytes.get(bs, 16), (0));
        assertEquals(Bytes.get(bs, 17), (0));
        assertEquals(Bytes.get(bs, 18), (1));
    }

    @Test
    void ensureToBitStringWorks() {
        final byte[] bs = {0b1111111, 0b0, 0b1010101};
        System.out.println(Bytes.toBitString(bs));
        String expected = "111111100000000010101010";
        assertEquals(expected, (Bytes.toBitString(bs)));
    }

    @Test
    void ensureSetSetsBytesCorrectly() {
        final byte[] bs = {0b1111111, 0b0, 0b1010101};
        assertEquals(Bytes.get(bs, 0), (1));
        Bytes.set(bs, 0, 0);
        assertEquals(Bytes.get(bs, 0), (0));
        assertEquals(Bytes.get(bs, 16), (1));
        assertEquals(Bytes.get(bs, 18), (1));
        assertEquals(Bytes.get(bs, 17), (0));
        Bytes.set(bs, 16, 0);
        assertEquals(Bytes.get(bs, 16), (0));
        assertEquals(Bytes.get(bs, 17), (0));
        assertEquals(Bytes.get(bs, 18), (1));
    }

    @Test
    void ensureSettingFirstBitResultsInFirstBitBeingSet() {
        byte b = 0b0;
        int expected = 0b1;
        int c = Bytes.set(b, 0);
        assertEquals(c, (expected));
    }

    @Test
    void ensureByteUnsetClearsByteAtFirstIndex() {
        byte b = 0b1;
        int expected = 0b0;
        assertEquals(Bytes.clear(b, 0), (expected));
    }

    @Test
    void ensureSettingBitAtSecondPositionResultsInSecondBitBeingSet() {
        byte b = 0b0;
        int expected = 0b10;
        assertEquals(Bytes.set(b, 1), (expected));
    }

    @Test
    void ensureClearingBitAtSecondPositionResultsInSecondBitBeingCleared() {
        byte b = 0b11;
        int e = 0b1;
        int f = 0b10;
        assertEquals(Bytes.clear(b, 1), (e));
        assertEquals(Bytes.clear(b, 0), (f));
    }

    @Test
    void ensureGettingBitAtFirstIndexInByteArrayProducesExpectedValue() {
        byte[] b = {0b1, 0b0};
        assertEquals(Bytes.get(b, 0), (1));
    }

    @Test
    void ensureGettingBitAtSecondIndexInArrayProducesExpectedValue() {
        byte[] b = {0b10, 0b0};
        assertEquals(Bytes.get(b, 1), (1));
        assertEquals(Bytes.get(b, 0), (0));
    }

    @Test
    void ensureClearingBitsAtLocationsProducesExpectedResults() {
        byte b = 0b1111111;
        byte expected = 0b1010101;
        for (int i = 0; i < 7; ++i) {
            if (i % 2 == 1) {
                b = (byte) Bytes.clear(b, i);
            }
        }
        assertEquals(b, (expected));
    }

    @Test
    void ensureGettingBitAtIndexWorks() {
        final byte b = 0b1010101;
        for (int i = 0; i < 8; ++i) {
            assertEquals(Bytes.get(b, i), ((i + 1) % 2));
        }
    }

    @Test
    void ensureSettingBitsProducesExpectedResults() {
        byte b = 0;
        for (int i = 0; i < 8; ++i) {
            if (i % 2 == 0) {
                b = (byte) Bytes.set(b, i);
            }
        }
        for (int i = 0; i < 8; ++i) {
            if (i % 2 == 0) {
                assertEquals(Bytes.get(b, i), (1));
            } else assertEquals(Bytes.get(b, i), (0));
        }
    }

    @Test
    void ensureGettingBitsInByteArrayAtWeirdLocationProducesExpectedResults() {
        byte[] bs = {0b101, 0b10101, 0b111101};
        assertEquals(Bytes.get(bs, 2), (1));
        assertEquals(Bytes.get(bs, 1), (0));
        assertEquals(Bytes.get(bs, 8), (1));
        assertEquals(Bytes.get(bs, 1), (0));
        assertEquals(Bytes.get(bs, 0), (1));
        assertEquals(Bytes.get(bs, 10), (1));
        assertEquals(Bytes.get(bs, 11), (0));
        assertEquals(Bytes.get(bs, 16), (1));
        BitSet b = BitSet.valueOf(bs);
        assertEquals(b.get(17), (false));
        assertEquals(Bytes.get(bs, 17), (0));
    }
}
