package com.realestate.controllers.rest;

import com.realestate.dto.AddressDTO;
import com.realestate.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static com.realestate.controllers.rest.AbstractRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class AddressDTOControllerTest {

    private AddressDtoController addressDTOController;

    private MockMvc mockMvc;

    @Mock
    private AddressService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        addressDTOController = new AddressDtoController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(addressDTOController)
                .setControllerAdvice(new RestResponseEntityExceptionHandler())
                .build();
    }

    @Test
    void getListOfAddresses() throws Exception {
        when(service.getAddressesDTO()).thenReturn(Arrays.asList(new AddressDTO(), new AddressDTO()));

        mockMvc.perform(get(AddressDtoController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addresses", hasSize(2)));
    }

    @Test
    void getAddressById() throws Exception {
        //given
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(1L);
        addressDTO.setStreet("Pushkina");

        when(service.findDTObyId(addressDTO.getId())).thenReturn(addressDTO);

        //then
        mockMvc.perform(get(AddressDtoController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street", equalTo("Pushkina")));
    }

    @Test
    void createNewAddress() throws Exception {
        //given
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setFacilityNumber(78);
        addressDTO.setPostcode(7100);
        addressDTO.setCity("City");
        addressDTO.setDistrict("District");
        addressDTO.setStreet("Street");

        AddressDTO returnDTO = new AddressDTO();
        returnDTO.setFacilityNumber(addressDTO.getFacilityNumber());
        returnDTO.setPostcode(addressDTO.getPostcode());
        returnDTO.setCity(addressDTO.getCity());
        returnDTO.setDistrict(addressDTO.getDistrict());
        returnDTO.setStreet(addressDTO.getStreet());

        when(service.saveAddressDTO(addressDTO)).thenReturn(returnDTO);

        //then
        mockMvc.perform(post(AddressDtoController.BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(addressDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    void updateCustomer() throws Exception {
        //given
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(1L);
        addressDTO.setFacilityNumber(78);
        addressDTO.setPostcode(7100);
        addressDTO.setCity("City");
        addressDTO.setDistrict("District");
        addressDTO.setStreet("Street");

        AddressDTO returnDTO = new AddressDTO();
        returnDTO.setId(addressDTO.getId());
        returnDTO.setFacilityNumber(addressDTO.getFacilityNumber());
        returnDTO.setPostcode(addressDTO.getPostcode());
        returnDTO.setCity(addressDTO.getCity());
        returnDTO.setDistrict(addressDTO.getDistrict());
        returnDTO.setStreet(addressDTO.getStreet());

        when(service.saveAddressDTO(addressDTO)).thenReturn(returnDTO);

        //when/then
        mockMvc.perform(put(AddressDtoController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(addressDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void patchAddress() throws Exception {
        //given
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setId(1L);
        addressDTO.setFacilityNumber(78);

        AddressDTO returnDTO = new AddressDTO();
        returnDTO.setId(addressDTO.getId());
        returnDTO.setFacilityNumber(addressDTO.getFacilityNumber());

        when(service.patchAddress(addressDTO, 1L)).thenReturn(returnDTO);

        mockMvc.perform(patch(AddressDtoController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(addressDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAddress() throws Exception {
        mockMvc.perform(delete(AddressDtoController.BASE_URL + "/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(service).deleteById(anyLong());
    }
}