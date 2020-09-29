package com.realestate.controllers.web;

import com.realestate.dto.ContactDTO;
import com.realestate.dto.RealEstateAgentDTO;
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
public class RealEstateAgentController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RealEstateAgentController.class);

    private final RealEstateAgentService agentService;
    private final ClientService clientService;

    public RealEstateAgentController(RealEstateAgentService agentService, ClientService clientService) {
        this.agentService = agentService;
        this.clientService = clientService;
        LOGGER.info("New instance of RealEstateAgentController created.");
    }

    @GetMapping("/agent/{id}/show")
    public String getRealEstateAgentById(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'getRealEstateAgentById(@PathVariable String id, Model model)' method");

        model.addAttribute("realEstateAgent", agentService.getById(Long.valueOf(id)));
        LOGGER.debug("add realEstateAgent attribute to model");

        LOGGER.trace("'getRealEstateAgentById(@PathVariable String id, Model model)' executed successfully.");
        return "realEstateAgent/show";
    }

    @GetMapping("/agents")
    public String getAllRealEstateAgents(Model model) {
        LOGGER.trace("Enter in 'getAllRealEstateAgents(Model model)' method");

        model.addAttribute("realEstateAgents", agentService.getRealEstateAgents());
        LOGGER.debug("add realEstateAgents attribute to model");

        LOGGER.trace("'getAllRealEstateAgents(Model model)' executed successfully.");
        return "realEstateAgent";
    }

    @GetMapping("/realEstateAgent/new")
    public String newAgent(Model model) {
        LOGGER.trace("Enter in 'newAgent(Model model)' method");

        model.addAttribute("realEstateAgent", new RealEstateAgentDTO());
        LOGGER.debug("add realEstateAgents attribute to model");

        model.addAttribute("contact", new ContactDTO());
        LOGGER.debug("add contact attribute to model");

        model.addAttribute("clients", clientService.getClients());
        LOGGER.debug("add clients attribute to model");

        LOGGER.trace("'newAgent(Model model)' executed successfully.");
        return "realEstateAgent/realEstateAgentForm";
    }

    @GetMapping("/realEstateAgent/{id}/update")
    public String updateAgent(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'updateAgent(@PathVariable String id, Model model)' method");

        RealEstateAgentDTO agentDTO = agentService.findDTObyId(Long.valueOf(id));

        model.addAttribute("realEstateAgent", agentDTO);
        LOGGER.debug("add realEstateAgents attribute to model");

        model.addAttribute("contact", agentDTO.getContactDTO());
        LOGGER.debug("add contact attribute to model");

        model.addAttribute("clients", clientService.getClients());
        LOGGER.debug("add clients attribute to model");

        LOGGER.trace("'updateAgent(@PathVariable String id, Model model)' executed successfully.");
        return "realEstateAgent/realEstateAgentForm";
    }

    @PostMapping("/realEstateAgent")
    public String saveOrUpdate(@Valid @ModelAttribute("realEstateAgent") RealEstateAgentDTO agentDTO, BindingResult agentBinding,
                               @Valid @ModelAttribute("contact") ContactDTO contactDTO, BindingResult contactBinding,
                               Model model) {
        LOGGER.trace("Enter in 'saveOrUpdate(RealEstateAgentCommand command, BindingResult bindingResult, Model model)' method");

        if (agentBinding.hasErrors() || contactBinding.hasErrors()) {

            agentBinding.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));
            contactBinding.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));

            model.addAttribute("realEstateAgent", agentDTO);
            LOGGER.debug("add realEstateAgents attribute to model");

            model.addAttribute("contact", contactDTO);
            LOGGER.debug("add contact attribute to model");

            model.addAttribute("clients", clientService.getClients());
            LOGGER.debug("add clients attribute to model");

            return "realEstateAgent/realEstateAgentForm";
        }
        agentDTO.setContactDTO(contactDTO);
        RealEstateAgentDTO savedCommand = agentService.saveRealEstateAgentDTO(agentDTO);

        LOGGER.trace("'saveOrUpdate(RealEstateAgentCommand command, BindingResult bindingResult, Model model)' executed successfully.");
        return "redirect:/agent/" + savedCommand.getId() + "/show";
    }

    @GetMapping("/realEstateAgent/{id}/delete")
    public String deleteById(@PathVariable String id) {
        LOGGER.trace("Enter in 'deleteById(@PathVariable String id)' method");

        agentService.deleteById(Long.valueOf(id));

        LOGGER.trace("'deleteById(@PathVariable String id)' executed successfully.");
        return "redirect:/agents";
    }
}
