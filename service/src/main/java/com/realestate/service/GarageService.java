package com.realestate.service;

import com.realestate.dto.GarageDTO;
import com.realestate.dto.GarageListDTO;
import com.realestate.model.buildings.Garage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface GarageService {

    Garage getById(Long id);

    List<Garage> getGarages();

    GarageDTO findDTObyId(Long id);

    GarageDTO saveDTO(GarageDTO garageDTO);

    void deleteById(Long id);

    void saveImage(Long id, MultipartFile multipartFile);

    List<GarageDTO> getGaragesDTO();

    GarageDTO patchGarage(Long id, GarageDTO garageDTO);

}
