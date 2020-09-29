package com.realestate.service;

import com.realestate.dto.RealEstateAgentDTO;
import com.realestate.model.people.RealEstateAgent;

import java.util.List;

public interface RealEstateAgentService {

    RealEstateAgent getById(Long id);

    List<RealEstateAgent> getRealEstateAgents();

    RealEstateAgentDTO findDTObyId(Long id);

    RealEstateAgentDTO saveRealEstateAgentDTO(RealEstateAgentDTO realEstateAgentDTO);

    void deleteById(Long id);

    List<RealEstateAgentDTO> getAgentsDTO();

    RealEstateAgentDTO patchAgent(Long id, RealEstateAgentDTO agentDTO);
}
