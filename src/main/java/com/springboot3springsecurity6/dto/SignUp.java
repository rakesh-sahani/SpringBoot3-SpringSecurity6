package com.springboot3springsecurity6.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot3springsecurity6.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SignUp {

    @NotBlank(message = "First name is mandatory")
    @JsonProperty("firstname")
    private String firstName;

    @JsonProperty("middlename")
    private String middleName;

    @NotBlank(message = "Last name is mandatory")
    @JsonProperty("lastname")
    private String lastName;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Password is mandatory")
    private String password;

    private Role role;
}
