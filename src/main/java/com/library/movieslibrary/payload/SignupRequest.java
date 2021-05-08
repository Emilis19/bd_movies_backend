package com.library.movieslibrary.payload;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignupRequest {
    @NonNull
    private String username;
    @NonNull
    private String email;
    @NonNull
    private String password;

    private Set<String> role;
}
