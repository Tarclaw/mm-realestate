package com.realestate.controllers.web;

import com.realestate.dto.AddressDTO;
import com.realestate.dto.StorageDTO;
import com.realestate.exceptions.ImageCorruptedException;
import com.realestate.service.ClientService;
import com.realestate.service.StorageService;
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
public class StorageController {

    private final StorageService storageService;
    private final ClientService clientService;

    private static final Logger LOGGER = LoggerFactory.getLogger(StorageController.class);

    public StorageController(StorageService storageService, ClientService clientService) {
        this.storageService = storageService;
        this.clientService = clientService;
        LOGGER.info("New instance of StorageController created.");
    }

    @GetMapping("/all/{id}/show/storage")
    public String getStorageById(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'getStorageById(@PathVariable String id, Model model)' method");

        model.addAttribute("storage", storageService.getById(Long.valueOf(id)));
        LOGGER.debug("add storage attribute to model");

        LOGGER.trace("'getStorageById(@PathVariable String id, Model model)' executed successfully.");
        return "storage/show";
    }

    @GetMapping("/all/storage")
    public String getAllStorages(Model model) {
        LOGGER.trace("Enter in 'getAllStorages(Model model)' method");

        model.addAttribute("storages", storageService.getStorages());
        LOGGER.debug("add storages attribute to model");

        LOGGER.trace("'getAllStorages(Model model)' executed successfully.");
        return "storages";
    }

    @GetMapping("/storage/new")
    public String newStorage(Model model) {
        LOGGER.trace("Enter in 'newStorage(Model model)' method");

        model.addAttribute("storage", new StorageDTO());
        LOGGER.debug("add storage attribute to model");

        model.addAttribute("address", new AddressDTO());
        LOGGER.debug("add address attribute to model");

        model.addAttribute("clients", clientService.getClients());
        LOGGER.debug("add clients attribute to model");

        LOGGER.trace("'newStorage(Model model)' executed successfully.");
        return "storage/storageForm";
    }

    @GetMapping("/storage/{id}/update")
    public String updateStorage(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'updateStorage(@PathVariable String id, Model model)' method");

        StorageDTO storage = storageService.findDTObyId(Long.valueOf(id));

        model.addAttribute("storage", storage);
        LOGGER.debug("add storage attribute to model");

        model.addAttribute("address", storage.getAddressDTO());
        LOGGER.debug("add address attribute to model");

        model.addAttribute("clients", clientService.getClients());
        LOGGER.debug("add clients attribute to model");

        LOGGER.trace("'updateStorage(@PathVariable String id, Model model)' executed successfully.");
        return "storage/storageForm";
    }

    @PostMapping("/storage/save")
    public String saveOrUpdate(@Valid @ModelAttribute("storage") StorageDTO storageDTO, BindingResult storageBinding,
                               @Valid @ModelAttribute("address") AddressDTO addressDTO, BindingResult addressBinding,
                               Model model) {
        LOGGER.trace("Enter in 'saveNew(storageCommand, storageBinding, addressCommand, addressBinding, model)' method");

        if (storageBinding.hasErrors() || addressBinding.hasErrors()) {

            storageBinding.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));
            addressBinding.getAllErrors().forEach(objectError -> LOGGER.warn(objectError.toString()));

            model.addAttribute("storage", storageDTO);
            LOGGER.debug("add storage attribute to model");

            model.addAttribute("address", addressDTO);
            LOGGER.debug("add address attribute to model");

            model.addAttribute("clients", clientService.getClients());
            LOGGER.debug("add clients attribute to model");

            return "storage/storageForm";
        }

        storageDTO.setAddressDTO(addressDTO);
        StorageDTO savedStorage = storageService.saveDTO(storageDTO);

        LOGGER.trace("'saveNew(storageCommand, storageBinding, addressCommand, addressBinding, model)' executed successfully.");
        return "redirect:/app/v1/all/" + savedStorage.getId() + "/show/storage";
    }

    @GetMapping("/storage/{id}/delete")
    public String deleteById(@PathVariable String id) {
        LOGGER.trace("Enter in 'deleteById(@PathVariable String id)' method");

        storageService.deleteById(Long.valueOf(id));

        LOGGER.trace("'deleteById(@PathVariable String id)' executed successfully.");
        return "redirect:/app/v1/all/storage";
    }

    @GetMapping("/storage/{id}/image")
    public String storageImageUpload(@PathVariable String id, Model model) {
        LOGGER.trace("Enter in 'storageImageUpload(@PathVariable String id, Model model)' method");

        model.addAttribute("storage", storageService.getById(Long.valueOf(id)));
        LOGGER.debug("add storage attribute to model");

        LOGGER.trace("'storageImageUpload(@PathVariable String id, Model model)' executed successfully.");
        return "storage/storageImageUpload";
    }

    @GetMapping("/storage/{id}/storageimage")
    public void renderStorageImage(@PathVariable String id, HttpServletResponse response) {
        LOGGER.trace("Enter in 'renderStorageImage(@PathVariable String id, HttpServletResponse response' method");

        StorageDTO storage = storageService.findDTObyId(Long.valueOf(id));

        if (storage.getImage() != null) {
            response.setContentType("image/jpeg");
            InputStream inputStream = new ByteArrayInputStream(storage.getImage());
            try {
                IOUtils.copy(inputStream, response.getOutputStream());
            } catch (IOException e) {
                LOGGER.error("Attention can't render storage image!!!", e);
                throw new ImageCorruptedException(e.getMessage());
            }
        }

        LOGGER.trace("'renderStorageImage(@PathVariable String id, HttpServletResponse response' executed successfully.");
    }

    @PostMapping("/storage/{id}/image")
    public String saveStorageImage(@PathVariable String id, @RequestParam("imagefile") MultipartFile multipartFile) {
        LOGGER.trace("Enter in 'saveStorageImage(String id, MultipartFile multipartFile)' method");

        storageService.saveImage(Long.valueOf(id), multipartFile);

        LOGGER.trace("'saveStorageImage(String id, MultipartFile multipartFile)' executed successfully.");
        return "redirect:/app/v1/all/" + id + "/show/storage";
    }
}
