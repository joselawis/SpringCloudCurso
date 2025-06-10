package com.lawis.springcloud.msvc.users.mappers;

import org.mapstruct.Mapper;

import com.lawis.libs.msvc.commons.models.User;
import com.lawis.libs.msvc.entities.users.UserEntity;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {

    User toModel(UserEntity entity);

    UserEntity toEntity(User model);
}
