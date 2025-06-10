package com.lawis.springcloud.msvc.users.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lawis.libs.msvc.commons.models.User;
import com.lawis.libs.msvc.entities.entities.users.RoleEntity;
import com.lawis.libs.msvc.entities.entities.users.UserEntity;
import com.lawis.springcloud.msvc.users.mappers.UserMapper;
import com.lawis.springcloud.msvc.users.repositories.RoleRepository;
import com.lawis.springcloud.msvc.users.repositories.UserRepository;

import jakarta.ws.rs.NotFoundException;

@Service
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private final UserMapper mapper;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
            UserMapper mapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
                .map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<User> findAll() {
        return ((Collection<UserEntity>) userRepository.findAll()).stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getEnabled() == null) {
            user.setEnabled(true);
        }
        UserEntity userEntity = mapper.toEntity(user);
        userEntity.setRoles(getRoles(user));
        UserEntity savedEntity = userRepository.save(userEntity);
        return mapper.toModel(savedEntity);
    }

    @Override
    @Transactional
    public Optional<User> update(Long id, User user) throws NotFoundException {
        return userRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setAdmin(user.isAdmin());
                    if (user.getEnabled() != null) {
                        existingUser.setEnabled(user.getEnabled());
                    } else {
                        existingUser.setEnabled(true);
                    }
                    existingUser.setRoles(getRoles(user));

                    return userRepository.save(existingUser);
                }).map(mapper::toModel);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private List<RoleEntity> getRoles(User user) {
        List<RoleEntity> roles = new ArrayList<>();
        Optional<RoleEntity> roleOptional = roleRepository.findByName("ROLE_USER");
        roleOptional.ifPresent(roles::add);

        if (user.isAdmin()) {
            Optional<RoleEntity> adminRoleOptional = roleRepository.findByName("ROLE_ADMIN");
            adminRoleOptional.ifPresent(roles::add);
        }
        return roles;
    }
}
