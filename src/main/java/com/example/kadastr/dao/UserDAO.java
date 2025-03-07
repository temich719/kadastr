package com.example.kadastr.dao;

import com.example.kadastr.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserDAO extends JpaRepository<User, UUID> {

    @Query(value = "SELECT * FROM users WHERE username = :username LIMIT 1", nativeQuery = true)
    Optional<User> findUserByUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1", nativeQuery = true)
    Optional<User> findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}
