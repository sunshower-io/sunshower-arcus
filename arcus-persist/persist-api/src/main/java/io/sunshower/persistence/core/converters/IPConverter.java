package io.sunshower.persistence.core.converters;

import io.sunshower.persistence.core.NetworkAddress;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class IPConverter implements AttributeConverter<NetworkAddress, String> {

  @Override
  public String convertToDatabaseColumn(NetworkAddress machineAddress) {
    if (machineAddress == null) {
      return null;
    }
    return machineAddress.getValue();
  }

  @Override
  public NetworkAddress convertToEntityAttribute(String chars) {
    if (chars == null) {
      return null;
    }
    return new NetworkAddress(chars);
  }
}
