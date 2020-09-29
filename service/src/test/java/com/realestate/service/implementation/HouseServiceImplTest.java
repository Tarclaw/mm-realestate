package com.realestate.service.implementation;

import com.realestate.dto.HouseDTO;
import com.realestate.exceptions.NotFoundException;
import com.realestate.mapper.HouseMapper;
import com.realestate.model.buildings.House;
import com.realestate.repositories.ClientRepository;
import com.realestate.repositories.HouseRepository;
import com.realestate.service.HouseService;
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

class HouseServiceImplTest {

    private HouseService service;

    @Mock
    private HouseRepository houseRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private HouseMapper houseMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new HouseServiceImpl(houseRepository, clientRepository, houseMapper);
    }

    @Test
    void getById() {
        //given
        House source = new House();
        source.setId(1L);
        when(houseRepository.findHousesByIdWithClients(anyLong())).thenReturn(Optional.of(source));

        //when
        House house = service.getById(1L);

        //then
        assertNotNull(house);
        assertEquals(1L, house.getId());
        verify(houseRepository, times(1)).findHousesByIdWithClients(anyLong());
    }

    @Test
    void getByIdExceptionHandling() {
        assertThrows(NotFoundException.class, () -> service.getById(anyLong()));
    }

    @Test
    void getHouses() {
        when(service.getHouses()).
                thenReturn(new ArrayList<>(Collections.singletonList(new House())));

        List<House> houses = service.getHouses();

        assertEquals(1, houses.size());
        verify(houseRepository, times(1)).findAll();
    }

    @Test
    void findDTObyId() {
        //given
        when(houseRepository.findHousesByIdWithClients(anyLong())).thenReturn(Optional.of(new House()));
        HouseDTO houseDTO = new HouseDTO();
        houseDTO.setId(1L);
        when(houseMapper.houseToHouseDTO(any())).thenReturn(houseDTO);

        //when
        HouseDTO command = service.findDTObyId(1L);

        //then
        assertNotNull(command);
        assertEquals(1L, command.getId());
        verify(houseRepository, times(1)).findHousesByIdWithClients(anyLong());
        verify(houseMapper, times(1)).houseToHouseDTO(any());
    }

    @Test
    void deleteById() {
        service.deleteById(anyLong());
        verify(houseRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void saveImage() throws IOException {
        //given
        Long id = 1L;
        House house = new House();
        house.setId(id);

        when(houseRepository.findById(id)).thenReturn(Optional.of(house));

        MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "test.txt",
                "text/plain", "HouseImageStub".getBytes());
        ArgumentCaptor<House> houseCaptor = ArgumentCaptor.forClass(House.class);

        //when
        service.saveImage(id, multipartFile);

        //then
        verify(houseRepository, times(1)).save(houseCaptor.capture());

        House saved = houseCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, saved.getImage().length);
    }
}