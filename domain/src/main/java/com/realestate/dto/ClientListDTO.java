package com.realestate.dto;

import java.util.ArrayList;
import java.util.List;

public class ClientListDTO {

    private List<ClientDTO> clientDTOS = new ArrayList<>();

    public ClientListDTO() {
    }

    public ClientListDTO(List<ClientDTO> clientDTOS) {
        this.clientDTOS = clientDTOS;
    }

    public List<ClientDTO> getClientDTOS() {
        return clientDTOS;
    }

    public void setClientDTOS(List<ClientDTO> clientDTOS) {
        this.clientDTOS = clientDTOS;
    }
}
