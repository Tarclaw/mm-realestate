package com.realestate.mapper;

import com.realestate.dto.AddressDTO;
import com.realestate.model.buildings.Address;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressMapper INSTANCE = Mappers.getMapper(AddressMapper.class);

    AddressDTO addressToAddressDTO(Address address);

    Address addressDTOtoAddress(AddressDTO addressDTO);

}
