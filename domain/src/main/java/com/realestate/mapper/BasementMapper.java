package com.realestate.mapper;

import com.realestate.dto.BasementDTO;
import com.realestate.model.buildings.Basement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface BasementMapper {

    BasementMapper INSTANCE = Mappers.getMapper(BasementMapper.class);

    @Mapping(source = "address.id" , target = "addressDTO.id")
    @Mapping(source = "address.postcode" , target = "addressDTO.postcode")
    @Mapping(source = "address.facilityNumber" , target = "addressDTO.facilityNumber")
    @Mapping(source = "address.city" , target = "addressDTO.city")
    @Mapping(source = "address.district" , target = "addressDTO.district")
    @Mapping(source = "address.street" , target = "addressDTO.street")
    BasementDTO basementToBasementDTO(Basement basement);

    @Mapping(source = "addressDTO.id" , target = "address.id")
    @Mapping(source = "addressDTO.postcode" , target = "address.postcode")
    @Mapping(source = "addressDTO.facilityNumber" , target = "address.facilityNumber")
    @Mapping(source = "addressDTO.city" , target = "address.city")
    @Mapping(source = "addressDTO.district" , target = "address.district")
    @Mapping(source = "addressDTO.street" , target = "address.street")
    Basement basementDTOtoBasement(BasementDTO basementDTO);

}
