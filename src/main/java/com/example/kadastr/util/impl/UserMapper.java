package com.example.kadastr.util.impl;

import com.example.kadastr.dto.RoleDto;
import com.example.kadastr.dto.UserDto;
import com.example.kadastr.model.Role;
import com.example.kadastr.model.User;
import com.example.kadastr.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class UserMapper implements Mapper<User, UserDto> {

    private final Mapper<Role, RoleDto> roleMapper;

    @Autowired
    public UserMapper(Mapper<Role, RoleDto> roleMapper) {
        this.roleMapper = roleMapper;
    }

    @Override
    public User mapToModel(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setParentName(userDto.getParentName());
        user.setRole(roleMapper.mapToModel(userDto.getRole()));
        return user;
    }

    @Override
    public UserDto mapToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setName(user.getName());
        userDto.setSurname(user.getSurname());
        userDto.setParentName(user.getParentName());
        userDto.setRole(roleMapper.mapToDto(user.getRole()));
        return userDto;
    }
}
