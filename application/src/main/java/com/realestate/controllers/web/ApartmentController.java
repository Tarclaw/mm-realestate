package com.realestate.controllers.web;

import com.realestate.dto.AddressDTO;
import com.realestate.dto.ApartmentDTO;
import com.realestate.exceptions.ImageCorruptedException;
import com.realestate.service.ApartmentService;
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
public class ApartmentController {

    private final ApartmentService apartmentService;
    private final ClientService clientService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApartmentController.class);

    public ApartmentController(ApartmentService apartmentService, ClientService clientService) {
        this.apartmentService = apartmentService;
        this.clientService = clientService;
        LOGGER.info("New instance of ApartmentController created.");
    }

    @GetMapping("/all/{id}/show/apartment")
    public String getApartmentById(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'getApartmentById(@PathVariable String id, Model model)' method");

        model.addAttribute("apartment", apartmentService.getById(Long.valueOf(id)));
        LOGGER.debug("add apartment attribute to model");

        LOGGER.trace("'getApartmentById(@PathVariable String id, Model model)' executed successfully.");
        return "apartment/show";
    }

    @GetMapping("/all/apartment")
    public String getAllApartments(Model model) {
        LOGGER.trace("Enter in 'getAllApartments(Model model)' method");

        model.addAttribute("apartments", apartmentService.getApartments());
        LOGGER.debug("add apartments attribute to model");

        LOGGER.trace("'getAllApartments(Model model)' executed successfully.");
        return "apartments";
    }

    @GetMapping("/apartment/new")
    public String newApartment(Model model) {
        LOGGER.trace("Enter in 'newApartment(Model model)' method");

        model.addAttribute("apartment", new ApartmentDTO());
        LOGGER.debug("add apartment attribute to model");

        model.addAttribute("address", new AddressDTO());
        LOGGER.debug("add address attribute to model");

        model.addAttribute("clients", clientService.getClients());
        LOGGER.debug("add clients attribute to model");

        LOGGER.trace("'newApartment(Model model)' executed successfully.");
        return "apartment/apartmentForm";
    }

    @GetMapping("/apartment/{id}/update")
    public String updateApartment(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'updateApartment(@PathVariable String id, Model model)' method");

        ApartmentDTO apartment = apartmentService.findDTObyId(Long.valueOf(id));
        model.addAttribute("apartment", apartment);
        LOGGER.debug("add apartment attribute to model");

        model.addAttribute("address", apartment.getAddressDTO());
        LOGGER.debug("add address attribute to model");

        model.addAttribute("clients", clientService.getClients());
        LOGGER.debug("add clients attribute to model");

        LOGGER.trace("'updateApartment(@PathVariable String id, Model model)' executed successfully.");
        return "apartment/apartmentForm";
    }

    @PostMapping("/apartment/save")
    public String saveOrUpdate(@Valid @ModelAttribute("apartment") ApartmentDTO apartmentDTO, BindingResult apartmentBinding,
                               @Valid @ModelAttribute("address") AddressDTO addressDTO, BindingResult addressBinding, Model model) {
        LOGGER.trace("Enter in 'saveNew(apartmentCommand, apartmentBinding, addressCommand, addressBinding, model)' method");

        if (apartmentBinding.hasErrors() || addressBinding.hasErrors()) {

            apartmentBinding.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));
            addressBinding.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));

            model.addAttribute("apartment", apartmentDTO);
            LOGGER.debug("add apartment attribute to model");

            model.addAttribute("address", addressDTO);
            LOGGER.debug("add address attribute to model");

            model.addAttribute("clients", clientService.getClients());
            LOGGER.debug("add clients attribute to model");

            return "apartment/apartmentForm";
        }

        apartmentDTO.setAddressDTO(addressDTO);
        ApartmentDTO savedApartment = apartmentService.saveDTO(apartmentDTO);

        LOGGER.trace("'saveNew(apartmentCommand, apartmentBinding, addressCommand, addressBinding, model)' executed successfully.");
        return "redirect:/app/v1/all/" + savedApartment.getId() + "/show/apartment";
    }

    @GetMapping("/apartment/{id}/delete")
    public String deleteById(@PathVariable String id) {
        LOGGER.trace("Enter in 'deleteById(@PathVariable String id)' method");

        apartmentService.deleteById(Long.valueOf(id));

        LOGGER.trace("'deleteById(@PathVariable String id)' executed successfully.");
        return "redirect:/app/v1/all/apartment";
    }

    @GetMapping("/apartment/{id}/image")
    public String apartmentImageUpload(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'apartmentImageUpload(@PathVariable String id, Model model)' method");

        model.addAttribute("apartment", apartmentService.getById(Long.valueOf(id)));
        LOGGER.debug("add apartment attribute to model");

        LOGGER.trace("'apartmentImageUpload(@PathVariable String id, Model model)' executed successfully.");
        return "apartment/apartmentImageUpload";
    }

    @GetMapping("/apartment/{id}/apartmentimage")
    public void renderApartmentImage(@PathVariable String id, HttpServletResponse response) {
        LOGGER.trace("Enter in 'renderApartmentImage(@PathVariable String id, HttpServletResponse response)' method");
        ApartmentDTO apartment = apartmentService.findDTObyId(Long.valueOf(id));

        if (apartment.getImage() != null) {
            response.setContentType("image/jpeg");
            InputStream inputStream = new ByteArrayInputStream(apartment.getImage());
            try {
                IOUtils.copy(inputStream, response.getOutputStream());
            } catch (IOException e) {
                LOGGER.error("Attention can't render apartment image!!!", e);
                throw new ImageCorruptedException(e.getMessage());
            }
        }
        LOGGER.trace("'renderApartmentImage(@PathVariable String id, HttpServletResponse response)' executed successfully.");
    }

    @PostMapping("/apartment/{id}/image")
    public String saveApartmentImage(@PathVariable String id, @RequestParam("imagefile") MultipartFile file) {
        LOGGER.trace("Enter in 'saveApartmentImage(String id, MultipartFile file)' method");

        apartmentService.saveImage(Long.valueOf(id), file);

        LOGGER.trace("'saveApartmentImage(String id, MultipartFile file)' executed successfully.");
        return "redirect:/app/v1/all/" + id + "/show/apartment";
    }
}
