package com.realestate.mapper;

import com.realestate.dto.ClientDTO;
import com.realestate.model.people.Client;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = ContactMapper.class)
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    @Mapping(source = "contact.email", target = "contactDTO.email")
    @Mapping(source = "contact.skype", target = "contactDTO.skype")
    @Mapping(source = "contact.mobileNumber", target = "contactDTO.mobileNumber")
    ClientDTO clientToClientDTO(Client client);

    @Mapping(source = "contactDTO.email", target = "contact.email")
    @Mapping(source = "contactDTO.skype", target = "contact.skype")
    @Mapping(source = "contactDTO.mobileNumber", target = "contact.mobileNumber")
    Client clientDTOtoClient(ClientDTO clientDTO);

}
