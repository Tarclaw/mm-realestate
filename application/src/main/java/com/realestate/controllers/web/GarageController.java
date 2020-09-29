package com.realestate.controllers.web;

import com.realestate.dto.AddressDTO;
import com.realestate.dto.GarageDTO;
import com.realestate.exceptions.ImageCorruptedException;
import com.realestate.service.ClientService;
import com.realestate.service.GarageService;
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
public class GarageController {

    private final GarageService garageService;
    private final ClientService clientService;

    private static final Logger LOGGER = LoggerFactory.getLogger(GarageController.class);

    public GarageController(GarageService garageService, ClientService clientService) {
        this.garageService = garageService;
        this.clientService = clientService;
        LOGGER.info("New instance of GarageController created.");
    }

    @GetMapping("/all/{id}/show/garage")
    public String getGarageById(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'getGarageById(@PathVariable String id, Model model)' method");

        model.addAttribute("garage", garageService.getById(Long.valueOf(id)));
        LOGGER.debug("add garage attribute to model");

        LOGGER.trace("'getGarageById(@PathVariable String id, Model model)' executed successfully.");
        return "garage/show";
    }

    @GetMapping("/all/garage")
    public String getAllGarages(Model model) {
        LOGGER.trace("Enter in 'getAllGarages(Model model)' method");

        model.addAttribute("garages", garageService.getGarages());
        LOGGER.debug("add garages attribute to model");

        LOGGER.trace("'getAllGarages(Model model)' executed successfully.");
        return "garages";
    }

    @GetMapping("/garage/new")
    public String newGarage(Model model) {
        LOGGER.trace("Enter in 'getAllGarages(Model model)' method");

        model.addAttribute("garage", new GarageDTO());
        LOGGER.debug("add garage attribute to model");

        model.addAttribute("address", new AddressDTO());
        LOGGER.debug("add address attribute to model");

        model.addAttribute("clients", clientService.getClients());
        LOGGER.debug("add clients attribute to model");

        LOGGER.trace("'getAllGarages(Model model)' executed successfully.");
        return "garage/garageForm";
    }

    @GetMapping("/garage/{id}/update")
    public String updateGarage(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'updateGarage(@PathVariable String id, Model model)' method");

        GarageDTO garage = garageService.findDTObyId(Long.valueOf(id));
        model.addAttribute("garage", garage);
        LOGGER.debug("add garage attribute to model");

        model.addAttribute("address", garage.getAddressDTO());
        LOGGER.debug("add address attribute to model");

        model.addAttribute("clients", clientService.getClients());
        LOGGER.debug("add clients attribute to model");

        LOGGER.trace("'updateGarage(@PathVariable String id, Model model)' executed successfully.");
        return "garage/garageForm";
    }

    @PostMapping("/garage/save")
    public String saveOrUpdate(@Valid @ModelAttribute("garage") GarageDTO gatageDTO, BindingResult garageBinding,
                               @Valid @ModelAttribute("address") AddressDTO addressDTO, BindingResult addressBinding,
                               Model model) {
        LOGGER.trace("Enter in 'saveNew(gatageDTO, garageBinding, addressCommand, addressBinding, Model model' method");

        if (garageBinding.hasErrors() || addressBinding.hasErrors()) {

            garageBinding.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));
            addressBinding.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));

            model.addAttribute("garage", gatageDTO);
            LOGGER.debug("add garage attribute to model");

            model.addAttribute("address", addressDTO);
            LOGGER.debug("add address attribute to model");

            model.addAttribute("clients", clientService.getClients());
            LOGGER.debug("add clients attribute to model");

            return "garage/garageForm";
        }

        gatageDTO.setAddressDTO(addressDTO);
        GarageDTO savedGarage = garageService.saveDTO(gatageDTO);

        LOGGER.trace("'saveNew(garageCommand, garageBinding, addressCommand, addressBinding, Model model' executed successfully.");
        return "redirect:/app/v1/all/" + savedGarage.getId() + "/show/garage";
    }

    @GetMapping("/garage/{id}/delete")
    public String deleteById(@PathVariable String id) {
        LOGGER.trace("Enter in 'deleteById(@PathVariable String id)' method");

        garageService.deleteById(Long.valueOf(id));

        LOGGER.trace("'deleteById(@PathVariable String id)' executed successfully.");
        return "redirect:/app/v1/all/garage";
    }

    @GetMapping("/garage/{id}/image")
    public String garageImageUpload(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'garageImageUpload(@PathVariable String id, Model model)' method");

        model.addAttribute("garage", garageService.getById(Long.valueOf(id)));
        LOGGER.debug("add garage attribute to model");

        LOGGER.trace("'garageImageUpload(@PathVariable String id, Model model)' executed successfully.");
        return "garage/garageImageUpload";
    }

    @GetMapping("/garage/{id}/garageimage")
    public void renderGarageImage(@PathVariable String id, HttpServletResponse response) {
        LOGGER.trace("Enter in 'renderGarageImage(@PathVariable String id, HttpServletResponse response)' method");

        GarageDTO garage = garageService.findDTObyId(Long.valueOf(id));

        if (garage.getImage() != null) {
            response.setContentType("image/jpeg");
            InputStream inputStream = new ByteArrayInputStream(garage.getImage());
            try {
                IOUtils.copy(inputStream, response.getOutputStream());
            } catch (IOException e) {
                LOGGER.error("Attention can't render garage image!!!", e);
                throw new ImageCorruptedException(e.getMessage());
            }
        }

        LOGGER.trace("'renderGarageImage(@PathVariable String id, HttpServletResponse response)' executed successfully.");
    }

    @PostMapping("/garage/{id}/image")
    public String saveGarageImage(@PathVariable String id, @RequestParam("imagefile") MultipartFile multipartFile) {
        LOGGER.trace("Enter in 'saveGarageImage(String id, MultipartFile multipartFile)' method");

        garageService.saveImage(Long.valueOf(id), multipartFile);

        LOGGER.trace("'saveGarageImage(String id, MultipartFile multipartFile)' executed successfully.");
        return "redirect:/app/v1/all/" + id + "/show/garage";
    }
}
