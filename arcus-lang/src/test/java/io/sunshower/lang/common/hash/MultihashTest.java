package io.sunshower.lang.common.hash;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Random;
import org.junit.jupiter.api.Test;

class MultihashTest {

    @Test
    void ensureDecodingEncodedMultiHashProducesExpectedResults() {
        final byte[] data = {(byte) Multihash.Type.SHA1.value, 2, 0, 1};

        Multihash multicode = Multihash.decode(data);
        assertEquals(multicode.getType(), (Multihash.Type.SHA1));
        assertEquals(multicode.data[0], ((byte) 0));
        assertEquals(multicode.data[1], ((byte) 1));
    }

    @Test
    void ensureDecodingProducesExpectedResults() {
        final byte[] data = {(byte) Multihash.Type.SHA1.value, 2, 0, 1};

        Multihash multicode = Multihash.decode(data);
        byte[] d = Multihash.encode(multicode);
        assertTrue(Arrays.equals(data, d));
    }

    @Test
    void ensureEncodingDataForAllValuesProducesExpectedResults() {
        for (Multihash.Type type : Multihash.Type.values()) {
            byte[] data = randomBytes(100);
            data[0] = (byte) type.value;
            data[1] = (byte) (data.length - 2);
            Multihash fst = Multihash.decode(data);
            byte[] snd = Multihash.encode(fst);
            assertEquals(fst, (Multihash.decode(snd)));
        }
    }

    private byte[] randomBytes(int length) {
        final Random random = new Random();
        final byte[] data = new byte[length];
        random.nextBytes(data);
        return data;
    }
}
