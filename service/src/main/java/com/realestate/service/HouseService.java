package com.realestate.service;

import com.realestate.dto.HouseDTO;
import com.realestate.model.buildings.House;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HouseService {

    House getById(Long id);

    List<House> getHouses();

    HouseDTO findDTObyId(Long id);

    HouseDTO saveDTO(HouseDTO houseDTO);

    void deleteById(Long id);

    void saveImage(Long id, MultipartFile multipartFile);

    List<HouseDTO> getHousesDTO();

    HouseDTO patchHouse(Long id, HouseDTO houseDTO);

}
