package com.supplieswind.user.core.models;

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
    private String firstname;
    private String lastname;
    private String emailAddress;

    private Account account;
}
