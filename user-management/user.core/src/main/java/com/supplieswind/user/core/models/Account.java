package com.supplieswind.user.core.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "accounts")
public class Account {
    @Size(min = 2, message = "username must have a minimum of 2 characters")
    @NotBlank(message = "the account username is mandatory")
    private String username;

    @Size(min = 7, message = "username must have a minimum of 7 characters")
    @NotBlank(message = "the account password is mandatory")
    private String password;

    @NotEmpty(message = "You should specify at last 1 user role")
    private List<Role> roles;
}
