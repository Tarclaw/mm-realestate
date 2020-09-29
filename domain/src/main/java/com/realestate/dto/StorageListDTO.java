package com.realestate.dto;

import java.util.ArrayList;
import java.util.List;

public class StorageListDTO {

    private List<StorageDTO> storageDTOS = new ArrayList<>();

    public StorageListDTO(List<StorageDTO> storageDTOS) {
        this.storageDTOS = storageDTOS;
    }

    public List<StorageDTO> getStorageDTOS() {
        return storageDTOS;
    }

    public void setStorageDTOS(List<StorageDTO> storageDTOS) {
        this.storageDTOS = storageDTOS;
    }
}
