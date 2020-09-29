package com.realestate.mapper;

import com.realestate.dto.ContactDTO;
import com.realestate.model.people.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ContactMapper {

    ContactMapper INSTANCE = Mappers.getMapper(ContactMapper.class);

    ContactDTO contactToContactDTO(Contact contact);

    Contact contactDTOtoContact(ContactDTO contactDTO);

}
