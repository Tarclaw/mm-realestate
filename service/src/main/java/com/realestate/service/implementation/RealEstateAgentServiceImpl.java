package com.realestate.service.implementation;

import com.realestate.dto.RealEstateAgentDTO;
import com.realestate.exceptions.NotFoundException;
import com.realestate.mapper.RealEstateAgentMapper;
import com.realestate.model.people.Client;
import com.realestate.model.people.RealEstateAgent;
import com.realestate.repositories.ClientRepository;
import com.realestate.repositories.RealEstateAgentRepository;
import com.realestate.service.RealEstateAgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RealEstateAgentServiceImpl implements RealEstateAgentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RealEstateAgentServiceImpl.class);

    private final RealEstateAgentRepository agentRepository;
    private final ClientRepository clientRepository;
    private final RealEstateAgentMapper mapper;

    public RealEstateAgentServiceImpl(RealEstateAgentRepository agentRepository,
                                      ClientRepository clientRepository,
                                      RealEstateAgentMapper mapper) {
        this.agentRepository = agentRepository;
        this.clientRepository = clientRepository;
        this.mapper = mapper;
        LOGGER.info("New instance of RealEstateAgentServiceImpl created");
    }

    @Override
    public RealEstateAgent getById(final Long id) {
        LOGGER.trace("Enter and execute 'RealEstateAgentServiceImpl.getById(final Long id)' method");
        return agentRepository.findRealEstateAgentsByIdWithEntities(id)
                         .orElseThrow(
                                      () -> {
                                          LOGGER.warn("We don't have Agent with id= " + id);
                                          return new NotFoundException("Please chose different Agent");
                                      }
                         );
    }

    @Override
    public List<RealEstateAgent> getRealEstateAgents() {
        LOGGER.trace("Enter in 'RealEstateAgentServiceImpl.getRealEstateAgents()' method");

        List<RealEstateAgent> agents = new ArrayList<>();
        agentRepository.findAll().iterator().forEachRemaining(agents :: add);
        LOGGER.debug("Find all Agents from DB and add them to HashSet");

        LOGGER.trace("'RealEstateAgentServiceImpl.getRealEstateAgents()' executed successfully.");
        return agents;
    }

    @Override
    @Transactional
    public RealEstateAgentDTO findDTObyId(final Long id) {
        LOGGER.trace("Enter 'RealEstateAgentServiceImpl.findCommandById(final Long id)' method");

        RealEstateAgent agent = getById(id);

        List<Long> clientIds = new ArrayList<>();
        agent.getClients().forEach(client -> clientIds.add(client.getId()));
        LOGGER.debug("build  clientIds list");

        RealEstateAgentDTO agentDTO = mapper.realEstateAgentToRealEstateAgentDTO(agent);
        agentDTO.setClientIds(clientIds);
        LOGGER.debug("add clientIds list to agentDTO");

        LOGGER.trace("'RealEstateAgentServiceImpl.findCommandById(final Long id)' executed successfully");
        return agentDTO;
    }

    @Override
    @Transactional
    public RealEstateAgentDTO saveRealEstateAgentDTO(RealEstateAgentDTO agentDTO) {
        LOGGER.trace("Enter in 'RealEstateAgentServiceImpl.saveRealEstateAgentCommand(RealEstateAgentCommand command)' method");

        Set<Client> clients = new HashSet<>();
        agentDTO.getClientIds().forEach(id -> clients.add(clientRepository.findById(id).orElseThrow(() -> {
            LOGGER.warn("We don't have Client with id= " + id);
            return new NotFoundException("We don't have this Client. Please choose another one.");
        })));

        RealEstateAgent detachedAgent = mapper.realEstateAgentDTOtoRealEstateAgent(agentDTO);
        detachedAgent.setClients(clients);

        RealEstateAgent savedAgent = agentRepository.save(detachedAgent);

        LOGGER.debug("We save Agent with id= " + savedAgent.getId());
        LOGGER.trace("'RealEstateAgentServiceImpl.saveRealEstateAgentCommand(RealEstateAgentCommand command)' executed successfully.");
        return mapper.realEstateAgentToRealEstateAgentDTO(savedAgent);
    }

    @Override
    public void deleteById(final Long id) {
        LOGGER.trace("Enter and execute 'RealEstateAgentServiceImpl.deleteById(final Long id)' method");

        agentRepository.deleteById(id);
        LOGGER.debug("Delete Agent with id= " + id);

        LOGGER.trace("'RealEstateAgentServiceImpl.deleteById(final Long id)' executed successfully.");
    }

    @Override
    public List<RealEstateAgentDTO> getAgentsDTO() {
        return getRealEstateAgents()
                .stream()
                .map(mapper::realEstateAgentToRealEstateAgentDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RealEstateAgentDTO patchAgent(Long id, RealEstateAgentDTO agentDTO) {
        return agentRepository.findById(id).map(agent -> {
            if (agentDTO.getSalary() != null) {
                agent.setSalary(agentDTO.getSalary());
            }
            if (agentDTO.getHiredDate() != null) {
                agent.setHiredDate(agentDTO.getHiredDate());
            }
            if (agentDTO.getQuitDate() != null) {
                agent.setQuitDate(agentDTO.getQuitDate());
            }
            if (agentDTO.getFirstName() != null) {
                agent.setFirstName(agentDTO.getFirstName());
            }
            if (agentDTO.getLastName() != null) {
                agent.setLastName(agentDTO.getLastName());
            }
            if (agentDTO.getLogin() != null) {
                agent.setLogin(agentDTO.getLogin());
            }
            if (agentDTO.getPassword() != null) {
                agent.setPassword(agentDTO.getPassword());
            }
            if (agentDTO.getContactDTO() != null) {
                if (agentDTO.getContactDTO().getEmail() != null) {
                    agent.getContact().setEmail(agentDTO.getContactDTO().getEmail());
                }
                if (agentDTO.getContactDTO().getSkype() != null) {
                    agent.getContact().setSkype(agentDTO.getContactDTO().getSkype());
                }
                if (agentDTO.getContactDTO().getMobileNumber() != null) {
                    agent.getContact().setMobileNumber(agentDTO.getContactDTO().getMobileNumber());
                }
            }
            return mapper.realEstateAgentToRealEstateAgentDTO(agentRepository.save(agent));
        }).orElseThrow(RuntimeException::new);
    }
}
