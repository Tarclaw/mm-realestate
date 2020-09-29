package com.realestate.controllers.web;

import com.realestate.dto.AddressDTO;
import com.realestate.dto.GarageDTO;
import com.realestate.exceptions.NotFoundException;
import com.realestate.model.buildings.Garage;
import com.realestate.model.people.Client;
import com.realestate.service.ClientService;
import com.realestate.service.GarageService;
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

class GarageControllerTest {

    private GarageController controller;

    private MockMvc mockMvc;

    @Mock
    private GarageService garageService;

    @Mock
    private ClientService clientService;

    @Mock
    private Model model;

    @Mock
    private BindingResult garageBinding;

    @Mock
    private BindingResult addressBinding;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new GarageController(garageService, clientService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void getGarageById() throws Exception {
        //given
        when(garageService.getById(anyLong())).thenReturn(new Garage());
        ArgumentCaptor<Garage> garageCaptor = ArgumentCaptor.forClass(Garage.class);

        //when
        String viewName = controller.getGarageById("1", model);

        //then
        assertEquals("garage/show", viewName);
        verify(garageService, times(1)).getById(anyLong());
        verify(model, times(1)).addAttribute(eq("garage"), garageCaptor.capture());

        mockMvc.perform(get("/app/v1/all/1/show/garage"))
                .andExpect(status().isOk())
                .andExpect(view().name("garage/show"))
                .andExpect(model().attributeExists("garage"));
    }

    @Test
    void getGarageByIdWhenThereIsNoThisGarageInDB() throws Exception {

        when(garageService.getById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/app/v1/all/111/show/garage"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    void getGarageByIdWhenNumberFormatException() throws Exception {

        mockMvc.perform(get("/app/v1/all/abc/show/garage"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    void getAllGarages() throws Exception {
        //given
        List<Garage> garages = new ArrayList<>(
                Collections.singletonList(new Garage())
        );

        when(garageService.getGarages()).thenReturn(garages);

        ArgumentCaptor<List<Garage>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        //when
        String viewName = controller.getAllGarages(model);

        //then
        assertEquals("garages", viewName);
        verify(garageService, times(1)).getGarages();
        verify(model, times(1)).addAttribute(eq("garages"), argumentCaptor.capture());

        List<Garage> setInController = argumentCaptor.getValue();
        assertEquals(1, setInController.size());

        mockMvc.perform(get("/app/v1/all/garage"))
                .andExpect(status().isOk())
                .andExpect(view().name("garages"));
    }

    @Test
    void newGarage() throws Exception {
        //given
        when(clientService.getClients()).thenReturn(
                new ArrayList<>(
                        Collections.singletonList(new Client())
                )
        );
        ArgumentCaptor<GarageDTO> garageCaptor = ArgumentCaptor.forClass(GarageDTO.class);
        ArgumentCaptor<AddressDTO> addressCaptor = ArgumentCaptor.forClass(AddressDTO.class);
        ArgumentCaptor<Set<Client>> clientCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.newGarage(model);

        //then
        assertEquals("garage/garageForm", viewName);
        verify(model, times(1)).addAttribute(eq("garage"), garageCaptor.capture());
        verify(model, times(1)).addAttribute(eq("address"), addressCaptor.capture());
        verify(model, times(1)).addAttribute(eq("clients"), clientCaptor.capture());

        mockMvc.perform(get("/app/v1/garage/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("garage/garageForm"))
                .andExpect(model().attributeExists("garage"))
                .andExpect(model().attributeExists("address"))
                .andExpect(model().attributeExists("clients"));
    }

    @Test
    void updateGarage() throws Exception {
        //given
        GarageDTO garageDTO = new GarageDTO();
        garageDTO.setAddressDTO(new AddressDTO());
        when(garageService.findDTObyId(anyLong())).thenReturn(garageDTO);
        when(clientService.getClients()).thenReturn(
                new ArrayList<>(
                        Collections.singletonList(new Client())
                )
        );
        ArgumentCaptor<GarageDTO> garageCaptor = ArgumentCaptor.forClass(GarageDTO.class);
        ArgumentCaptor<AddressDTO> addressCaptor = ArgumentCaptor.forClass(AddressDTO.class);
        ArgumentCaptor<Set<Client>> clientCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.updateGarage("1", model);

        //then
        assertEquals("garage/garageForm", viewName);
        verify(garageService, times(1)).findDTObyId(anyLong());
        verify(model, times(1)).addAttribute(eq("garage"), garageCaptor.capture());
        verify(model, times(1)).addAttribute(eq("address"), addressCaptor.capture());
        verify(model, times(1)).addAttribute(eq("clients"), clientCaptor.capture());

        mockMvc.perform(get("/app/v1/garage/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("garage/garageForm"))
                .andExpect(model().attributeExists("garage"))
                .andExpect(model().attributeExists("address"))
                .andExpect(model().attributeExists("clients"));
    }

    @Test
    void saveOrUpdate() throws Exception {
        //given
        GarageDTO garageDTO = new GarageDTO();
        garageDTO.setId(1L);
        AddressDTO addressDTO = new AddressDTO();

        when(garageBinding.hasErrors()).thenReturn(false);
        when(addressBinding.hasErrors()).thenReturn(false);
        when(garageService.saveDTO(any())).thenReturn(garageDTO);

        //when
        String viewName = controller.saveOrUpdate(garageDTO, garageBinding, addressDTO, addressBinding, model);

        //then
        assertEquals("redirect:/app/v1/all/1/show/garage", viewName);
        verify(garageBinding, times(1)).hasErrors();
        verify(addressBinding, times(1)).hasErrors();
        verify(garageService, times(1)).saveDTO(any());

        mockMvc.perform(post("/app/v1/garage/save")
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
                .andExpect(view().name("redirect:/app/v1/all/1/show/garage"));
    }

    @Test
    void saveOrUpdateWhenCommandValuesAreNotValid() throws Exception {
        //given
        when(garageBinding.hasErrors()).thenReturn(true);

        ArgumentCaptor<GarageDTO> garageCaptor = ArgumentCaptor.forClass(GarageDTO.class);
        ArgumentCaptor<AddressDTO> addressCaptor = ArgumentCaptor.forClass(AddressDTO.class);
        ArgumentCaptor<Set<Client>> clientsCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.saveOrUpdate(new GarageDTO(), garageBinding, new AddressDTO(), addressBinding, model);
        assertEquals("garage/garageForm", viewName);
        verify(garageBinding, times(1)).hasErrors();
        verify(garageBinding, times(1)).getAllErrors();
        verify(addressBinding, times(1)).getAllErrors();
        verify(model, times(1)).addAttribute(eq("garage"), garageCaptor.capture());
        verify(model, times(1)).addAttribute(eq("address"), addressCaptor.capture());
        verify(model, times(1)).addAttribute(eq("clients"), clientsCaptor.capture());
        verify(clientService, times(1)).getClients();

        mockMvc.perform(post("/app/v1/garage/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("garage/garageForm"));
    }

    @Test
    void deleteById() throws Exception {
        String viewName = controller.deleteById("1");
        assertEquals("redirect:/app/v1/all/garage", viewName);
        verify(garageService, times(1)).deleteById(anyLong());

        mockMvc.perform(get("/app/v1/garage/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/app/v1/all/garage"));
    }

    @Test
    void garageImageUpload() throws Exception {
        //given
        Garage garage = new Garage();
        garage.setId(1L);

        when(garageService.getById(anyLong())).thenReturn(garage);

        ArgumentCaptor<Garage> garageCaptor = ArgumentCaptor.forClass(Garage.class);

        //when
        String viewName = controller.garageImageUpload("1", model);

        //then
        assertEquals("garage/garageImageUpload", viewName);
        verify(garageService, times(1)).getById(anyLong());
        verify(model, times(1)).addAttribute(eq("garage"), garageCaptor.capture());

        mockMvc.perform(get("/app/v1/garage/1/image")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("garage/garageImageUpload"));
    }

    @Test
    void renderGarageImage() throws Exception {
        //given
        GarageDTO garageDTO = new GarageDTO();
        garageDTO.setId(1L);
        garageDTO.setImage("GarageImageStub".getBytes());

        when(garageService.findDTObyId(anyLong())).thenReturn(garageDTO);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/app/v1/garage/1/garageimage"))
                .andExpect(status().isOk()).andReturn().getResponse();

        //then
        assertEquals(garageDTO.getImage().length, response.getContentAsByteArray().length);
    }

    @Test
    void saveGarageImage() throws Exception {
        //given
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile" , "testing.txt",
                "text/plain", "GarageImageStun".getBytes());

        //when
        String viewName = controller.saveGarageImage("1", multipartFile);

        //then
        assertEquals("redirect:/app/v1/all/1/show/garage", viewName);
        verify(garageService, times(1)).saveImage(1L, multipartFile);

        mockMvc.perform(multipart("/app/v1/garage/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/app/v1/all/1/show/garage"));
    }
}