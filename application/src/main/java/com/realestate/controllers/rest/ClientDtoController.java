package com.realestate.controllers.rest;

import com.realestate.dto.ClientDTO;
import com.realestate.dto.ClientListDTO;
import com.realestate.service.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/clients")
public class ClientDtoController {

    private final ClientService service;

    public ClientDtoController(ClientService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ClientListDTO getListOfClients() {
        return new ClientListDTO(service.getClientsDTO());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClientDTO getClientById(@PathVariable Long id) {
        return service.findDTObyId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDTO createNewClient(@RequestBody ClientDTO clientDTO) {
        return service.saveDetached(clientDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClientDTO updateClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        clientDTO.setId(id);
        return service.saveAttached(clientDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClientDTO patchClient(@PathVariable Long id, @RequestBody ClientDTO clientDTO) {
        return service.patchClient(id, clientDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteClient(@PathVariable Long id) {
        service.deleteById(id);
    }
}
