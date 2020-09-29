package com.realestate.controllers.web;

import com.realestate.dto.AddressDTO;
import com.realestate.dto.HouseDTO;
import com.realestate.exceptions.ImageCorruptedException;
import com.realestate.service.ClientService;
import com.realestate.service.HouseService;
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
public class HouseController {

    private final HouseService houseService;
    private final ClientService clientService;

    private static final Logger LOGGER = LoggerFactory.getLogger(HouseController.class);

    public HouseController(HouseService houseService, ClientService clientService) {
        this.houseService = houseService;
        this.clientService = clientService;
        LOGGER.info("New instance of HouseController created.");
    }

    @GetMapping("/all/{id}/show/house")
    public String getHouseById(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'getHouseById(@PathVariable String id, Model model)' method");

        model.addAttribute("house", houseService.getById(Long.valueOf(id)));
        LOGGER.debug("add house attribute to model");

        LOGGER.trace("'getHouseById(@PathVariable String id, Model model)' executed successfully.");
        return "house/show";
    }

    @GetMapping("/all/house")
    public String getAllHouses(Model model) {
        LOGGER.trace("Enter in 'getAllHouses(Model model)' method");

        model.addAttribute("houses", houseService.getHouses());
        LOGGER.debug("add houses attribute to model");

        LOGGER.trace("'getAllHouses(Model model)' executed successfully.");
        return "houses";
    }

    @GetMapping("/house/new")
    public String newHouse(Model model) {
        LOGGER.trace("Enter in 'newHouse(Model model)' method");

        model.addAttribute("house", new HouseDTO());
        LOGGER.debug("add house attribute to model");

        model.addAttribute("address", new AddressDTO());
        LOGGER.debug("add address attribute to model");

        model.addAttribute("clients", clientService.getClients());
        LOGGER.debug("add clients attribute to model");

        LOGGER.trace("'newHouse(Model model)' executed successfully.");
        return "house/houseForm";
    }

    @GetMapping("/house/{id}/update")
    public String updateHouse(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'updateHouse(@PathVariable String id, Model model)' method");

        HouseDTO house = houseService.findDTObyId(Long.valueOf(id));
        model.addAttribute("house", house);
        LOGGER.debug("add house attribute to model");

        model.addAttribute("address", house.getAddressDTO());
        LOGGER.debug("add address attribute to model");

        model.addAttribute("clients", clientService.getClients());
        LOGGER.debug("add clients attribute to model");

        LOGGER.trace("'updateHouse(@PathVariable String id, Model model)' executed successfully.");
        return "house/houseForm";
    }

    @PostMapping("/house/save")
    public String saveOrUpdate(@Valid @ModelAttribute("house") HouseDTO houseDTO, BindingResult houseBinding,
                               @Valid @ModelAttribute("address") AddressDTO addressDTO, BindingResult addressBinding,
                               Model model) {
        LOGGER.trace("Enter in 'saveNew(houseCommand, houseBinding, addressCommand, addressBinding, model' method");

        if (houseBinding.hasErrors() || addressBinding.hasErrors()) {

            houseBinding.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));
            addressBinding.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));

            model.addAttribute("house", houseDTO);
            LOGGER.debug("add house attribute to model");

            model.addAttribute("address", addressDTO);
            LOGGER.debug("add address attribute to model");

            model.addAttribute("clients", clientService.getClients());
            LOGGER.debug("add clients attribute to model");

            return "house/houseForm";
        }

        houseDTO.setAddressDTO(addressDTO);
        HouseDTO savedHouse = houseService.saveDTO(houseDTO);

        LOGGER.trace("'saveNew(houseCommand, houseBinding, addressCommand, addressBinding, model' executed successfully.");
        return "redirect:/app/v1/all/" + savedHouse.getId() + "/show/house";
    }

    @GetMapping("/house/{id}/delete")
    public String deleteById(@PathVariable String id) {
        LOGGER.trace("Enter in 'deleteById(@PathVariable String id)' method");

        houseService.deleteById(Long.valueOf(id));

        LOGGER.trace("'deleteById(@PathVariable String id)' executed successfully.");
        return "redirect:/app/v1/all/house";
    }

    @GetMapping("/house/{id}/image")
    public String houseImageUpload(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'houseImageUpload(@PathVariable String id, Model model)' method");

        model.addAttribute("house", houseService.getById(Long.valueOf(id)));
        LOGGER.debug("add house attribute to model");

        LOGGER.trace("'houseImageUpload(@PathVariable String id, Model model)' executed successfully.");
        return "house/houseImageUpload";
    }

    @GetMapping("/house/{id}/houseimage")
    public void renderHouseImage(@PathVariable String id, HttpServletResponse response) {
        LOGGER.trace("Enter in 'renderHouseImage(@PathVariable String id, HttpServletResponse response)' method");

        HouseDTO house = houseService.findDTObyId(Long.valueOf(id));

        if (house.getImage() != null) {
            response.setContentType("image/jpeg");
            InputStream inputStream = new ByteArrayInputStream(house.getImage());
            try {
                IOUtils.copy(inputStream, response.getOutputStream());
            } catch (IOException e) {
                LOGGER.error("Attention can't render house image!!!", e);
                throw new ImageCorruptedException(e.getMessage());
            }
        }

        LOGGER.trace("'renderHouseImage(@PathVariable String id, HttpServletResponse response)' executed successfully.");
    }

    @PostMapping("/house/{id}/image")
    public String saveHouseImage(@PathVariable String id, @RequestParam("imagefile") MultipartFile file) {
        LOGGER.trace("Enter in 'saveHouseImage(String id, MultipartFile file)' method");

        houseService.saveImage(Long.valueOf(id), file);

        LOGGER.trace("'saveHouseImage(String id, MultipartFile file)' executed successfully.");
        return "redirect:/app/v1/all/" + id + "/show/house";
    }
}
