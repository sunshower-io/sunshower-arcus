package io.sunshower.persistence.id;

import io.sunshower.common.Identifier;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by haswell on 7/20/17.
 */
public class UUIDSequence implements Sequence<Identifier> {

    @Override
    public Identifier next() {
        final UUID id = UUID.randomUUID();
        final ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(id.getMostSignificantBits());
        buffer.putLong(id.getLeastSignificantBits());
        return Identifier.valueOf(buffer.array());
    }
}
