package com.realestate.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class ClientDTO extends PersonDTO {

    private Long agentId;

    @NotNull(message = "Please enter value")
    @Size(min = 10, max = 1000, message = "Please provide requirements between 10 and 1000 characters")
    private String customerRequirements;

    private Set<FacilityDTO> facilities = new HashSet<>();
    private Set<RealEstateAgentDTO> realEstateAgents = new HashSet<>();

    public ClientDTO() {
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getCustomerRequirements() {
        return customerRequirements;
    }

    public void setCustomerRequirements(String customerRequirements) {
        this.customerRequirements = customerRequirements;
    }

    public Set<FacilityDTO> getFacilities() {
        return facilities;
    }

    public void setFacilities(Set<FacilityDTO> facilities) {
        this.facilities = facilities;
    }

    public Set<RealEstateAgentDTO> getRealEstateAgents() {
        return realEstateAgents;
    }

    public void setRealEstateAgents(Set<RealEstateAgentDTO> realEstateAgents) {
        this.realEstateAgents = realEstateAgents;
    }
}
