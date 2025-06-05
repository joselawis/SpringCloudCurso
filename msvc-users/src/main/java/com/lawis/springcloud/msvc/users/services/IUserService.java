package com.lawis.springcloud.msvc.users.services;

import java.util.Optional;

import com.lawis.springcloud.msvc.users.entities.User;

public interface IUserService {

    public Optional<User> findById(Long id);

    public User findByUsername(String username);

    public User findByEmail(String email);

    public Iterable<User> findAll();

    public User save(User user);

    public void delete(Long id);

}
