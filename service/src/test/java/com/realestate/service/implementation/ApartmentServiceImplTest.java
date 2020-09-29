package com.realestate.service.implementation;

import com.realestate.dto.ApartmentDTO;
import com.realestate.exceptions.NotFoundException;
import com.realestate.mapper.ApartmentMapper;
import com.realestate.model.buildings.Apartment;
import com.realestate.repositories.ApartmentRepository;
import com.realestate.repositories.ClientRepository;
import com.realestate.service.ApartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ApartmentServiceImplTest {

    private ApartmentService service;

    @Mock
    private ApartmentRepository apartmentRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ApartmentMapper apartmentMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new ApartmentServiceImpl(apartmentRepository, clientRepository, apartmentMapper);
    }

    @Test
    void getById() {
        Apartment apartment = new Apartment();
        apartment.setId(1L);
        Optional<Apartment> source = Optional.of(apartment);
        when(apartmentRepository.findApartmentsByIdWithClients(anyLong())).thenReturn(source);

        Apartment apartmentFromRepo = service.getById(1L);

        assertNotNull(apartmentFromRepo);
        assertEquals(1L, apartmentFromRepo.getId());
        verify(apartmentRepository, times(1)).findApartmentsByIdWithClients(anyLong());
    }

    @Test
    void getByIdWhenThereIsNoApartmentInDB() {
        assertThrows(NotFoundException.class, () -> service.getById(anyLong()));
    }

    @Test
    void getApartments() {
        when(service.getApartments())
                .thenReturn(new ArrayList<>(Collections.singletonList(new Apartment())));

        List<Apartment> apartments = service.getApartments();

        assertEquals(1, apartments.size());
        verify(apartmentRepository, times(1)).findAll();
    }

    @Test
    void findDTObyId() {
        //given
        when(apartmentRepository.findApartmentsByIdWithClients(anyLong())).thenReturn(Optional.of(new Apartment()));

        ApartmentDTO apartmentDTO = new ApartmentDTO();
        apartmentDTO.setId(1L);
        when(apartmentMapper.apartmentToApartmentDTO(any())).thenReturn(apartmentDTO);

        //when
        ApartmentDTO command = service.findDTObyId(1L);

        //then
        assertEquals(1L, command.getId());
        verify(apartmentRepository, times(1)).findApartmentsByIdWithClients(anyLong());
        verify(apartmentMapper, times(1)).apartmentToApartmentDTO(any());
    }

    @Test
    void deleteById() {
        service.deleteById(1L);
        verify(apartmentRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void saveImage() throws IOException {
        //given
        Long id = 1L;
        Apartment apartment = new Apartment();
        apartment.setId(id);

        when(apartmentRepository.findById(id)).thenReturn(Optional.of(apartment));

        MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt",
                "text/plain", "ApartmentImage".getBytes());
        ArgumentCaptor<Apartment> apartmentCaptor = ArgumentCaptor.forClass(Apartment.class);

        //when
        service.saveImage(id, multipartFile);

        //then
        verify(apartmentRepository, times(1)).save(apartmentCaptor.capture());
        Apartment saved = apartmentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, saved.getImage().length);
    }
}