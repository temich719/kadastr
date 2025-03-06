package com.example.kadastr.dao;

import com.example.kadastr.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserDAO extends JpaRepository<User, UUID> {
}
