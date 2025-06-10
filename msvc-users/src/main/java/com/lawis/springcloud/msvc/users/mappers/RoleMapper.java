package com.lawis.springcloud.msvc.users.mappers;

import org.mapstruct.Mapper;

import com.lawis.libs.msvc.commons.models.Role;
import com.lawis.libs.msvc.entities.entities.users.RoleEntity;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toModel(RoleEntity entity);

    RoleEntity toEntity(Role model);
}