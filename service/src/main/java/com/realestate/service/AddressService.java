package com.realestate.service;

import com.realestate.dto.AddressDTO;
import com.realestate.model.buildings.Address;

import java.util.List;

public interface AddressService {

    Address getById(Long id);

    List<Address> getAddresses();

    List<AddressDTO> getAddressesDTO();

    AddressDTO findDTObyId(Long id);

    AddressDTO saveAddressDTO(AddressDTO addressDTO);

    AddressDTO patchAddress(AddressDTO addressDTO, Long id);

    void deleteById(Long id);

}
