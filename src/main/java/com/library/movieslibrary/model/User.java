package com.library.movieslibrary.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class User {
    @Id
    private String id;
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String password;

    private Set<Role> roles = new HashSet<>();

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
