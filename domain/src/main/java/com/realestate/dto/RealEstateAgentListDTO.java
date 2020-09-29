package com.realestate.dto;

import java.util.ArrayList;
import java.util.List;

public class RealEstateAgentListDTO {

    private List<RealEstateAgentDTO> agentDTOS = new ArrayList<>();

    public RealEstateAgentListDTO() {
    }

    public RealEstateAgentListDTO(List<RealEstateAgentDTO> agentDTOS) {
        this.agentDTOS = agentDTOS;
    }

    public List<RealEstateAgentDTO> getAgentDTOS() {
        return agentDTOS;
    }

    public void setAgentDTOS(List<RealEstateAgentDTO> agentDTOS) {
        this.agentDTOS = agentDTOS;
    }
}
