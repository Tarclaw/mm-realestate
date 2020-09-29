package com.realestate.controllers.web;

import com.realestate.dto.AddressDTO;
import com.realestate.dto.BasementDTO;
import com.realestate.exceptions.ImageCorruptedException;
import com.realestate.service.BasementService;
import com.realestate.service.ClientService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/app/v1")
public class BasementController {

    private final BasementService basementService;
    private final ClientService clientService;

    private static final Logger LOGGER = LoggerFactory.getLogger(BasementController.class);

    public BasementController(BasementService basementService, ClientService clientService) {
        this.basementService = basementService;
        this.clientService = clientService;
        LOGGER.info("New instance of BasementController created.");
    }

    @GetMapping("/all/{id}/show/basement")
    public String getBasementById(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'getBasementById(@PathVariable String id, Model model)' method");

        model.addAttribute("basement", basementService.getById(Long.valueOf(id)));
        LOGGER.debug("add basement attribute to model");

        LOGGER.trace("'getBasementById(@PathVariable String id, Model model)' executed successfully.");
        return "basement/show";
    }

    @GetMapping("/all/basement")
    public String getAllBasements(Model model) {
        LOGGER.trace("Enter in 'getAllBasements(Model model)' method");

        model.addAttribute("basements", basementService.getBasements());
        LOGGER.debug("add basements attribute to model");

        LOGGER.trace("'getAllBasements(Model model)' executed successfully.");
        return "basements";
    }

    @GetMapping("/basement/new")
    public String newBasement(Model model) {
        LOGGER.trace("Enter in 'newBasement(Model model)' method");

        model.addAttribute("basement", new BasementDTO());
        LOGGER.debug("add basement attribute to model");

        model.addAttribute("address", new AddressDTO());
        LOGGER.debug("add address attribute to model");

        model.addAttribute("clients", clientService.getClients());
        LOGGER.debug("add clients attribute to model");

        LOGGER.trace("'newBasement(Model model)' executed successfully.");
        return "basement/basementForm";
    }

    @GetMapping("/basement/{id}/update")
    public String updateBasement(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'updateBasement(@PathVariable String id, Model model)' method");

        BasementDTO basement = basementService.findDTObyId(Long.valueOf(id));
        model.addAttribute("basement", basement);
        LOGGER.debug("add basement attribute to model");

        model.addAttribute("address", basement.getAddressDTO());
        LOGGER.debug("add address attribute to model");

        model.addAttribute("clients", clientService.getClients());
        LOGGER.debug("add clients attribute to model");

        LOGGER.trace("'updateBasement(@PathVariable String id, Model model)' executed successfully.");
        return "basement/basementForm";
    }

    @PostMapping("/basement/save")
    public String saveOrUpdate(@Valid @ModelAttribute("basement") BasementDTO basementDTO, BindingResult basementBinding,
                               @Valid @ModelAttribute("address") AddressDTO addressDTO, BindingResult addressBinding, Model model) {
        LOGGER.trace("Enter in 'saveNew(basementCommand, basementBinding, addressCommand, addressBinding, Model model)' method");

        if (basementBinding.hasErrors() || addressBinding.hasErrors()) {

            basementBinding.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));
            addressBinding.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));

            model.addAttribute("basement", basementDTO);
            LOGGER.debug("add basement attribute to model");

            model.addAttribute("address", addressDTO);
            LOGGER.debug("add address attribute to model");

            model.addAttribute("clients", clientService.getClients());
            LOGGER.debug("add clients attribute to model");

            return "basement/basementForm";
        }

        basementDTO.setAddressDTO(addressDTO);
        BasementDTO savedBasement = basementService.saveDTO(basementDTO);

        LOGGER.trace("'saveNew(basementCommand, basementBinding, addressCommand, addressBinding, Model model)' executed successfully.");
        return "redirect:/app/v1/all/" + savedBasement.getId() + "/show/basement";
    }

    @GetMapping("/basement/{id}/delete")
    public String deleteById(@PathVariable String id) {
        LOGGER.trace("Enter in 'deleteById(@PathVariable String id)' method");

        basementService.deleteById(Long.valueOf(id));

        LOGGER.trace("'deleteById(@PathVariable String id)' executed successfully.");
        return "redirect:/app/v1/all/basement";
    }

    @GetMapping("/basement/{id}/image")
    public String basementImageUpload(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'basementImageUpload(@PathVariable String id, Model model)' method");

        model.addAttribute("basement", basementService.getById(Long.valueOf(id)));
        LOGGER.debug("add basement attribute to model");

        LOGGER.trace("'basementImageUpload(@PathVariable String id, Model model)' executed successfully.");
        return "basement/basementImageUpload";
    }

    @GetMapping("/basement/{id}/basementimage")
    public void renderBasementImage(@PathVariable String id, HttpServletResponse response) {
        LOGGER.trace("Enter in 'renderBasementImage(@PathVariable String id, HttpServletResponse response)' method");
        BasementDTO basement = basementService.findDTObyId(Long.valueOf(id));

        if (basement.getImage() != null) {
            response.setContentType("image/jpeg");
            InputStream inputStream = new ByteArrayInputStream(basement.getImage());
            try {
                IOUtils.copy(inputStream, response.getOutputStream());
            } catch (IOException e) {
                LOGGER.error("Attention can't render basement image!!!", e);
                throw new ImageCorruptedException(e.getMessage());
            }
        }
        LOGGER.trace("'renderBasementImage(@PathVariable String id, HttpServletResponse response)' executed successfully.");
    }

    @PostMapping("/basement/{id}/image")
    public String saveBasementImage(@PathVariable String id, @RequestParam("imagefile") MultipartFile multipartFile) {
        LOGGER.trace("Enter in 'saveBasementImage(String id, MultipartFile multipartFile)' method");

        basementService.saveImage(Long.valueOf(id), multipartFile);

        LOGGER.trace("'saveBasementImage(String id, MultipartFile multipartFile)' executed successfully.");
        return "redirect:/app/v1/all/" + id + "/show/basement";
    }
}
