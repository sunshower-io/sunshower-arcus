package io.sunshower.persistence.core;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public interface AddressValidator {

  /**
   * This method is not 100% correct. I just don't want to add a dependency on Guava or something
   * else. Use it carefully
   *
   * @param value
   * @return
   */
  default NetworkAddress.Type check(String value) {
    for (int i = 0; i < value.length(); i++) {
      char ch = value.charAt(i);
      if (!(ch == '.' || ch == ':' || Character.digit(ch, 16) != -1)) {
        return NetworkAddress.Type.DNS;
      }
    }
    try {
      InetAddress addr = InetAddress.getByName(value);
      return addr instanceof Inet4Address ? NetworkAddress.Type.IPV4 : NetworkAddress.Type.IPV6;
    } catch (UnknownHostException e) {
      return NetworkAddress.Type.IPV6;
    }
  }
}
