package com.realestate.controllers.web;

import com.realestate.dto.AddressDTO;
import com.realestate.dto.StorageDTO;
import com.realestate.exceptions.NotFoundException;
import com.realestate.model.buildings.Storage;
import com.realestate.model.people.Client;
import com.realestate.service.ClientService;
import com.realestate.service.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class StorageControllerTest {

    private StorageController controller;

    private MockMvc mockMvc;

    @Mock
    private StorageService storageService;

    @Mock
    private ClientService clientService;

    @Mock
    private Model model;

    @Mock
    private BindingResult storageBinding;

    @Mock
    private BindingResult addressBinding;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new StorageController(storageService, clientService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void getStorageById() throws Exception {
        //given
        when(storageService.getById(anyLong())).thenReturn(new Storage());
        ArgumentCaptor<Storage> storageCaptor = ArgumentCaptor.forClass(Storage.class);

        //when
        String viewName = controller.getStorageById("1", model);

        //then
        assertEquals("storage/show", viewName);
        verify(storageService, times(1)).getById(anyLong());
        verify(model, times(1)).addAttribute(eq("storage"), storageCaptor.capture());

        mockMvc.perform(get("/app/v1/all/1/show/storage"))
                .andExpect(status().isOk())
                .andExpect(view().name("storage/show"))
                .andExpect(model().attributeExists("storage"));
    }

    @Test
    void getStorageByIdWhenThereIsNoThisStorageInDB() throws Exception {

        when(storageService.getById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/app/v1/all/111/show/storage"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    void getStorageByIdWhenNumberFormatException() throws Exception {

        mockMvc.perform(get("/app/v1/all/abc/show/storage"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    void getAllStorages() throws Exception {
        // given
        List<Storage> storages = new ArrayList<>(
                Collections.singletonList(new Storage())
        );

        when(storageService.getStorages()).thenReturn(storages);

        ArgumentCaptor<List<Storage>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        //when
        String viewName = controller.getAllStorages(model);

        //then
        assertEquals("storages", viewName);
        verify(storageService, times(1)).getStorages();
        verify(model, times(1)).addAttribute(eq("storages"), argumentCaptor.capture());

        List<Storage> setInController = argumentCaptor.getValue();
        assertEquals(1, setInController.size());

        mockMvc.perform(get("/app/v1/all/storage"))
                .andExpect(status().isOk())
                .andExpect(view().name("storages"));
    }

    @Test
    void newStorage() throws Exception {
        //given
        when(clientService.getClients()).thenReturn(
                new ArrayList<>(
                        Collections.singletonList(new Client())
                )
        );
        ArgumentCaptor<StorageDTO> storageCaptor = ArgumentCaptor.forClass(StorageDTO.class);
        ArgumentCaptor<AddressDTO> addressCaptor = ArgumentCaptor.forClass(AddressDTO.class);
        ArgumentCaptor<Set<Client>> clientCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.newStorage(model);

        //then
        assertEquals("storage/storageForm", viewName);
        verify(model, times(1)).addAttribute(eq("storage"), storageCaptor.capture());
        verify(model, times(1)).addAttribute(eq("address"), addressCaptor.capture());
        verify(model, times(1)).addAttribute(eq("clients"), clientCaptor.capture());

        mockMvc.perform(get("/app/v1/storage/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("storage/storageForm"))
                .andExpect(model().attributeExists("storage"))
                .andExpect(model().attributeExists("address"))
                .andExpect(model().attributeExists("clients"));
    }

    @Test
    void updateStorage() throws Exception {
        //given
        StorageDTO storageDTO = new StorageDTO();
        storageDTO.setAddressDTO(new AddressDTO());
        when(storageService.findDTObyId(anyLong())).thenReturn(storageDTO);
        when(clientService.getClients()).thenReturn(
                new ArrayList<>(
                        Collections.singletonList(new Client())
                )
        );
        ArgumentCaptor<StorageDTO> storageCaptor = ArgumentCaptor.forClass(StorageDTO.class);
        ArgumentCaptor<AddressDTO> addressCaptor = ArgumentCaptor.forClass(AddressDTO.class);
        ArgumentCaptor<Set<Client>> clientCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.updateStorage("1", model);

        //then
        assertEquals("storage/storageForm", viewName);
        verify(storageService, times(1)).findDTObyId(anyLong());
        verify(model, times(1)).addAttribute(eq("storage"), storageCaptor.capture());
        verify(model, times(1)).addAttribute(eq("address"), addressCaptor.capture());
        verify(model, times(1)).addAttribute(eq("clients"), clientCaptor.capture());

        mockMvc.perform(get("/app/v1/storage/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("storage/storageForm"))
                .andExpect(model().attributeExists("storage"))
                .andExpect(model().attributeExists("address"))
                .andExpect(model().attributeExists("clients"));
    }

    @Test
    void saveOrUpdate() throws Exception {
        //given
        StorageDTO storageDTO = new StorageDTO();
        storageDTO.setId(1L);
        AddressDTO addressDTO = new AddressDTO();

        when(storageBinding.hasErrors()).thenReturn(false);
        when(addressBinding.hasErrors()).thenReturn(false);
        when(storageService.saveDTO(any())).thenReturn(storageDTO);

        //when
        String viewName = controller.saveOrUpdate(storageDTO, storageBinding, addressDTO, addressBinding, model);

        //then
        assertEquals("redirect:/app/v1/all/1/show/storage", viewName);
        verify(storageBinding, times(1)).hasErrors();
        verify(addressBinding, times(1)).hasErrors();
        verify(storageService, times(1)).saveDTO(any());

        mockMvc.perform(post("/app/v1/storage/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("numberOfRooms", "1")
                .param("totalArea", "20")
                .param("description", "some description")
                .param("monthRent", "3000")
                .param("price", "300000")
                .param("postcode", "88550")
                .param("facilityNumber", "33")
                .param("city", "some city")
                .param("district", "some district")
                .param("street", "some street")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/app/v1/all/1/show/storage"));
    }

    @Test
    void saveOrUpdateWhenCommandValuesAreNotValid() throws Exception {
        //given
        when(storageBinding.hasErrors()).thenReturn(true);

        ArgumentCaptor<StorageDTO> storageCaptor = ArgumentCaptor.forClass(StorageDTO.class);
        ArgumentCaptor<AddressDTO> addressCaptor = ArgumentCaptor.forClass(AddressDTO.class);
        ArgumentCaptor<Set<Client>> clientsCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.saveOrUpdate(new StorageDTO(), storageBinding, new AddressDTO(), addressBinding, model);
        assertEquals("storage/storageForm", viewName);
        verify(storageBinding, times(1)).hasErrors();
        verify(storageBinding, times(1)).getAllErrors();
        verify(addressBinding, times(1)).getAllErrors();
        verify(model, times(1)).addAttribute(eq("storage"), storageCaptor.capture());
        verify(model, times(1)).addAttribute(eq("address"), addressCaptor.capture());
        verify(model, times(1)).addAttribute(eq("clients"), clientsCaptor.capture());
        verify(clientService, times(1)).getClients();

        mockMvc.perform(post("/app/v1/storage/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("storage/storageForm"));
    }

    @Test
    void deleteById() throws Exception {
        String viewName = controller.deleteById("1");
        assertEquals("redirect:/app/v1/all/storage", viewName);

        mockMvc.perform(get("/app/v1/storage/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/app/v1/all/storage"));
    }

    @Test
    void storageImageUpload() throws Exception {
        //given
        Storage storage = new Storage();
        storage.setId(1L);

        when(storageService.getById(anyLong())).thenReturn(storage);

        ArgumentCaptor<Storage> storageCaptor = ArgumentCaptor.forClass(Storage.class);

        //when
        String viewName = controller.storageImageUpload("1", model);

        //then
        assertEquals("storage/storageImageUpload", viewName);
        verify(storageService, times(1)).getById(anyLong());
        verify(model, times(1)).addAttribute(eq("storage"), storageCaptor.capture());

        mockMvc.perform(get("/app/v1/storage/1/image")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("storage/storageImageUpload"));
    }

    @Test
    void renderStorageImage() throws Exception {
        //given
        StorageDTO storage = new StorageDTO();
        storage.setId(1L);
        storage.setImage("StorageImageStub".getBytes());

        when(storageService.findDTObyId(anyLong())).thenReturn(storage);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/app/v1/storage/1/storageimage"))
                .andExpect(status().isOk()).andReturn().getResponse();

        //then
        assertEquals(storage.getImage().length, response.getContentAsByteArray().length);
    }

    @Test
    void saveStorageImage() throws Exception {
        //given
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "test.txt",
                "text/plain", "StorageImageStub".getBytes());

        //when
        String viewName = controller.saveStorageImage("1", multipartFile);

        //then
        assertEquals("redirect:/app/v1/all/1/show/storage", viewName);
        verify(storageService, times(1)).saveImage(1L, multipartFile);

        mockMvc.perform(multipart("/app/v1/storage/1/image")
                .file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/app/v1/all/1/show/storage"));
    }
}