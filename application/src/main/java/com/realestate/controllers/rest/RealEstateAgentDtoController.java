package com.realestate.controllers.rest;

import com.realestate.dto.RealEstateAgentDTO;
import com.realestate.dto.RealEstateAgentListDTO;
import com.realestate.service.RealEstateAgentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/agents")
public class RealEstateAgentDtoController {

    private final RealEstateAgentService service;

    public RealEstateAgentDtoController(RealEstateAgentService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RealEstateAgentListDTO getListOfAgents() {
        return new RealEstateAgentListDTO(service.getAgentsDTO());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RealEstateAgentDTO getAgentById(@PathVariable Long id) {
        return service.findDTObyId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RealEstateAgentDTO createNewAgent(@RequestBody RealEstateAgentDTO agentDTO) {
        return service.saveRealEstateAgentDTO(agentDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RealEstateAgentDTO updateAgent(@PathVariable Long id, @RequestBody RealEstateAgentDTO agentDTO) {
        agentDTO.setId(id);
        return service.saveRealEstateAgentDTO(agentDTO);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RealEstateAgentDTO patchAgent(@PathVariable Long id, @RequestBody RealEstateAgentDTO agentDTO) {
        return service.patchAgent(id, agentDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAgent(@PathVariable Long id) {
        service.deleteById(id);
    }
}
