package com.lawis.springcloud.msvc.users.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.lawis.libs.msvc.commons.models.Role;
import com.lawis.libs.msvc.commons.models.User;
import com.lawis.libs.msvc.entities.entities.users.RoleEntity;
import com.lawis.libs.msvc.entities.entities.users.UserEntity;

@Mapper(componentModel = "spring", uses = RoleMapper.class)
public interface UserMapper {

    User toModel(UserEntity entity);

    UserEntity toEntity(User model);

    RoleMapper getRoleMapper();

    default List<Role> toRoleList(List<RoleEntity> entities) {
        return entities.stream()
                .map(getRoleMapper()::toModel)
                .toList();
    }

    default List<RoleEntity> toRoleEntityList(List<Role> models) {
        return models.stream()
                .map(getRoleMapper()::toEntity)
                .toList();
    }

}
