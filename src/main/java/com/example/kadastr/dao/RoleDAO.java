package com.example.kadastr.dao;

import com.example.kadastr.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

//Data access object on Role entity
@Repository
public interface RoleDAO extends JpaRepository<Role, UUID> {

    /**
     * finds role by its name
     * @param name role name
     * @return role with given name
     */
    @Query(value = "SELECT * FROM roles WHERE name = :name LIMIT 1", nativeQuery = true)
    Role findRoleByName(String name);

}
