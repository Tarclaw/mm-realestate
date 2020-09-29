package com.realestate.mapper;

import com.realestate.dto.PersonDTO;
import com.realestate.model.people.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = ContactMapper.class)
public interface PersonMapper {

    PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);

    @Mapping(source = "contact.email", target = "contactDTO.email")
    @Mapping(source = "contact.skype", target = "contactDTO.skype")
    @Mapping(source = "contact.mobileNumber", target = "contactDTO.mobileNumber")
    PersonDTO personToPersonDTO(Person person);

    @Mapping(source = "contactDTO.email", target = "contact.email")
    @Mapping(source = "contactDTO.skype", target = "contact.skype")
    @Mapping(source = "contactDTO.mobileNumber", target = "contact.mobileNumber")
    Person personDTOtoPerson(PersonDTO personDTO);

}
