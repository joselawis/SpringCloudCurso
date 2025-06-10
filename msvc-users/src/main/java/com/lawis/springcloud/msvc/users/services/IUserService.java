package com.lawis.springcloud.msvc.users.services;

import java.util.Optional;

import com.lawis.libs.msvc.commons.models.User;

public interface IUserService {

    public Optional<User> findById(Long id);

    public Optional<User> findByUsername(String username);

    public Optional<User> findByEmail(String email);

    public Iterable<User> findAll();

    public User save(User user);

    public Optional<User> update(Long id, User user);

    public void delete(Long id);

}
