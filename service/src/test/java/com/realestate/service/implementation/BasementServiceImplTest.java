package com.realestate.service.implementation;

import com.realestate.dto.BasementDTO;
import com.realestate.exceptions.NotFoundException;
import com.realestate.mapper.BasementMapper;
import com.realestate.model.buildings.Basement;
import com.realestate.repositories.BasementRepository;
import com.realestate.repositories.ClientRepository;
import com.realestate.service.BasementService;
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

class BasementServiceImplTest {

    private BasementService service;

    @Mock
    private BasementRepository basementRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private BasementMapper basementMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new BasementServiceImpl(basementRepository, clientRepository, basementMapper);
    }

    @Test
    void getById() {
        //given
        Basement source = new Basement();
        source.setId(1L);
        when(basementRepository.findBasementByIdWithClients(anyLong())).thenReturn(Optional.of(source));

        //when
        Basement basement = service.getById(1L);

        //then
        assertNotNull(basement);
        assertEquals(1, basement.getId());
        verify(basementRepository, times(1)).findBasementByIdWithClients(anyLong());
    }

    @Test
    void getByIdThrowException() {
        assertThrows(NotFoundException.class, () -> service.getById(anyLong()));
    }

    @Test
    void getBasements() {
        //given
        when(service.getBasements())
                .thenReturn(new ArrayList<>(Collections.singletonList(new Basement())));

        //when
        List<Basement> basements = service.getBasements();

        //then
        assertEquals(1, basements.size());
        verify(basementRepository, times(1)).findAll();
    }

    @Test
    void findDTObyId() {
        //given
        when(basementRepository.findBasementByIdWithClients(anyLong())).thenReturn(Optional.of(new Basement()));

        BasementDTO basementDTO = new BasementDTO();
        basementDTO.setId(1L);
        when(basementMapper.basementToBasementDTO(any())).thenReturn(basementDTO);

        //when
        BasementDTO command = service.findDTObyId(1L);

        //then
        assertNotNull(command);
        assertEquals(1L, command.getId());
        verify(basementRepository, times(1)).findBasementByIdWithClients(anyLong());
    }

    @Test
    void deleteById() {
        service.deleteById(anyLong());
        verify(basementRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void saveImage() throws IOException {
        Long id = 1L;
        Basement basement = new Basement();
        basement.setId(id);

        when(basementRepository.findById(id)).thenReturn(Optional.of(basement));
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt",
                "text/plain", "BasementImageStub".getBytes());
        ArgumentCaptor<Basement> basementCaptor = ArgumentCaptor.forClass(Basement.class);

        //when
        service.saveImage(id, multipartFile);

        //then
        verify(basementRepository, times(1)).save(basementCaptor.capture());
        Basement saved = basementCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, saved.getImage().length);
    }
}