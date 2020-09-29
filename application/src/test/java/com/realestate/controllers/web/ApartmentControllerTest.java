package com.realestate.controllers.web;

import com.realestate.dto.AddressDTO;
import com.realestate.dto.ApartmentDTO;
import com.realestate.exceptions.NotFoundException;
import com.realestate.model.buildings.Apartment;
import com.realestate.model.people.Client;
import com.realestate.service.ApartmentService;
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

class ApartmentControllerTest {

    private ApartmentController controller;

    private MockMvc mockMvc;

    @Mock
    private ApartmentService apartmentService;

    @Mock
    private ClientService clientService;

    @Mock
    private Model model;

    @Mock
    private BindingResult apartmentBinding;

    @Mock
    private BindingResult addressBinding;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new ApartmentController(apartmentService, clientService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void getApartmentById() throws Exception {
        //given
        when(apartmentService.getById(anyLong())).thenReturn(new Apartment());
        ArgumentCaptor<Apartment> argumentCaptor = ArgumentCaptor.forClass(Apartment.class);

        //when
        String viewName = controller.getApartmentById("1", model);

        //then
        assertEquals("apartment/show", viewName);
        verify(apartmentService, times(1)).getById(anyLong());
        verify(model, times(1)).addAttribute(eq("apartment"), argumentCaptor.capture());

        mockMvc.perform(get("/app/v1/all/1/show/apartment"))
                .andExpect(status().isOk())
                .andExpect(view().name("apartment/show"))
                .andExpect(model().attributeExists("apartment"));
    }

    @Test
    void getApartmentByIdWhenThereIsNoThisApartmentInDB() throws Exception {

        when(apartmentService.getById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/app/v1/all/111/show/apartment"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    void getApartmentByIdWhenNumberFormatException() throws Exception {

        mockMvc.perform(get("/app/v1/all/abc/show/apartment"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    void getAllApartments() throws Exception {
        //given
        List<Apartment> apartments = new ArrayList<>(
                Collections.singletonList(new Apartment())
        );
        when(apartmentService.getApartments()).thenReturn(apartments);
        ArgumentCaptor<List<Apartment>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        //when
        String viewName = controller.getAllApartments(model);

        //then
        assertEquals("apartments", viewName);
        verify(apartmentService, times(1)).getApartments();
        verify(model, times(1)).addAttribute(eq("apartments"), argumentCaptor.capture());
        List<Apartment> setInController = argumentCaptor.getValue();
        assertEquals(1, setInController.size());

        mockMvc.perform(get("/app/v1/all/apartment"))
                .andExpect(status().isOk())
                .andExpect(view().name("apartments"))
                .andExpect(model().attributeExists("apartments"));
    }

    @Test
    void newApartment() throws Exception {
        //given
        when(clientService.getClients()).thenReturn(
                new ArrayList<>(
                        Collections.singletonList(new Client())
                )
        );
        ArgumentCaptor<ApartmentDTO> apartmentCaptor = ArgumentCaptor.forClass(ApartmentDTO.class);
        ArgumentCaptor<AddressDTO> addressCaptor = ArgumentCaptor.forClass(AddressDTO.class);
        ArgumentCaptor<Set<Client>> clientsCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.newApartment(model);

        //then
        assertEquals("apartment/apartmentForm", viewName);
        verify(clientService, times(1)).getClients();
        verify(model, times(1)).addAttribute(eq("apartment"), apartmentCaptor.capture());
        verify(model, times(1)).addAttribute(eq("address"), addressCaptor.capture());
        verify(model, times(1)).addAttribute(eq("clients"), clientsCaptor.capture());

        mockMvc.perform(get("/app/v1/apartment/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("apartment/apartmentForm"))
                .andExpect(model().attributeExists("apartment"))
                .andExpect(model().attributeExists("address"))
                .andExpect(model().attributeExists("clients"));
    }

    @Test
    void updateApartment() throws Exception {
        //given
        ApartmentDTO apartmentDTO = new ApartmentDTO();
        apartmentDTO.setAddressDTO(new AddressDTO());
        when(apartmentService.findDTObyId(anyLong())).thenReturn(apartmentDTO);
        when(clientService.getClients()).thenReturn(new ArrayList<>(
                        Collections.singletonList(new Client())
                )
        );
        ArgumentCaptor<ApartmentDTO> apartmentCaptor = ArgumentCaptor.forClass(ApartmentDTO.class);
        ArgumentCaptor<AddressDTO> addressCaptor = ArgumentCaptor.forClass(AddressDTO.class);
        ArgumentCaptor<Set<Client>> clientsCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.updateApartment("1", model);

        //then
        assertEquals("apartment/apartmentForm", viewName);
        verify(apartmentService, times(1)).findDTObyId(anyLong());
        verify(clientService, times(1)).getClients();
        verify(model, times(1)).addAttribute(eq("apartment"), apartmentCaptor.capture());
        verify(model, times(1)).addAttribute(eq("address"), addressCaptor.capture());
        verify(model, times(1)).addAttribute(eq("clients"), clientsCaptor.capture());

        mockMvc.perform(get("/app/v1/apartment/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("apartment/apartmentForm"))
                .andExpect(model().attributeExists("apartment"))
                .andExpect(model().attributeExists("address"))
                .andExpect(model().attributeExists("clients"));
    }

    @Test
    void saveOrUpdate() throws Exception {
        //given
        ApartmentDTO apartmentDTO = new ApartmentDTO();
        apartmentDTO.setId(1L);
        AddressDTO addressDTO = new AddressDTO();

        when(apartmentBinding.hasErrors()).thenReturn(false);
        when(addressBinding.hasErrors()).thenReturn(false);
        when(apartmentService.saveDTO(any())).thenReturn(apartmentDTO);

        //when
        String viewName = controller.saveOrUpdate(apartmentDTO, apartmentBinding, addressDTO, addressBinding, model);

        //then
        assertEquals("redirect:/app/v1/all/1/show/apartment", viewName);
        verify(apartmentBinding, times(1)).hasErrors();
        verify(addressBinding, times(1)).hasErrors();
        verify(apartmentService, times(1)).saveDTO(any());

        mockMvc.perform(post("/app/v1/apartment/save")
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
                .andExpect(view().name("redirect:/app/v1/all/1/show/apartment"));
    }

    @Test
    void saveOrUpdateWhenCommandValuesAreNotValid() throws Exception {
        //given
        when(apartmentBinding.hasErrors()).thenReturn(true);

        ArgumentCaptor<ApartmentDTO> apartmentCaptor = ArgumentCaptor.forClass(ApartmentDTO.class);
        ArgumentCaptor<AddressDTO> addressCaptor = ArgumentCaptor.forClass(AddressDTO.class);
        ArgumentCaptor<Set<Client>> clientsCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.saveOrUpdate(new ApartmentDTO(), apartmentBinding, new AddressDTO(), addressBinding, model);

        //then
        assertEquals("apartment/apartmentForm", viewName);
        verify(apartmentBinding, times(1)).hasErrors();
        verify(apartmentBinding, times(1)).getAllErrors();
        verify(addressBinding, times(1)).getAllErrors();
        verify(model, times(1)).addAttribute(eq("apartment"), apartmentCaptor.capture());
        verify(model, times(1)).addAttribute(eq("address"), addressCaptor.capture());
        verify(model, times(1)).addAttribute(eq("clients"), clientsCaptor.capture());
        verify(clientService, times(1)).getClients();

        mockMvc.perform(post("/app/v1/apartment/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("apartment/apartmentForm"));
    }

    @Test
    void deleteById() throws Exception {
        String viewName = controller.deleteById("1");
        assertEquals("redirect:/app/v1/all/apartment", viewName);
        verify(apartmentService, times(1)).deleteById(anyLong());

        mockMvc.perform(get("/app/v1/apartment/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/app/v1/all/apartment"));
    }

    @Test
    void apartmentImageUpload() throws Exception {
        //given
        Apartment apartment = new Apartment();
        apartment.setId(1L);

        when(apartmentService.getById(anyLong())).thenReturn(apartment);

        ArgumentCaptor<Apartment> apartmentCaptor = ArgumentCaptor.forClass(Apartment.class);

        //when
        String viewName = controller.apartmentImageUpload("1", model);
        assertEquals("apartment/apartmentImageUpload", viewName);

        verify(apartmentService, times(1)).getById(anyLong());
        verify(model, times(1)).addAttribute(eq("apartment"), apartmentCaptor.capture());

        //then
        mockMvc.perform(get("/app/v1/apartment/1/image")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("apartment/apartmentImageUpload"));
    }

    @Test
    void renderApartmentImage() throws Exception {
        //given
        ApartmentDTO apartmentDTO = new ApartmentDTO();
        apartmentDTO.setId(1L);
        apartmentDTO.setImage("ApartmentImageStub".getBytes());

        when(apartmentService.findDTObyId(anyLong())).thenReturn(apartmentDTO);

        //when
        MockHttpServletResponse response = mockMvc.perform(get("/app/v1/apartment/1/apartmentimage"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        //then
        assertEquals(apartmentDTO.getImage().length, response.getContentAsByteArray().length);
    }

    @Test
    void saveApartmentImage() throws Exception {
        //given
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile", "testing.txt",
                "text/plain", "ApartmentImage".getBytes());
        //when
        String viewName = controller.saveApartmentImage("1", multipartFile);

        //then
        assertEquals("redirect:/app/v1/all/1/show/apartment", viewName);
        verify(apartmentService, times(1)).saveImage(1L, multipartFile);

        mockMvc.perform(multipart("/app/v1/apartment/1/image").file(multipartFile))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/app/v1/all/1/show/apartment"));
    }
}