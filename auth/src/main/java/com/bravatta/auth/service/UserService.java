package com.bravatta.auth.service;

import com.bravatta.auth.model.User;
import com.bravatta.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final HashService hashService;

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) return null;

        String hashedInput = hashService.sha1(password);
        if (!hashedInput.equals(user.getPassword())) return null;

        return jwtService.generateToken(email);
    }

    public String getRole(String email) {
        User user = userRepository.findByEmail(email);
        return user != null ? user.getRole() : null;
    }

    public String register(String email, String password) {
        User existing = userRepository.findByEmail(email);
        if (existing != null) return "El usuario ya existe";

        User user = new User();
        user.setEmail(email);
        user.setPassword(hashService.sha1(password));
        user.setRole("USER");
        userRepository.save(user);

        return "Usuario creado exitosamente";
    }
}