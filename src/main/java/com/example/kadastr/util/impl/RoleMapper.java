package com.example.kadastr.util.impl;

import com.example.kadastr.dto.RoleDto;
import com.example.kadastr.model.Role;
import com.example.kadastr.util.Mapper;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper implements Mapper<Role, RoleDto> {

    @Override
    public Role mapToModel(RoleDto roleDto) {
        Role role = new Role();
        role.setName(roleDto.getName());
        return role;
    }

    @Override
    public RoleDto mapToDto(Role role) {
        return new RoleDto(role.getName());
    }
}
