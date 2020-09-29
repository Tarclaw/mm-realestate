package com.realestate.controllers.web;

import com.realestate.dto.AddressDTO;
import com.realestate.dto.ClientDTO;
import com.realestate.dto.ContactDTO;
import com.realestate.service.ClientService;
import com.realestate.service.RealEstateAgentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class ClientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientController.class);

    private final ClientService clientService;
    private final RealEstateAgentService agentService;

    public ClientController(ClientService clientService, RealEstateAgentService agentService) {
        this.clientService = clientService;
        this.agentService = agentService;
        LOGGER.info("New instance of ClientController created.");
    }

    @GetMapping("/client/{id}/show")
    public String getClientById(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'getClientById(@PathVariable String id, Model model)' method");

        model.addAttribute("client", clientService.getById(Long.valueOf(id)));
        LOGGER.debug("add client attribute to model");

        LOGGER.trace("'getClientById(@PathVariable String id, Model model)' executed successfully.");
        return "client/show";
    }

    @GetMapping("/clients")
    public String getAllClients(Model model) {
        LOGGER.trace("Enter in 'getAllClients(Model model)' method");

        model.addAttribute("clients", clientService.getClients());
        LOGGER.debug("add clients attribute to model");

        LOGGER.trace("'getAllClients(Model model)' executed successfully.");
        return "client";
    }

    @GetMapping("/client/new")
    public String newClient(Model model) {
        LOGGER.trace("Enter in 'newClient(Model model)' method");

        model.addAttribute("client", new ClientDTO());
        LOGGER.debug("add client attribute to model");

        model.addAttribute("contact", new ContactDTO());
        LOGGER.debug("add facility attribute to model");

        model.addAttribute("address", new AddressDTO());
        LOGGER.debug("add address attribute to model");

        model.addAttribute("realEstateAgents", agentService.getRealEstateAgents());
        LOGGER.debug("add realEstateAgents attribute to model");

        LOGGER.trace("'newClient(Model model)' executed successfully.");
        return "client/clientEmptyForm";
    }

    @GetMapping("/client/{id}/update")
    public String updateClient(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'updateClient(@PathVariable String id, Model model)' method");

        ClientDTO client = clientService.findDTObyId(Long.valueOf(id));

        model.addAttribute("client", client);
        LOGGER.debug("add client attribute to model");

        model.addAttribute("contact", client.getContactDTO());
        LOGGER.debug("add contact attribute to model");

        LOGGER.trace("'updateClient(@PathVariable String id, Model model)' executed successfully.");
        return "client/clientForm";
    }

    @PostMapping("/client/save")
    public String saveNew(@Valid @ModelAttribute("client") ClientDTO clientDTO, BindingResult clientBindingResult,
                          @Valid @ModelAttribute("contact") ContactDTO contactDTO, BindingResult contactBindingResult,
                          @Valid @ModelAttribute("address") AddressDTO addressDTO, BindingResult addressBindingResult, Model model) {
        LOGGER.trace("Enter in 'saveNew(clientCommand, clientBindingResult, facilityCommand, facilityBindingResult, " +
                     "addressCommand, addressBindingResult, Model model)' method");

        if (clientBindingResult.hasErrors() || contactBindingResult.hasErrors() || addressBindingResult.hasErrors()) {
            clientBindingResult.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));
            contactBindingResult.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));
            addressBindingResult.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));

            model.addAttribute("client", clientDTO);
            LOGGER.debug("add client attribute to model");

            model.addAttribute("contact", contactDTO);
            LOGGER.debug("add facility attribute to model");

            model.addAttribute("address", addressDTO);
            LOGGER.debug("add address attribute to model");

            model.addAttribute("realEstateAgents", agentService.getRealEstateAgents());
            LOGGER.debug("add realEstateAgents attribute to model");

            return "client/clientEmptyForm";
        }

        clientDTO.setContactDTO(contactDTO);
        clientService.saveDetached(clientDTO);

        LOGGER.trace("'saveNew(clientCommand, clientBindingResult, facilityCommand, facilityBindingResult, " +
                     "addressCommand, addressBindingResult, Model model)' executed successfully.");
        return "redirect:/clients";
    }

    @PostMapping("/client/update")
    public String updateExisting(@Valid @ModelAttribute("client") ClientDTO clientDTO, BindingResult clientBindingResult,
                                 @Valid @ModelAttribute("contact") ContactDTO contactDTO, BindingResult contactBindingResult,
                                 Model model) {
        LOGGER.trace("Enter in 'updateExisting(ClientCommand clientCommand, BindingResult bindingResult, Model model)' method");

        if (clientBindingResult.hasErrors() || contactBindingResult.hasErrors()) {
            clientBindingResult.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));
            contactBindingResult.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));

            ClientDTO clientWithAgentsAndFacilities = clientService.findDTObyId(clientDTO.getId());
            clientDTO.setRealEstateAgents(clientWithAgentsAndFacilities.getRealEstateAgents());
            clientDTO.setFacilities(clientWithAgentsAndFacilities.getFacilities());

            model.addAttribute("client", clientDTO);
            LOGGER.debug("add client attribute to model");

            model.addAttribute("contact", clientDTO.getContactDTO());
            LOGGER.debug("add contact attribute to model");

            return "client/clientForm";
        }

        clientDTO.setContactDTO(contactDTO);
        ClientDTO savedCommand = clientService.saveAttached(clientDTO);

        LOGGER.trace("'updateExisting(ClientCommand clientCommand, BindingResult bindingResult, Model model)' executed successfully.");
        return "redirect:/client/" + savedCommand.getId() + "/show";
    }

    @GetMapping("/client/{id}/delete")
    public String deleteById(@PathVariable String id) {
        LOGGER.trace("Enter in 'deleteById(@PathVariable String id)' method");

        clientService.deleteById(Long.valueOf(id));

        LOGGER.trace("'deleteById(@PathVariable String id)' executed successfully.");
        return "redirect:/clients";
    }
}
