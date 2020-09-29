package com.realestate.service.implementation;

import com.realestate.dto.StorageDTO;
import com.realestate.exceptions.NotFoundException;
import com.realestate.mapper.StorageMapper;
import com.realestate.model.buildings.Storage;
import com.realestate.repositories.ClientRepository;
import com.realestate.repositories.StorageRepository;
import com.realestate.service.StorageService;
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

class StorageServiceImplTest {

    private StorageService service;

    @Mock
    private StorageRepository storageRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private StorageMapper storageMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new StorageServiceImpl(storageRepository, clientRepository, storageMapper);
    }

    @Test
    void getById() {
        //given
        Storage source = new Storage();
        source.setId(1L);
        when(storageRepository.findStoragesByIdWithClients(anyLong())).thenReturn(Optional.of(source));

        //when
        Storage storage = service.getById(1L);

        //then
        assertNotNull(storage);
        assertEquals(1L, storage.getId());
        verify(storageRepository, times(1)).findStoragesByIdWithClients(anyLong());
    }

    @Test
    void getByIdExceptionHandling() {
        assertThrows(NotFoundException.class, () -> service.getById(anyLong()));
    }

    @Test
    void getStorages() {
        when(service.getStorages()).
                thenReturn(new ArrayList<>(Collections.singletonList(new Storage())));

        List<Storage> storages = service.getStorages();

        assertEquals(1, storages.size());
        verify(storageRepository, times(1)).findAll();
    }

    @Test
    void findDTObyId() {
        //given
        Storage storage = new Storage();
        storage.setId(1L);
        StorageDTO source = new StorageDTO();
        source.setId(1L);
        when(storageRepository.findStoragesByIdWithClients(anyLong())).thenReturn(Optional.of(storage));
        when(storageMapper.storageToStorageDTO(storage)).thenReturn(source);

        //when
        StorageDTO command = service.findDTObyId(1L);

        //then
        assertNotNull(command);
        assertEquals(1L, command.getId());
        verify(storageRepository, times(1)).findStoragesByIdWithClients(anyLong());
        verify(storageMapper, times(1)).storageToStorageDTO(any());
    }

    @Test
    void deleteById() {
        service.deleteById(anyLong());
        verify(storageRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void saveImage() throws IOException {
        //given
        Long id = 1L;
        Storage storage = new Storage();
        storage.setId(id);

        when(storageRepository.findById(id)).thenReturn(Optional.of(storage));

        ArgumentCaptor<Storage> storageCaptor = ArgumentCaptor.forClass(Storage.class);
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "test.txt",
                "textplain", "StorageImageStub".getBytes());

        //when
        service.saveImage(id, multipartFile);

        //then
        verify(storageRepository, times(1)).save(storageCaptor.capture());

        Storage saved = storageCaptor.getValue();
        assertEquals(multipartFile.getBytes().length, saved.getImage().length);
    }
}