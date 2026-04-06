package com.pablodev.taskmanager.service;

import com.pablodev.taskmanager.entity.Role;
import com.pablodev.taskmanager.entity.User;
import com.pablodev.taskmanager.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder(); // simple, puedes inyectarlo más tarde
    }

    public User register(String username, String email, String rawPassword) throws IllegalArgumentException {
        // Validar email único
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email ya registrado");
        }

        // Encriptar contraseña
        String encodedPassword = passwordEncoder.encode(rawPassword);

        // Crear usuario
        User user = new User(username, email, encodedPassword, Role.USER, LocalDateTime.now());

        // Guardar en BD
        return userRepository.save(user);
    }
}