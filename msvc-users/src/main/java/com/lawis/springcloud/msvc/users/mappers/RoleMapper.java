package com.lawis.springcloud.msvc.users.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.lawis.libs.msvc.commons.models.Role;
import com.lawis.libs.msvc.entities.users.RoleEntity;

@Mapper(componentModel = "spring")
@Component
public interface RoleMapper {

    Role toModel(RoleEntity entity);

    RoleEntity toEntity(Role model);
}