package com.library.movieslibrary.service;


import com.library.movieslibrary.model.ERole;
import com.library.movieslibrary.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, String> {
	Optional<Role> findByName(ERole name);
}
