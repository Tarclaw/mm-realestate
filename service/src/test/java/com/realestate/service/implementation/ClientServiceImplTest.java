package com.realestate.service.implementation;

import com.realestate.dto.ClientDTO;
import com.realestate.exceptions.NotFoundException;
import com.realestate.mapper.ClientMapper;
import com.realestate.model.buildings.Facility;
import com.realestate.model.people.Client;
import com.realestate.model.people.RealEstateAgent;
import com.realestate.repositories.ClientRepository;
import com.realestate.repositories.RealEstateAgentRepository;
import com.realestate.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClientServiceImplTest {

    private ClientService service;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private RealEstateAgentRepository agentRepository;

    @Mock
    private ClientMapper clientMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new ClientServiceImpl(clientRepository, agentRepository, clientMapper);
    }

    @Test
    void getById() {
        //given
        Client source = new Client();
        source.setId(1L);
        when(clientRepository.findClientByIdWithFacilitiesAndAgents(anyLong())).thenReturn(Optional.of(source));

        //when
        Client client = service.getById(1L);

        //then
        assertNotNull(client);
        assertEquals(1L, client.getId());
        verify(clientRepository, times(1)).findClientByIdWithFacilitiesAndAgents(anyLong());
    }

    @Test
    void getByIdExceptionHandling() {
        assertThrows(NotFoundException.class, () -> service.getById(anyLong()));
    }

    @Test
    void getClients() {
        when(service.getClients()).
                thenReturn(new ArrayList<>(Collections.singletonList(new Client())));

        List<Client> clients = service.getClients();

        assertEquals(1, clients.size());
        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void findDTObyId() {
        //given
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        when(clientRepository.findClientByIdWithFacilitiesAndAgents(anyLong())).thenReturn(Optional.of(new Client()));
        when(clientMapper.clientToClientDTO(any())).thenReturn(clientDTO);

        //when
        ClientDTO command = service.findDTObyId(1L);

        //then
        assertNotNull(command);
        assertEquals(1L, command.getId());
        verify(clientRepository, times(1)).findClientByIdWithFacilitiesAndAgents(anyLong());
        verify(clientMapper, times(1)).clientToClientDTO(any());
    }

    @Test
    void saveDetached() {
        //todo
    }

    @Test
    void saveAttached() {
        //todo
    }

    @Test
    void deleteById() {
        //given
        Set<Facility> facilities = new HashSet<>();
        facilities.add(new Facility());

        Client source = new Client();
        source.setFacilities(facilities);

        Set<Client> clients = new HashSet<>();
        clients.add(source);

        RealEstateAgent agent = new RealEstateAgent();
        agent.setClients(clients);

        Set<RealEstateAgent> agents = new HashSet<>();
        agents.add(agent);

        source.setRealEstateAgents(agents);

        when(clientRepository.findClientByIdWithFacilitiesAndAgents(anyLong())).thenReturn(Optional.of(source));

        //when
        service.deleteById(anyLong());

        //then
        verify(clientRepository, times(1)).findClientByIdWithFacilitiesAndAgents(anyLong());
        verify(clientRepository, times(1)).deleteById(anyLong());
    }
}