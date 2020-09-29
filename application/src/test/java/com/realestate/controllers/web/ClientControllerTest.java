package com.realestate.controllers.web;

import com.realestate.dto.AddressDTO;
import com.realestate.dto.ClientDTO;
import com.realestate.dto.ContactDTO;
import com.realestate.exceptions.NotFoundException;
import com.realestate.model.people.Client;
import com.realestate.model.people.RealEstateAgent;
import com.realestate.service.ClientService;
import com.realestate.service.RealEstateAgentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class ClientControllerTest {

    private ClientController controller;

    private MockMvc mockMvc;

    @Mock
    private ClientService clientService;

    @Mock
    private RealEstateAgentService agentService;

    @Mock
    private Model model;

    @Mock
    private BindingResult clientResult;

    @Mock
    private BindingResult contactResult;

    @Mock
    private BindingResult addressResult;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new ClientController(clientService, agentService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void getClientById() throws Exception {
        //given
        when(clientService.getById(anyLong())).thenReturn(new Client());
        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);

        //when
        String viewName = controller.getClientById("1", model);

        //then
        assertEquals("client/show", viewName);
        verify(clientService, times(1)).getById(anyLong());
        verify(model, times(1)).addAttribute(eq("client"), clientCaptor.capture());

        mockMvc.perform(get("/client/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("client/show"))
                .andExpect(model().attributeExists("client"));
    }

    @Test
    void getClientByIdWhenThereIsNoThisClientInDB() throws Exception {

        when(clientService.getById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/client/111/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    void getClientByIdWhenNumberFormatException() throws Exception {

        mockMvc.perform(get("/client/abc/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    void getAllClients() throws Exception {
        //given
        List<Client> clients = new ArrayList<>(
                Collections.singletonList(new Client())
        );
        when(clientService.getClients()).thenReturn(clients);

        ArgumentCaptor<List<Client>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        //when
        String viewName = controller.getAllClients(model);

        //then
        assertEquals("client", viewName);
        verify(clientService, times(1)).getClients();
        verify(model, times(1)).addAttribute(eq("clients"), argumentCaptor.capture());

        List<Client> setInController = argumentCaptor.getValue();
        assertEquals(1, setInController.size());

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(view().name("client"));
    }

    @Test
    void newClient() throws Exception {
        //given
        ArgumentCaptor<ClientDTO> commandCaptor = ArgumentCaptor.forClass(ClientDTO.class);

        //when
        String viewName = controller.newClient(model);

        //then
        assertEquals("client/clientEmptyForm", viewName);
        verify(model, times(1)).addAttribute(eq("client"), commandCaptor.capture());

        mockMvc.perform(get("/client/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("client/clientEmptyForm"))
                .andExpect(model().attributeExists("client"));
    }

    @Test
    void updateClient() throws Exception {
        //given
        when(clientService.findDTObyId(anyLong())).thenReturn(new ClientDTO());
        ArgumentCaptor<ClientDTO> clientCaptor = ArgumentCaptor.forClass(ClientDTO.class);
        ArgumentCaptor<ContactDTO> contactCaptor = ArgumentCaptor.forClass(ContactDTO.class);

        //when
        String viewName = controller.updateClient("1", model);

        //then
        assertEquals("client/clientForm", viewName);
        verify(clientService, times(1)).findDTObyId(anyLong());
        verify(model, times(1)).addAttribute(eq("client"), clientCaptor.capture());
        verify(model, times(1)).addAttribute(eq("contact"), contactCaptor.capture());

        mockMvc.perform(get("/client/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("client/clientForm"))
                .andExpect(model().attributeExists("client"));
    }

    @Test
    void saveNew() throws Exception {
        //given
        ClientDTO clientDTO = new ClientDTO();
        ContactDTO contactDTO = new ContactDTO();
        AddressDTO addressDTO = new AddressDTO();

        when(clientResult.hasErrors()).thenReturn(false);
        when(contactResult.hasErrors()).thenReturn(false);
        when(addressResult.hasErrors()).thenReturn(false);
        when(clientService.saveDetached(clientDTO)).thenReturn(clientDTO);

        //when
        String viewName = controller.saveNew(clientDTO, clientResult,
                contactDTO, contactResult,
                addressDTO, addressResult, model);

        //then
        assertEquals("redirect:/clients", viewName);
        verify(clientResult, times(1)).hasErrors();
        verify(contactResult, times(1)).hasErrors();
        verify(addressResult, times(1)).hasErrors();
        verify(clientService, times(1)).saveDetached(any());

        mockMvc.perform(post("/client/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "First Name")
                .param("lastName", "Last Name")
                .param("login", "login")
                .param("password", "Password")
                .param("email", "some@email.com")
                .param("skype", "Skype")
                .param("mobileNumber", "+1 078 777 88 99")
                .param("customerRequirements", "customer requires smth")
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
                .andExpect(view().name("redirect:/clients"));
    }

    @Test
    void saveNewWhenCommandVariablesAreNotValid() throws Exception {
        //given
        ClientDTO clientDTO = new ClientDTO();
        ContactDTO contactDTO = new ContactDTO();
        AddressDTO addressDTO = new AddressDTO();

        when(clientResult.hasErrors()).thenReturn(true);

        ArgumentCaptor<ClientDTO> clientCaptor = ArgumentCaptor.forClass(ClientDTO.class);
        ArgumentCaptor<ContactDTO> contactCaptor = ArgumentCaptor.forClass(ContactDTO.class);
        ArgumentCaptor<AddressDTO> addressCaptor = ArgumentCaptor.forClass(AddressDTO.class);
        ArgumentCaptor<Set<RealEstateAgent>> agentsCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.saveNew(clientDTO, clientResult,
                contactDTO, contactResult,
                addressDTO, addressResult, model);

        //then
        assertEquals("client/clientEmptyForm", viewName);
        verify(clientResult, times(1)).hasErrors();
        verify(clientResult, times(1)).getAllErrors();
        verify(contactResult, times(1)).getAllErrors();
        verify(addressResult, times(1)).getAllErrors();
        verify(model, times(1)).addAttribute(eq("client"), clientCaptor.capture());
        verify(model, times(1)).addAttribute(eq("contact"), contactCaptor.capture());
        verify(model, times(1)).addAttribute(eq("address"), addressCaptor.capture());
        verify(model, times(1)).addAttribute(eq("realEstateAgents"), agentsCaptor.capture());
        verify(agentService, times(1)).getRealEstateAgents();

        mockMvc.perform(post("/client/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        )
                .andExpect(status().isOk())
                .andExpect(view().name("client/clientEmptyForm"));
    }

    @Test
    void updateExisting() throws Exception {
        //given
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        when(clientResult.hasErrors()).thenReturn(false);
        when(clientService.saveAttached(any())).thenReturn(clientDTO);

        //when
        String viewName = controller.updateExisting(clientDTO, clientResult,
                new ContactDTO(), contactResult, model);

        //then
        assertEquals("redirect:/client/1/show", viewName);
        verify(clientResult, times(1)).hasErrors();
        verify(clientService, times(1)).saveAttached(any());

        mockMvc.perform(post("/client/update")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("firstName", "First Name")
                .param("lastName", "Last Name")
                .param("login", "login")
                .param("password", "Password")
                .param("email", "some@email.com")
                .param("skype", "Skype")
                .param("mobileNumber", "+1 078 777 88 99")
                .param("customerRequirements", "customer requires smth")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/client/1/show"));
    }

    @Test
    void deleteById() throws Exception {
        String viewName = controller.deleteById("1");

        assertEquals("redirect:/clients", viewName);
        verify(clientService, times(1)).deleteById(anyLong());

        mockMvc.perform(get("/client/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/clients"));
    }
}