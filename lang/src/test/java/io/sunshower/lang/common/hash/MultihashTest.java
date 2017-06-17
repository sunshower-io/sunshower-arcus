package io.sunshower.lang.common.hash;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by haswell on 5/25/16.
 */
public class MultihashTest {


    @Test
    public void ensureDecodingEncodedMultiHashProducesExpectedResults() {
        final byte[] data = {
                (byte) Multihash.Type.SHA1.value,
                2,
                0,
                1
        };

        Multihash multicode = Multihash.decode(data);
        assertThat(multicode.getType(), is(Multihash.Type.SHA1));
        assertThat(multicode.data[0], is((byte) 0));
        assertThat(multicode.data[1], is((byte) 1));
        
    }
    
    @Test
    public void ensureDecodingProducesExpectedResults() {
        final byte[] data = {
                (byte) Multihash.Type.SHA1.value,
                2,
                0,
                1
        };

        Multihash multicode = Multihash.decode(data);
        byte[] d = Multihash.encode(multicode);
        assertTrue(Arrays.equals(data, d));
    }

    @Test
    public void ensureEncodingDataForAllValuesProducesExpectedResults() {
        for (Multihash.Type type : Multihash.Type.values()) {
            byte[] data = randomBytes(100);
            data[0] = (byte) type.value;
            data[1] = (byte) (data.length - 2);
            Multihash fst = Multihash.decode(data);
            byte[] snd = Multihash.encode(fst);
            assertThat(fst, is(Multihash.decode(snd)));
        }
    }

    private byte[] randomBytes(int length) {
        final Random random = new Random();
        final byte[] data = new byte[length];
        random.nextBytes(data);
        return data;
    }

}