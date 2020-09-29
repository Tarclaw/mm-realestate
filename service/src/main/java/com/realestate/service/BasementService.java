package com.realestate.service;

import com.realestate.dto.BasementDTO;
import com.realestate.model.buildings.Basement;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BasementService {

    Basement getById(Long id);

    List<Basement> getBasements();

    List<BasementDTO> getBasementsDTO();

    BasementDTO findDTObyId(Long id);

    BasementDTO saveDTO(BasementDTO basementDTO);

    void deleteById(Long id);

    void saveImage(Long id, MultipartFile file);

    BasementDTO patchBasement(Long id, BasementDTO basementDTO);
}
