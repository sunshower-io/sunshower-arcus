package io.sunshower.persistence.id;

import io.sunshower.lang.common.hash.Hashes;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
public class Nodes {

    private static final Object lock = new Object();

    private static volatile InetAddress localAddress;

    private static final Map<InetAddress, Long> addressCache;

    private static final AtomicReference<UUID> fallbackNodeId;

    static {
        addressCache = new HashMap<>();
        fallbackNodeId = new AtomicReference<>(UUID.randomUUID());
    }

    public static InetAddress localAddress() {
        if (localAddress == null) {
            synchronized (lock) {
                if (localAddress == null) {
                    try {
                        localAddress = InetAddress.getLocalHost();
                    } catch (UnknownHostException ex) {
                        throw new IllegalStateException(ex);
                    }
                }
            }
        }
        return localAddress;
    }

    public static Set<NetworkInterface> getLocalInterfaces() {
        try {
            Enumeration<NetworkInterface> networkInterfaces =
                    NetworkInterface.getNetworkInterfaces();
            final Set<NetworkInterface> results = new HashSet<>();

            while (networkInterfaces.hasMoreElements()) {
                results.add(networkInterfaces.nextElement());
            }
            return results;
        } catch (SocketException e) {
            throw new NoSuchElementException("Failed to find local network interfaces");
        }
    }

    public static NetworkInterface getIdentifiableInterface() {
        return getLocalInterfaces().stream()
                .filter(
                        t -> {
                            try {
                                return t.isUp() && !t.isLoopback();
                            } catch (SocketException e) {
                                return false;
                            }
                        })
                .findAny()
                .get();
    }

    public static byte[] getIdentifiableNodeHardwareAddress() {
        return getIdentifiableNodeHardwareAddress(false);
    }

    public static byte[] getIdentifiableNodeHardwareAddress(boolean noFail) {
        try {
            return getIdentifiableInterface().getHardwareAddress();
        } catch (SocketException e) {
            if (noFail) {
                final ByteBuffer buffer = ByteBuffer.allocate(16);
                UUID uuid = fallbackNodeId.get();
                long lsb = uuid.getLeastSignificantBits();
                long msb = uuid.getMostSignificantBits();
                buffer.putLong(msb);
                buffer.putLong(lsb);
                return buffer.array();
            }
            throw new IllegalStateException(e);
        }
    }

    public static InetAddress getIdentifiableNetworkAddress() {
        return getIdentifiableInterface().getInetAddresses().nextElement();
    }

    public static long localIdentity() {
        return localIdentity(getIdentifiableInterface().getInetAddresses().nextElement());
    }

    private static final long clock() {
        return System.currentTimeMillis();
    }

    public static long localIdentity(InetAddress address) {
        long lsb = 0;
        long clock = clock();
        lsb |= (clock & 0x3f00000000000000L) >>> 56;
        lsb |= 0x0000000000000080;
        lsb |= (clock & 0x00ff000000000000L) >>> 48;
        lsb |= localIdentityForAddress(address);
        return lsb;
    }

    private static long localIdentityForAddress(InetAddress address) {
        if (addressCache.containsKey(address)) {
            return addressCache.get(address);
        }

        byte[] hash =
                Hashes.hashCode(Hashes.Algorithm.MD5)
                        .digest(
                                ByteBuffer.wrap(
                                        address.toString().getBytes(StandardCharsets.UTF_8)));
        long node = 0;
        for (int i = 0; i < Math.min(6, hash.length); i++) {
            node |= (0x00000000000000ff & (long) hash[i]) << (5 - i) * 8;
        }
        addressCache.put(address, node);
        return node;
    }
}
