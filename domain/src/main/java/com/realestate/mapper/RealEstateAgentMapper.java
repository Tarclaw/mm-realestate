package com.realestate.mapper;

import com.realestate.dto.RealEstateAgentDTO;
import com.realestate.model.people.RealEstateAgent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        uses = ContactMapper.class)
public interface RealEstateAgentMapper {

    RealEstateAgentMapper INSTANCE = Mappers.getMapper(RealEstateAgentMapper.class);

    @Mapping(source = "contact.email", target = "contactDTO.email")
    @Mapping(source = "contact.skype", target = "contactDTO.skype")
    @Mapping(source = "contact.mobileNumber", target = "contactDTO.mobileNumber")
    RealEstateAgentDTO realEstateAgentToRealEstateAgentDTO(RealEstateAgent realEstateAgent);

    @Mapping(source = "contactDTO.email", target = "contact.email")
    @Mapping(source = "contactDTO.skype", target = "contact.skype")
    @Mapping(source = "contactDTO.mobileNumber", target = "contact.mobileNumber")
    RealEstateAgent realEstateAgentDTOtoRealEstateAgent(RealEstateAgentDTO realEstateAgentDTO);

}

