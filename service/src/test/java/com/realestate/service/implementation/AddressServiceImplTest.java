package com.realestate.service.implementation;

import com.realestate.dto.AddressDTO;
import com.realestate.exceptions.NotFoundException;
import com.realestate.mapper.AddressMapper;
import com.realestate.model.buildings.Address;
import com.realestate.repositories.AddressRepository;
import com.realestate.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AddressServiceImplTest {

    private AddressService service;

    @Mock
    private AddressRepository repository;

    @Mock
    private AddressMapper addressMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new AddressServiceImpl(repository, addressMapper);
    }

    @Test
    void getById() {
        //given
        Optional<Address> sourceAddress = Optional.of(new Address());

        when(repository.findById(1L)).thenReturn(sourceAddress);

        //when
        Address address = service.getById(1L);

        //then
        assertNotNull(address);
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getByIdWhenAddressDoesNotExist() {
        assertThrows(NotFoundException.class, () -> service.getById(anyLong()));
    }

    @Test
    void getAddresses() {
        //given
        when(service.getAddresses()).thenReturn(Collections.singletonList(new Address()));

        //when
        List<Address> addresses = service.getAddresses();

        //then
        assertEquals(1, addresses.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getAddressesDTO() {
        //todo
    }

    @Test
    void findDTObyId() {
        //given
        Optional<Address> sourceAddress = Optional.of(new Address());
        when(repository.findById(anyLong())).thenReturn(sourceAddress);

        AddressDTO addressDTO = new AddressDTO();
        when(addressMapper.addressToAddressDTO(any())).thenReturn(addressDTO);

        //when
        AddressDTO command = service.findDTObyId(1L);

        //then
        assertNotNull(command);
        verify(repository, times(1)).findById(anyLong());
        verify(addressMapper, times(1)).addressToAddressDTO(any());
    }

    @Test
    void saveAddressDTO() {
    }

    @Test
    void patchAddress() {
        //todo
    }

    @Test
    void deleteById() {
        service.deleteById(anyLong());
        verify(repository, times(1)).deleteById(anyLong());
    }
}