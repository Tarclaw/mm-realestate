package com.realestate.controllers.web;

import com.realestate.dto.AddressDTO;
import com.realestate.dto.HouseDTO;
import com.realestate.exceptions.NotFoundException;
import com.realestate.model.buildings.House;
import com.realestate.model.people.Client;
import com.realestate.service.ClientService;
import com.realestate.service.HouseService;
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

class HouseControllerTest {

    private HouseController controller;

    private MockMvc mockMvc;

    @Mock
    private HouseService houseService;

    @Mock
    private ClientService clientService;

    @Mock
    private Model model;

    @Mock
    private BindingResult houseBinding;

    @Mock
    private BindingResult addressBinding;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new HouseController(houseService, clientService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void getHouseById() throws Exception {
        //given
        when(houseService.getById(anyLong())).thenReturn(new House());
        ArgumentCaptor<House> houseCaptor = ArgumentCaptor.forClass(House.class);

        //when
        String viewName = controller.getHouseById("1", model);

        //then
        assertEquals("house/show", viewName);
        verify(houseService, times(1)).getById(anyLong());
        verify(model, times(1)).addAttribute(eq("house"), houseCaptor.capture());

        mockMvc.perform(get("/app/v1/all/1/show/house"))
                .andExpect(status().isOk())
                .andExpect(view().name("house/show"))
                .andExpect(model().attributeExists("house"));
    }

    @Test
    void getHouseByIdWhenThereIsNoThisHouseInDB() throws Exception {

        when(houseService.getById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/app/v1/all/111/show/house"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    void getHouseByIdWhenNumberFormatException() throws Exception {

        mockMvc.perform(get("/app/v1/all/abc/show/house"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    void getAllHouses() throws Exception {
        //given
        List<House> houses = new ArrayList<>(
                Collections.singletonList(new House())
        );

        when(houseService.getHouses()).thenReturn(houses);

        ArgumentCaptor<List<House>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        //when
        String viewName = controller.getAllHouses(model);

        //then
        assertEquals("houses", viewName);
        verify(houseService, times(1)).getHouses();
        verify(model, times(1)).addAttribute(eq("houses"), argumentCaptor.capture());

        List<House> setInController = argumentCaptor.getValue();
        assertEquals(1, setInController.size());

        mockMvc.perform(get("/app/v1/all/house"))
                .andExpect(status().isOk())
                .andExpect(view().name("houses"));
    }

    @Test
    void newHouse() throws Exception {
        //given
        when(clientService.getClients()).thenReturn(
                new ArrayList<>(
                        Collections.singletonList(new Client())
                )
        );
        ArgumentCaptor<HouseDTO> houseCaptor = ArgumentCaptor.forClass(HouseDTO.class);
        ArgumentCaptor<AddressDTO> addressCaptor = ArgumentCaptor.forClass(AddressDTO.class);
        ArgumentCaptor<Set<Client>> clientCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.newHouse(model);

        //then
        assertEquals("house/houseForm", viewName);
        verify(model, times(1)).addAttribute(eq("house"), houseCaptor.capture());
        verify(model, times(1)).addAttribute(eq("address"), addressCaptor.capture());
        verify(model, times(1)).addAttribute(eq("clients"), clientCaptor.capture());

        mockMvc.perform(get("/app/v1/house/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("house/houseForm"))
                .andExpect(model().attributeExists("house"))
                .andExpect(model().attributeExists("address"))
                .andExpect(model().attributeExists("clients"));
    }

    @Test
    void updateHouse() throws Exception {
        //given
        HouseDTO houseDTO = new HouseDTO();
        houseDTO.setAddressDTO(new AddressDTO());
        when(houseService.findDTObyId(anyLong())).thenReturn(houseDTO);
        when(clientService.getClients()).thenReturn(
                new ArrayList<>(
                        Collections.singletonList(new Client())
                )
        );
        ArgumentCaptor<HouseDTO> houseCaptor = ArgumentCaptor.forClass(HouseDTO.class);
        ArgumentCaptor<AddressDTO> addressCaptor = ArgumentCaptor.forClass(AddressDTO.class);
        ArgumentCaptor<Set<Client>> clientCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.updateHouse("1", model);

        //then
        assertEquals("house/houseForm", viewName);
        verify(houseService, times(1)).findDTObyId(anyLong());
        verify(model, times(1)).addAttribute(eq("house"), houseCaptor.capture());
        verify(model, times(1)).addAttribute(eq("address"), addressCaptor.capture());
        verify(model, times(1)).addAttribute(eq("clients"), clientCaptor.capture());

        mockMvc.perform(get("/app/v1/house/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("house/houseForm"))
                .andExpect(model().attributeExists("house"))
                .andExpect(model().attributeExists("address"))
                .andExpect(model().attributeExists("clients"));
    }

    @Test
    void saveOrUpdate() throws Exception {
        //given
        HouseDTO houseDTO = new HouseDTO();
        houseDTO.setId(1L);
        AddressDTO addressDTO = new AddressDTO();

        when(houseBinding.hasErrors()).thenReturn(false);
        when(addressBinding.hasErrors()).thenReturn(false);
        when(houseService.saveDTO(any())).thenReturn(houseDTO);

        //when
        String viewName = controller.saveOrUpdate(houseDTO, houseBinding, addressDTO, addressBinding, model);

        //then
        assertEquals("redirect:/app/v1/all/1/show/house", viewName);
        verify(houseBinding, times(1)).hasErrors();
        verify(addressBinding, times(1)).hasErrors();
        verify(houseService, times(1)).saveDTO(any());

        mockMvc.perform(post("/app/v1/house/save")
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
                .andExpect(view().name("redirect:/app/v1/all/1/show/house"));
    }

    @Test
    void saveOrUpdateWhenCommandValuesAreNotValid() throws Exception {
        //given
        when(houseBinding.hasErrors()).thenReturn(true);

        ArgumentCaptor<HouseDTO> houseCaptor = ArgumentCaptor.forClass(HouseDTO.class);
        ArgumentCaptor<AddressDTO> addressCaptor = ArgumentCaptor.forClass(AddressDTO.class);
        ArgumentCaptor<Set<Client>> clientsCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.saveOrUpdate(new HouseDTO(), houseBinding, new AddressDTO(), addressBinding, model);

        //then
        assertEquals("house/houseForm", viewName);
        verify(houseBinding, times(1)).hasErrors();
        verify(houseBinding, times(1)).getAllErrors();
        verify(addressBinding, times(1)).getAllErrors();
        verify(model, times(1)).addAttribute(eq("house"), houseCaptor.capture());
        verify(model, times(1)).addAttribute(eq("address"), addressCaptor.capture());
        verify(model, times(1)).addAttribute(eq("clients"), clientsCaptor.capture());
        verify(clientService, times(1)).getClients();

        mockMvc.perform(post("/app/v1/house/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("house/houseForm"));
    }

    @Test
    void deleteById() throws Exception {
        String viewName = controller.deleteById("1");
        assertEquals("redirect:/app/v1/all/house", viewName);
        verify(houseService, times(1)).deleteById(anyLong());

        mockMvc.perform(get("/app/v1/house/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/app/v1/all/house"));
    }

    @Test
    void houseImageUpload() throws Exception {
        //given
        House house = new House();
        house.setId(1L);

        when(houseService.getById(anyLong())).thenReturn(house);

        ArgumentCaptor<House> houseCaptor = ArgumentCaptor.forClass(House.class);

        //when
        String viewName = controller.houseImageUpload("1", model);

        //then
        assertEquals("house/houseImageUpload", viewName);
        verify(houseService, times(1)).getById(anyLong());
        verify(model, times(1)).addAttribute(eq("house"), houseCaptor.capture());

        mockMvc.perform(get("/app/v1/house/1/image")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("house/houseImageUpload"));
    }

    @Test
    void renderHouseImage() throws Exception {
        //given
        HouseDTO houseDTO = new HouseDTO();
        houseDTO.setId(1L);
        houseDTO.setImage("HouseImageStub".getBytes());

        when(houseService.findDTObyId(anyLong())).thenReturn(houseDTO);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/app/v1/house/1/houseimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        //then
        assertEquals(houseDTO.getImage().length, response.getContentAsByteArray().length);
    }

    @Test
    void saveHouseImage() throws Exception {
        //given
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile","testing.txt",
                "text/plain", "HouseImageStub".getBytes());

        //when
        String viewName = controller.saveHouseImage("1", multipartFile);

        //then
        assertEquals("redirect:/app/v1/all/1/show/house", viewName);
        verify(houseService, times(1)).saveImage(1L, multipartFile);

        mockMvc.perform(multipart("/app/v1/house/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/app/v1/all/1/show/house"));
    }
}