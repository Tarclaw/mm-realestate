package com.realestate.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PersonDTO {

    private Long id;

    @NotNull(message = "Please enter value")
    @Size(min = 2, max = 50, message = "Please enter value between 2 and 50 characters")
    private String firstName;

    @NotNull(message = "Please enter value")
    @Size(min = 2, max = 50, message = "Please enter value between 2 and 50 characters")
    private String lastName;

    @NotNull(message = "Please enter value")
    @Size(min = 2, max = 50, message = "Please enter value between 2 and 50 characters")
    private String login;

    @NotNull(message = "Please enter value")
    @Size(min = 2, max = 50, message = "Please enter value between 2 and 50 characters")
    private String password;

    private ContactDTO contactDTO;

    public PersonDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ContactDTO getContactDTO() {
        return contactDTO;
    }

    public void setContactDTO(ContactDTO contactDTO) {
        this.contactDTO = contactDTO;
    }
}
