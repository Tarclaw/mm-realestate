package com.realestate.controllers.web;

import com.realestate.dto.AddressDTO;
import com.realestate.dto.BasementDTO;
import com.realestate.exceptions.NotFoundException;
import com.realestate.model.buildings.Basement;
import com.realestate.model.people.Client;
import com.realestate.service.BasementService;
import com.realestate.service.ClientService;
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

class BasementControllerTest {

    private BasementController controller;

    private MockMvc mockMvc;

    @Mock
    private BasementService basementService;

    @Mock
    private ClientService clientService;

    @Mock
    private Model model;

    @Mock
    private BindingResult basementBinding;

    @Mock
    private BindingResult addressBinding;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new BasementController(basementService, clientService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void getBasementById() throws Exception {
        //given
        when(basementService.getById(anyLong())).thenReturn(new Basement());
        ArgumentCaptor<Basement> basementCaptor = ArgumentCaptor.forClass(Basement.class);

        //when
        String viewName = controller.getBasementById("1", model);

        //then
        assertEquals("basement/show", viewName);
        verify(basementService, times(1)).getById(anyLong());
        verify(model, times(1)).addAttribute(eq("basement"), basementCaptor.capture());

        mockMvc.perform(get("/app/v1/all/1/show/basement"))
                .andExpect(status().isOk())
                .andExpect(view().name("basement/show"))
                .andExpect(model().attributeExists("basement"));
    }

    @Test
    void getBasementByIdWhenThereIsNoThisBasementInDB() throws Exception {

        when(basementService.getById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/app/v1/all/111/show/basement"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    void getBasementByIdWhenNumberFormatException() throws Exception {

        mockMvc.perform(get("/app/v1/all/abc/show/basement"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    void getAllBasements() throws Exception {
        //given
        List<Basement> basements = new ArrayList<>(
                Collections.singletonList(new Basement())
        );
        when(basementService.getBasements()).thenReturn(basements);

        ArgumentCaptor<List<Basement>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        //when
        String viewName = controller.getAllBasements(model);

        //then
        assertEquals("basements", viewName);
        verify(basementService, times(1)).getBasements();
        verify(model, times(1)).addAttribute(eq("basements"), argumentCaptor.capture());

        List<Basement> setInController = argumentCaptor.getValue();
        assertEquals(1, setInController.size());

        mockMvc.perform(get("/app/v1/all/basement"))
                .andExpect(status().isOk())
                .andExpect(view().name("basements"));
    }

    @Test
    void newBasement() throws Exception {
        when(clientService.getClients()).thenReturn(
                new ArrayList<>(
                        Collections.singletonList(new Client())
                )
        );
        ArgumentCaptor<BasementDTO> basementCaptor = ArgumentCaptor.forClass(BasementDTO.class);
        ArgumentCaptor<AddressDTO> addressCaptor = ArgumentCaptor.forClass(AddressDTO.class);
        ArgumentCaptor<Set<Client>> clientCaptor = ArgumentCaptor.forClass(Set.class);

        String viewName = controller.newBasement(model);
        assertEquals("basement/basementForm", viewName);
        verify(model, times(1)).addAttribute(eq("basement"), basementCaptor.capture());
        verify(model, times(1)).addAttribute(eq("address"), addressCaptor.capture());
        verify(model, times(1)).addAttribute(eq("clients"), clientCaptor.capture());

        mockMvc.perform(get("/app/v1/basement/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("basement/basementForm"))
                .andExpect(model().attributeExists("basement"))
                .andExpect(model().attributeExists("address"))
                .andExpect(model().attributeExists("clients"));
    }

    @Test
    void updateBasement() throws Exception {
        //given
        BasementDTO basementDTO = new BasementDTO();
        basementDTO.setAddressDTO(new AddressDTO());
        when(basementService.findDTObyId(anyLong())).thenReturn(basementDTO);
        when(clientService.getClients()).thenReturn(
                new ArrayList<>(
                        Collections.singletonList(new Client())
                )
        );
        ArgumentCaptor<BasementDTO> basementCaptor = ArgumentCaptor.forClass(BasementDTO.class);
        ArgumentCaptor<AddressDTO> addressCaptor = ArgumentCaptor.forClass(AddressDTO.class);
        ArgumentCaptor<Set<Client>> clientCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.updateBasement("1", model);

        //then
        assertEquals("basement/basementForm", viewName);
        verify(basementService, times(1)).findDTObyId(anyLong());
        verify(model, times(1)).addAttribute(eq("basement"), basementCaptor.capture());
        verify(model, times(1)).addAttribute(eq("address"), addressCaptor.capture());
        verify(model, times(1)).addAttribute(eq("clients"), clientCaptor.capture());

        mockMvc.perform(get("/app/v1/basement/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("basement/basementForm"))
                .andExpect(model().attributeExists("basement"))
                .andExpect(model().attributeExists("address"))
                .andExpect(model().attributeExists("clients"));
    }

    @Test
    void saveOrUpdate() throws Exception {
        //given
        BasementDTO basementDTO = new BasementDTO();
        basementDTO.setId(1L);
        AddressDTO addressDTO = new AddressDTO();

        when(basementBinding.hasErrors()).thenReturn(false);
        when(addressBinding.hasErrors()).thenReturn(false);
        when(basementService.saveDTO(any())).thenReturn(basementDTO);

        //when
        String viewName = controller.saveOrUpdate(basementDTO, basementBinding, addressDTO, addressBinding, model);

        //then
        assertEquals("redirect:/app/v1/all/1/show/basement", viewName);
        verify(basementBinding, times(1)).hasErrors();
        verify(addressBinding, times(1)).hasErrors();
        verify(basementService, times(1)).saveDTO(any());

        mockMvc.perform(post("/app/v1/basement/save")
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
                .andExpect(view().name("redirect:/app/v1/all/1/show/basement"));
    }

    @Test
    void deleteById() throws Exception {
        String viewName = controller.deleteById("1");
        assertEquals("redirect:/app/v1/all/basement", viewName);
        verify(basementService, times(1)).deleteById(anyLong());

        mockMvc.perform(get("/app/v1/basement/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/app/v1/all/basement"));
    }

    @Test
    void basementImageUpload() throws Exception {
        //given
        Basement basement = new Basement();
        basement.setId(1L);
        when(basementService.getById(anyLong())).thenReturn(basement);
        ArgumentCaptor<Basement> basementCaptor = ArgumentCaptor.forClass(Basement.class);

        //when
        String viewName = controller.basementImageUpload("1", model);

        //then
        assertEquals("basement/basementImageUpload", viewName);
        verify(basementService, times(1)).getById(anyLong());
        verify(model, times(1)).addAttribute(eq("basement"), basementCaptor.capture());

        mockMvc.perform(get("/app/v1/basement/1/image")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("basement/basementImageUpload"));
    }

    @Test
    void renderBasementImage() throws Exception {
        //given
        BasementDTO basementDTO = new BasementDTO();
        basementDTO.setId(1L);
        basementDTO.setImage("BasementImageStub".getBytes());

        when(basementService.findDTObyId(anyLong())).thenReturn(basementDTO);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/app/v1/basement/1/basementimage"))
                .andExpect(status().isOk()).andReturn().getResponse();

        //then
        assertEquals(basementDTO.getImage().length, response.getContentAsByteArray().length);
    }

    @Test
    void saveBasementImage() throws Exception {
        //given
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt",
                "text/plan", "BasementImageStub".getBytes());

        //when
        String viewName = controller.saveBasementImage("1", multipartFile);

        //then
        assertEquals("redirect:/app/v1/all/1/show/basement", viewName);
        verify(basementService, times(1)).saveImage(1L, multipartFile);

        mockMvc.perform(multipart("/app/v1/basement/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/app/v1/all/1/show/basement"));
    }
}