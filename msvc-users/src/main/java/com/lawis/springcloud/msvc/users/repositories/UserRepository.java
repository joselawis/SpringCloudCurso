package com.lawis.springcloud.msvc.users.repositories;

import org.springframework.data.repository.CrudRepository;

import com.lawis.springcloud.msvc.users.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

    User findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
