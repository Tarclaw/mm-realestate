package com.realestate.service.implementation;

import com.realestate.dto.RealEstateAgentDTO;
import com.realestate.exceptions.NotFoundException;
import com.realestate.mapper.RealEstateAgentMapper;
import com.realestate.model.people.RealEstateAgent;
import com.realestate.repositories.ClientRepository;
import com.realestate.repositories.RealEstateAgentRepository;
import com.realestate.service.RealEstateAgentService;
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

class RealEstateAgentServiceImplTest {

    private RealEstateAgentService service;

    @Mock
    private RealEstateAgentRepository agentRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private RealEstateAgentMapper agentMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new RealEstateAgentServiceImpl(agentRepository, clientRepository, agentMapper);
    }

    @Test
    void getById() {
        //given
        RealEstateAgent source = new RealEstateAgent();
        source.setId(1L);
        when(agentRepository.findRealEstateAgentsByIdWithEntities(anyLong())).thenReturn(Optional.of(source));

        //when
        RealEstateAgent agent = service.getById(1L);

        //then
        assertNotNull(agent);
        assertEquals(source.getId(), agent.getId());
        verify(agentRepository, times(1)).findRealEstateAgentsByIdWithEntities(anyLong());
    }

    @Test
    void getByIdExceptionHandling() {
        assertThrows(NotFoundException.class, () -> service.getById(anyLong()));
    }

    @Test
    void getRealEstateAgents() {
        when(service.getRealEstateAgents()).
                thenReturn(new ArrayList<>(Collections.singletonList(new RealEstateAgent())));

        List<RealEstateAgent> agents = service.getRealEstateAgents();

        assertEquals(1, agents.size());
        verify(agentRepository, times(1)).findAll();
    }

    @Test
    void findDTObyId() {
        //given
        RealEstateAgentDTO agentDTO = new RealEstateAgentDTO();
        agentDTO.setId(1L);
        when(agentRepository.findRealEstateAgentsByIdWithEntities(anyLong())).thenReturn(Optional.of(new RealEstateAgent()));
        when(agentMapper.realEstateAgentToRealEstateAgentDTO(any())).thenReturn(agentDTO);

        //when
        RealEstateAgentDTO command = service.findDTObyId(1L);

        //then
        assertNotNull(command);
        assertEquals(agentDTO.getId(), command.getId());
        verify(agentRepository, times(1)).findRealEstateAgentsByIdWithEntities(anyLong());
        verify(agentMapper, times(1)).realEstateAgentToRealEstateAgentDTO(any());
    }

    @Test
    void deleteById() {
        service.deleteById(anyLong());
        verify(agentRepository, times(1)).deleteById(anyLong());
    }
}