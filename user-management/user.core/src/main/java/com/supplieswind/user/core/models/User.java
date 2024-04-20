package com.supplieswind.user.core.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "users") // mark a class as a domain model for mongo db
public class User {
    @Id
    private String id;

    @NotEmpty(message = "firstname is mandatory")
    private String firstname;

    @NotEmpty(message = "lastname is mandatory")
    private String lastname;

    @Email(message = "please provide a valid email address")
    @NotEmpty(message = "email address is mandatory")
    private String emailAddress;

    @Valid
    @NotNull(message = "please provide account credentials")
    private Account account;
}
