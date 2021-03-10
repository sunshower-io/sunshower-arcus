package io.sunshower.persistence.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AddressValidatorTest {

  @Test
  void testDNS() {
    assertEquals(new AddressValidator() {}.check("www.google.com"), NetworkAddress.Type.DNS);
  }

  @Test
  void testIPV4Addr() {
    assertEquals(new AddressValidator() {}.check("8.8.8.8"), NetworkAddress.Type.IPV4);
  }

  @Test
  void testIPV6Addr() {
    assertEquals(new AddressValidator() {}.check("fff.fff.fff"), NetworkAddress.Type.IPV6);
    assertEquals(new AddressValidator() {}.check("::0"), NetworkAddress.Type.IPV6);
  }
}
