package com.realestate.service.implementation;

import com.realestate.dto.GarageDTO;
import com.realestate.exceptions.NotFoundException;
import com.realestate.mapper.GarageMapper;
import com.realestate.model.buildings.Garage;
import com.realestate.repositories.ClientRepository;
import com.realestate.repositories.GarageRepository;
import com.realestate.service.GarageService;
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

class GarageServiceImplTest {

    private GarageService service;

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private GarageMapper garageMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new GarageServiceImpl(garageRepository, clientRepository, garageMapper);
    }

    @Test
    void getById() {
        //given
        Garage source = new Garage();
        source.setId(1L);
        when(garageRepository.findGaragesByIdWithClients(anyLong())).thenReturn(Optional.of(source));

        //when
        Garage garage = service.getById(1L);

        //then
        assertNotNull(garage);
        assertEquals(1L, garage.getId());
        verify(garageRepository, times(1)).findGaragesByIdWithClients(anyLong());
    }

    @Test
    void getByIdExceptionHandling() {
        assertThrows(NotFoundException.class, () -> service.getById(anyLong()));
    }

    @Test
    void getGarages() {
        when(service.getGarages()).
                thenReturn(new ArrayList<>(Collections.singletonList(new Garage())));

        List<Garage> garages = service.getGarages();

        assertEquals(1, garages.size());
        verify(garageRepository, times(1)).findAll();
    }

    @Test
    void findDTObyId() {
        //given
        when(garageRepository.findGaragesByIdWithClients(anyLong())).thenReturn(Optional.of(new Garage()));
        GarageDTO garageDTO = new GarageDTO();
        garageDTO.setId(1L);
        when(garageMapper.garageToGarageDTO(any())).thenReturn(garageDTO);

        //when
        GarageDTO command = service.findDTObyId(1L);

        //then
        assertNotNull(command);
        assertEquals(1L, command.getId());
        verify(garageRepository, times(1)).findGaragesByIdWithClients(anyLong());
        verify(garageMapper, times(1)).garageToGarageDTO(any());
    }

    @Test
    void deleteById() {
        service.deleteById(anyLong());
        verify(garageRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void saveImage() throws IOException {
        //given
        Long id = 1L;
        Garage garage = new Garage();
        garage.setId(id);

        when(garageRepository.findById(id)).thenReturn(Optional.of(garage));

        ArgumentCaptor<Garage> garageCaptor = ArgumentCaptor.forClass(Garage.class);
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt",
                "text/plain", "GarageImage".getBytes());

        //when
        service.saveImage(id, multipartFile);

        //then
        verify(garageRepository, times(1)).save(garageCaptor.capture());
        Garage saved = garageCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, saved.getImage().length);
    }
}