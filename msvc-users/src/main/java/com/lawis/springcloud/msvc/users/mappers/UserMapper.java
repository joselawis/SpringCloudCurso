package com.lawis.springcloud.msvc.users.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.lawis.libs.msvc.commons.models.User;
import com.lawis.libs.msvc.entities.users.UserEntity;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
@Component
public interface UserMapper {

    User toModel(UserEntity entity);

    UserEntity toEntity(User model);
}
