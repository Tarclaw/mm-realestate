package com.realestate.mapper;

import com.realestate.dto.GarageDTO;
import com.realestate.model.buildings.Garage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = AddressMapper.class)
public interface GarageMapper {

    GarageMapper INSTANCE = Mappers.getMapper(GarageMapper.class);

    @Mapping(source = "address.id" , target = "addressDTO.id")
    @Mapping(source = "address.postcode" , target = "addressDTO.postcode")
    @Mapping(source = "address.facilityNumber" , target = "addressDTO.facilityNumber")
    @Mapping(source = "address.city" , target = "addressDTO.city")
    @Mapping(source = "address.district" , target = "addressDTO.district")
    @Mapping(source = "address.street" , target = "addressDTO.street")
    GarageDTO garageToGarageDTO(Garage garage);

    @Mapping(source = "addressDTO.id" , target = "address.id")
    @Mapping(source = "addressDTO.postcode" , target = "address.postcode")
    @Mapping(source = "addressDTO.facilityNumber" , target = "address.facilityNumber")
    @Mapping(source = "addressDTO.city" , target = "address.city")
    @Mapping(source = "addressDTO.district" , target = "address.district")
    @Mapping(source = "addressDTO.street" , target = "address.street")
    Garage garageDTOtoGarage(GarageDTO garageDTO);

}
