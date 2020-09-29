package com.realestate.service;

import com.realestate.dto.ClientDTO;
import com.realestate.model.people.Client;

import java.util.List;

public interface ClientService {

    Client getById(Long id);

    List<Client> getClients();

    ClientDTO findDTObyId(Long id);

    ClientDTO saveAttached(ClientDTO clientDTO);

    ClientDTO saveDetached(ClientDTO clientDTO);

    void deleteById(Long id);

    List<ClientDTO> getClientsDTO();

    ClientDTO patchClient(Long id, ClientDTO clientDTO);
}
