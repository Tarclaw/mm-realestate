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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientServiceImpl.class);

    private final ClientRepository clientRepository;
    private final RealEstateAgentRepository agentRepository;
    private final ClientMapper mapper;

    public ClientServiceImpl(ClientRepository clientRepository,
                             RealEstateAgentRepository agentRepository,
                             ClientMapper mapper) {
        this.clientRepository = clientRepository;
        this.agentRepository = agentRepository;
        this.mapper = mapper;
        LOGGER.info("New instance of ClientServiceImpl created");
    }

    @Override
    public Client getById(final Long id) {
        LOGGER.trace("Enter and execute 'ClientServiceImpl.getById(final Long id)' method");
        return clientRepository.findClientByIdWithFacilitiesAndAgents(id)
                         .orElseThrow(
                                      () -> {
                                          LOGGER.warn("We don't have Client with id= " + id);
                                          return new NotFoundException("Please chose different Client.");
                                      }
                         );
    }

    @Override
    public List<Client> getClients() {
        LOGGER.trace("Enter in 'ClientServiceImpl.getClients()' method");

        List<Client> clients = new ArrayList<>();
        clientRepository.findAll().iterator().forEachRemaining(clients :: add);
        LOGGER.debug("Find all Clients from DB and add them to HashSet");

        LOGGER.trace("'ClientServiceImpl.getClients()' executed successfully.");
        return clients;
    }

    @Override
    @Transactional
    public ClientDTO findDTObyId(final Long id) {
        LOGGER.trace("Enter and execute 'ClientServiceImpl.findCommandById(final Long id)' method");
        return mapper.clientToClientDTO(getById(id));
    }

    @Override
    @Transactional
    public ClientDTO saveDetached(final ClientDTO clientDTO) {
        LOGGER.trace("Enter in 'ClientServiceImpl.saveDetached(final ClientCommand clientCommand, final FacilityCommand facilityCommand)' method");

        RealEstateAgent agent = agentRepository.findById(clientDTO.getAgentId()).orElseThrow(() -> {
            LOGGER.warn("We don't have Agent with id= " + clientDTO.getAgentId());
            return new NotFoundException("We don't have this Agent. Please choose another one.");
        });

        Client detachedClient = mapper.clientDTOtoClient(clientDTO);
        detachedClient.addAgent(agent);

        Client savedClient = clientRepository.save(detachedClient);

        LOGGER.debug("We save Client with id= " + savedClient.getId());
        LOGGER.trace("'ClientServiceImpl.saveDetached(final ClientCommand clientCommand, final FacilityCommand facilityCommand)' executed successfully.");
        return mapper.clientToClientDTO(savedClient);
    }

    @Override
    @Transactional
    public ClientDTO saveAttached(final ClientDTO clientDTO) {
        LOGGER.trace("Enter in 'ClientServiceImpl.saveAttached(final ClientCommand command)' method");

        Client detachedClient = mapper.clientDTOtoClient(clientDTO);
        Client savedClient = clientRepository.save(detachedClient);

        LOGGER.debug("We save Client with id= " + savedClient.getId());
        LOGGER.trace("'ClientServiceImpl.saveAttached(final ClientCommand command)' executed successfully.");
        return mapper.clientToClientDTO(savedClient);
    }

    @Override
    @Transactional
    public void deleteById(final Long id) {
        LOGGER.trace("Enter in 'ClientServiceImpl.deleteById(final Long id)' method");

        Client client = clientRepository.findClientByIdWithFacilitiesAndAgents(id).orElseThrow(() -> {
            LOGGER.warn("We don't have Client with id= " + id);
            return new NotFoundException("We don't have this Client. Please choose another one.");
        });

        List<Facility> facilities = new ArrayList<>(client.getFacilities());
        facilities.forEach(client::removeFacility);
        LOGGER.debug("Remove all Facilities from Client to delete.");

        List<RealEstateAgent> agents = new ArrayList<>(client.getRealEstateAgents());
        agents.forEach(client::removeAgent);
        LOGGER.debug("Remove all Agents from Client to delete.");

        clientRepository.deleteById(id);
        LOGGER.debug("Delete Client with id= " + id);

        LOGGER.trace("'ClientServiceImpl.deleteById(final Long id)' executed successfully.");
    }

    @Override
    @Transactional
    public List<ClientDTO> getClientsDTO() {
        return getClients()
                .stream()
                .map(mapper::clientToClientDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClientDTO patchClient(Long id, ClientDTO clientDTO) {
        return clientRepository.findById(id).map(client -> {
            if (clientDTO.getCustomerRequirements() != null) {
                client.setCustomerRequirements(clientDTO.getCustomerRequirements());
            }
            if (clientDTO.getFirstName() != null) {
                client.setFirstName(clientDTO.getFirstName());
            }
            if (clientDTO.getLastName() != null) {
                client.setLastName(clientDTO.getLastName());
            }
            if (clientDTO.getLogin() != null) {
                client.setLogin(clientDTO.getLogin());
            }
            if (clientDTO.getPassword() != null) {
                client.setPassword(clientDTO.getPassword());
            }
            if (clientDTO.getContactDTO() != null) {
                if (clientDTO.getContactDTO().getEmail() != null) {
                    client.getContact().setEmail(clientDTO.getContactDTO().getEmail());
                }
                if (clientDTO.getContactDTO().getSkype() != null) {
                    client.getContact().setSkype(clientDTO.getContactDTO().getSkype());
                }
                if (clientDTO.getContactDTO().getMobileNumber() != null) {
                    client.getContact().setMobileNumber(clientDTO.getContactDTO().getMobileNumber());
                }
            }
            return mapper.clientToClientDTO(clientRepository.save(client));
        }).orElseThrow(RuntimeException::new);
    }
}
