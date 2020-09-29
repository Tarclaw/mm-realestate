package com.realestate.service.implementation;

import com.realestate.dto.AddressDTO;
import com.realestate.exceptions.NotFoundException;
import com.realestate.mapper.AddressMapper;
import com.realestate.model.buildings.Address;
import com.realestate.repositories.AddressRepository;
import com.realestate.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final AddressRepository repository;
    private final AddressMapper mapper;

    public AddressServiceImpl(AddressRepository repository, AddressMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
        LOGGER.info("New instance of AddressServiceImpl created");
    }

    @Override
    public Address getById(final Long id) {
        LOGGER.trace("Enter and execute 'AddressServiceImpl.getById(final Long id)' method");
        return repository.findById(id)
                .orElseThrow(
                        () -> {
                            LOGGER.warn("We don't have address with id= " + id);
                            return new NotFoundException("Please choose different Address");
                        }
                );
    }

    @Override
    public List<Address> getAddresses() {
        LOGGER.trace("Enter in 'AddressServiceImpl.getAddresses()' method");

        List<Address> addresses = new ArrayList<>();
        repository.findAll().iterator().forEachRemaining(addresses :: add);

        LOGGER.debug("Find all Addresses from DB and add them to ArrayList");
        LOGGER.trace("'AddressServiceImpl.getAddresses()' executed successfully.");
        return addresses;
    }

    @Override
    public List<AddressDTO> getAddressesDTO() {
        return getAddresses()
                .stream()
                .map(mapper::addressToAddressDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AddressDTO findDTObyId(final Long id) {
        LOGGER.trace("Enter and execute 'AddressServiceImpl.findCommandById(final Long id)' method");
        return mapper.addressToAddressDTO(getById(id));
    }

    @Override
    @Transactional
    public AddressDTO saveAddressDTO(final AddressDTO addressDTO) {
        LOGGER.trace("Enter in 'AddressServiceImpl.saveAddressCommand(final AddressCommand command)' method");

        Address address = repository.save(mapper.addressDTOtoAddress(addressDTO));

        LOGGER.debug("We saved address with id=" + address.getId());
        LOGGER.trace("'AddressServiceImpl.saveAddressCommand(final AddressCommand command)' executed successfully.");
        return mapper.addressToAddressDTO(address);
    }

    @Override
    public AddressDTO patchAddress(AddressDTO addressDTO, Long id) {
        return repository.findById(id).map(address -> {
            if(addressDTO.getCity() != null) {
                address.setCity(addressDTO.getCity());
            }
            if(addressDTO.getDistrict() != null) {
                address.setDistrict(addressDTO.getDistrict());
            }
            if(addressDTO.getFacilityNumber() != null) {
                address.setFacilityNumber(addressDTO.getFacilityNumber());
            }
            if(addressDTO.getPostcode() != null) {
                address.setPostcode(addressDTO.getPostcode());
            }
            if(addressDTO.getStreet() != null) {
                address.setStreet(addressDTO.getStreet());
            }
            return mapper.addressToAddressDTO(repository.save(address));
        }).orElseThrow(RuntimeException::new);
    }

    @Override
    public void deleteById(final Long id) {
        LOGGER.trace("Enter in 'AddressServiceImpl.deleteById(final Long id)' method");

        repository.deleteById(id);

        LOGGER.debug("Delete Address with id= " + id);
        LOGGER.trace("'AddressServiceImpl.deleteById(final Long id)' executed successfully.");
    }
}
