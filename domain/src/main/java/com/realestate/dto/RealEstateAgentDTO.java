package com.realestate.dto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RealEstateAgentDTO extends PersonDTO {

    @NotNull(message = "Common, everybody must have something to eat")
    private BigInteger salary;

    @PastOrPresent(message = "Hired date should be past or present")
    private LocalDate hiredDate;

    @FutureOrPresent(message = "Quit date should be present or future")
    private LocalDate quitDate;

    private Set<ClientDTO> clientsDTO = new HashSet<>();

    private List<Long> clientIds = new ArrayList<>();

    public RealEstateAgentDTO() {
    }

    public BigInteger getSalary() {
        return salary;
    }

    public void setSalary(BigInteger salary) {
        this.salary = salary;
    }

    public LocalDate getHiredDate() {
        return hiredDate;
    }

    public void setHiredDate(LocalDate hiredDate) {
        this.hiredDate = hiredDate;
    }

    public LocalDate getQuitDate() {
        return quitDate;
    }

    public void setQuitDate(LocalDate quitDate) {
        this.quitDate = quitDate;
    }

    public Set<ClientDTO> getClientsDTO() {
        return clientsDTO;
    }

    public void setClientsDTO(Set<ClientDTO> clientsDTO) {
        this.clientsDTO = clientsDTO;
    }

    public void addClientDTO(ClientDTO clientDTO) {
        if (clientsDTO == null) {
            clientsDTO = new HashSet<>();
        }

        this.clientsDTO.add(clientDTO);
    }

    public List<Long> getClientIds() {
        return clientIds;
    }

    public void setClientIds(List<Long> clientIds) {
        this.clientIds = clientIds;
    }
}
