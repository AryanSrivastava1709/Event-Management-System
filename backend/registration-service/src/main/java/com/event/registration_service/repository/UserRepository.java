package com.event.registration_service.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.event.registration_service.model.User;


// This interface is used to create the user repository or to communicate with the user table in the database

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    //The method findByEmail is used to find the user by email and is automatically implemented by Spring Data JPA
    Optional<User> findByEmail(String email);
}
