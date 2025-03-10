package com.example.kadastr.dao;

import com.example.kadastr.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

//Data access object on User entity
@Repository
public interface UserDAO extends JpaRepository<User, UUID> {

    /**
     * finds user by username
     * @param username - username
     * @return user with given username
     */
    @Query(value = "SELECT * FROM users WHERE username = :username LIMIT 1", nativeQuery = true)
    Optional<User> findUserByUsername(@Param("username") String username);

}
