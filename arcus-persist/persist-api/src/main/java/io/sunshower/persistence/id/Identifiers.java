package io.sunshower.persistence.id;



import java.time.Clock;

/**
 * Created by haswell on 7/18/17.
 */
public class Identifiers {

    public static Sequence<Identifier> randomSequence() {
        return new UUIDSequence();
    }

    public static Sequence<Identifier> newSequence() {
        return newSequence(true);
    }

    public static Sequence<Identifier> newSequence(boolean backpressure) {
        return newSequence(Clock.systemUTC(), backpressure);
    }

    public static Sequence<Identifier> newSequence(
            Clock clock,
            boolean applyBackpressure
    ) {
        return new IDSequence(
                clock,
                Nodes.getIdentifiableNodeHardwareAddress(),
                Nodes.getIdentifiableNetworkAddress(),
                applyBackpressure
        );
    }


    /**
     * Used to expose an ID's byte value without copying.  Do not use unless you really
     * know what you're doing
     * @param id
     * @return
     */
    public static byte[] getBytes(Identifier id) {
        return id.id;
    }
}
