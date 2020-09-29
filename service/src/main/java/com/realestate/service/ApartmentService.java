package com.realestate.service;

import com.realestate.dto.ApartmentDTO;
import com.realestate.model.buildings.Apartment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ApartmentService {

    Apartment getById(Long id);

    List<Apartment> getApartments();

    List<ApartmentDTO> getApartmentsDTO();

    ApartmentDTO findDTObyId(Long id);

    ApartmentDTO saveDTO(ApartmentDTO apartmentDTO);

    void deleteById(Long id);

    void saveImage(Long id, MultipartFile multipartFile);

    ApartmentDTO patchApartment(ApartmentDTO apartmentDTO, Long id);
}
