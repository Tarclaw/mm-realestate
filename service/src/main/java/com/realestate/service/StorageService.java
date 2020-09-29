package com.realestate.service;

import com.realestate.dto.StorageDTO;
import com.realestate.model.buildings.Storage;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {

    Storage getById(Long id);

    List<Storage> getStorages();

    StorageDTO findDTObyId(Long id);

    StorageDTO saveDTO(StorageDTO storageDTO);

    void deleteById(Long id);

    void saveImage(Long id, MultipartFile multipartFile);

    List<StorageDTO> getStoragesDTO();

    StorageDTO patchStorage(Long id, StorageDTO storageDTO);
}
