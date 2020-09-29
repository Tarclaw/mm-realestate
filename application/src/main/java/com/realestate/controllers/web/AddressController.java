package com.realestate.controllers.web;

import com.realestate.dto.AddressDTO;
import com.realestate.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/app/v1")
public class AddressController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressController.class);

    private final AddressService service;

    public AddressController(AddressService service) {
        this.service = service;
        LOGGER.info("New instance of AddressController created.");
    }

    @GetMapping("/all/{id}/show/address")
    public String getAddressById(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'getAddressById(@PathVariable String id, Model model)' method");

        model.addAttribute("address", service.getById(Long.valueOf(id)));
        LOGGER.debug("add address attribute to model");

        LOGGER.trace("'getAddressById(@PathVariable String id, Model model)' executed successfully.");
        return "address/show";
    }

    @GetMapping("/all/address")
    public String getAddresses(Model model) {
        LOGGER.trace("Enter in 'getAddresses(Model model)' method");

        model.addAttribute("addresses", service.getAddresses());
        LOGGER.debug("add addresses attribute to model");

        LOGGER.trace("'getAddresses(Model model)' executed successfully.");
        return "addresses";
    }

    @GetMapping("/address/{id}/update")
    public String updateAddress(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'updateAddress(@PathVariable String id, Model model)' method");

        model.addAttribute("address", service.findDTObyId(Long.valueOf(id)));
        LOGGER.debug("add addresses attribute to model");

        LOGGER.trace("'updateAddress(@PathVariable String id, Model model)' executed successfully.");
        return "address/addressForm";
    }

    @PostMapping("/address")
    public String saveOrUpdate(@Valid @ModelAttribute("address") AddressDTO addressDTO,
                               BindingResult bindingResult, Model model) {
        LOGGER.trace("Enter in 'saveOrUpdate(AddressCommand addressCommand, BindingResult bindingResult, Model model)' method");

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));

            model.addAttribute("address", addressDTO);
            LOGGER.debug("add address attribute to model");

            return "address/addressForm";
        }
        AddressDTO savedAddress = service.saveAddressDTO(addressDTO);

        LOGGER.trace("'saveOrUpdate(AddressCommand addressCommand, BindingResult bindingResult, Model model)' executed successfully.");
        return "redirect:/app/v1/all/" + savedAddress.getId() + "/show/address";
    }

    @GetMapping("/address/{id}/delete")
    public String deleteById(@PathVariable String id) {
        LOGGER.trace("Enter in 'deleteById(@PathVariable String id)' method");

        service.deleteById(Long.valueOf(id));

        LOGGER.trace("'deleteById(@PathVariable String id)' executed successfully.");
        return "redirect:/app/v1/all/address";
    }
}
