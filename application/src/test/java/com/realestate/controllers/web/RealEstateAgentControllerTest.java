package com.realestate.controllers.web;

import com.realestate.dto.ContactDTO;
import com.realestate.dto.RealEstateAgentDTO;
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

class RealEstateAgentControllerTest {

    private RealEstateAgentController controller;

    private MockMvc mockMvc;

    @Mock
    private RealEstateAgentService agentService;

    @Mock
    private ClientService clientService;

    @Mock
    private Model model;

    @Mock
    private BindingResult agentBindingResult;

    @Mock
    private BindingResult contactBindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        controller = new RealEstateAgentController(agentService, clientService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ControllerExceptionHandler())
                .build();
    }

    @Test
    void getRealEstateAgentById() throws Exception {
        //given
        when(agentService.getById(anyLong())).thenReturn(new RealEstateAgent());
        ArgumentCaptor<RealEstateAgent> agentCaptor = ArgumentCaptor.forClass(RealEstateAgent.class);

        //when
        String viewName = controller.getRealEstateAgentById("1", model);

        //then
        assertEquals("realEstateAgent/show", viewName);
        verify(agentService, times(1)).getById(anyLong());
        verify(model, times(1)).addAttribute(eq("realEstateAgent"), agentCaptor.capture());

        mockMvc.perform(get("/agent/1/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("realEstateAgent/show"))
                .andExpect(model().attributeExists("realEstateAgent"));
    }

    @Test
    void getAgentByIdWhenThereIsNoThisAgentInDB() throws Exception {

        when(agentService.getById(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/agent/111/show"))
                .andExpect(status().isNotFound())
                .andExpect(view().name("404error"));
    }

    @Test
    void getAgentByIdWhenNumberFormatException() throws Exception {

        mockMvc.perform(get("/agent/abc/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }

    @Test
    void getAllRealEstateAgents() throws Exception {
        //given
        List<RealEstateAgent> agents = new ArrayList<>(
                Collections.singletonList(new RealEstateAgent())
        );

        when(agentService.getRealEstateAgents()).thenReturn(agents);

        ArgumentCaptor<List<RealEstateAgent>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        //when
        String viewName = controller.getAllRealEstateAgents(model);

        //then
        assertEquals("realEstateAgent", viewName);
        verify(agentService, times(1)).getRealEstateAgents();
        verify(model, times(1)).addAttribute(eq("realEstateAgents"), argumentCaptor.capture());

        List<RealEstateAgent> setInController = argumentCaptor.getValue();
        assertEquals(1, setInController.size());

        mockMvc.perform(get("/agents"))
                .andExpect(status().isOk())
                .andExpect(view().name("realEstateAgent"));
    }

    @Test
    void newAgent() throws Exception {
        //given
        when(clientService.getClients()).thenReturn(
                new ArrayList<>(
                        Collections.singletonList(new Client())
                )
        );
        ArgumentCaptor<RealEstateAgentDTO> commandCaptor = ArgumentCaptor.forClass(RealEstateAgentDTO.class);
        ArgumentCaptor<Set<Client>> clientsCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.newAgent(model);

        //then
        assertEquals("realEstateAgent/realEstateAgentForm", viewName);
        verify(clientService, times(1)).getClients();
        verify(model, times(1)).addAttribute(eq("realEstateAgent"), commandCaptor.capture());
        verify(model, times(1)).addAttribute(eq("clients"), clientsCaptor.capture());
        mockMvc.perform(get("/realEstateAgent/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("realEstateAgent/realEstateAgentForm"))
                .andExpect(model().attributeExists("realEstateAgent"))
                .andExpect(model().attributeExists("clients"));
    }

    @Test
    void updateAgent() throws Exception {
        //given
        List<Client> clients = new ArrayList<>(Collections.singletonList(new Client()));
        when(agentService.findDTObyId(1L)).thenReturn(new RealEstateAgentDTO());
        when(clientService.getClients()).thenReturn(clients);
        ArgumentCaptor<RealEstateAgentDTO> commandCaptor = ArgumentCaptor.forClass(RealEstateAgentDTO.class);
        ArgumentCaptor<Set<Client>> clientsCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.updateAgent("1", model);

        //then
        assertEquals("realEstateAgent/realEstateAgentForm", viewName);
        verify(agentService, times(1)).findDTObyId(anyLong());
        verify(clientService, times(1)).getClients();
        verify(model, times(1)).addAttribute(eq("realEstateAgent"), commandCaptor.capture());
        verify(model, times(1)).addAttribute(eq("clients"), clientsCaptor.capture());

        mockMvc.perform(get("/realEstateAgent/1/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("realEstateAgent/realEstateAgentForm"))
                .andExpect(model().attributeExists("realEstateAgent"))
                .andExpect(model().attributeExists("clients"));
    }

    @Test
    void saveOrUpdate() throws Exception {
        //given
        RealEstateAgentDTO agentDTO = new RealEstateAgentDTO();
        agentDTO.setId(1L);
        when(agentBindingResult.hasErrors()).thenReturn(false);
        when(agentService.saveRealEstateAgentDTO(any())).thenReturn(agentDTO);

        //when
        String viewName = controller.saveOrUpdate(agentDTO, agentBindingResult,
                new ContactDTO(), contactBindingResult, model);

        //then
        assertEquals("redirect:/agent/1/show", viewName);
        verify(agentBindingResult, times(1)).hasErrors();
        verify(agentService, times(1)).saveRealEstateAgentDTO(any());

        mockMvc.perform(post("/realEstateAgent")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("firstName", "First Name")
                .param("lastName", "Last Name")
                .param("login", "login")
                .param("password", "Password")
                .param("email", "some@email.com")
                .param("skype", "Skype")
                .param("mobileNumber", "+1 078 777 88 99")
                .param("salary", "1 000 000")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/agent/1/show"));
    }

    @Test
    void saveOrUpdateWhenCommandVariablesAreNotValid() throws Exception {
        //given
        when(agentBindingResult.hasErrors()).thenReturn(true);

        ArgumentCaptor<RealEstateAgentDTO> agentCaptor = ArgumentCaptor.forClass(RealEstateAgentDTO.class);
        ArgumentCaptor<Set<Client>> clientsCaptor = ArgumentCaptor.forClass(Set.class);

        //when
        String viewName = controller.saveOrUpdate(new RealEstateAgentDTO(), agentBindingResult,
                new ContactDTO(), contactBindingResult, model);

        //then
        assertEquals("realEstateAgent/realEstateAgentForm", viewName);
        verify(agentBindingResult, times(1)).hasErrors();
        verify(agentBindingResult, times(1)).getAllErrors();
        verify(model, times(1)).addAttribute(eq("realEstateAgent"), agentCaptor.capture());
        verify(model, times(1)).addAttribute(eq("clients"), clientsCaptor.capture());
        verify(clientService, times(1)).getClients();

        mockMvc.perform(post("/realEstateAgent")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(view().name("realEstateAgent/realEstateAgentForm"));
    }

    @Test
    void deleteById() throws Exception {
        String viewName = controller.deleteById("1");

        assertEquals("redirect:/agents", viewName);
        verify(agentService, times(1)).deleteById(anyLong());

        mockMvc.perform(get("/realEstateAgent/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/agents"));
    }
}