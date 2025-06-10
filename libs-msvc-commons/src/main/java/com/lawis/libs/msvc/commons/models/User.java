package com.lawis.libs.msvc.commons.models;

import java.util.List;

import lombok.Data;

@Data
public class User {
    private Long id;

    private String username;

    private String password;

    private Boolean enabled;

    private boolean admin;

    private String email;

    private List<Role> roles;
}
