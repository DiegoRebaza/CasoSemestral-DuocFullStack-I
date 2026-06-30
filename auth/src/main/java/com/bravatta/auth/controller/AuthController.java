package com.bravatta.auth.controller;

import com.bravatta.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String token = userService.login(email, password);

        Map<String, String> resp = new HashMap<>();
        if (token == null) {
            resp.put("status", "error");
            resp.put("token", "");
        } else {
            resp.put("status", "ok");
            resp.put("token", token);
        }
        return resp;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        String resultado = userService.register(email, password);

        Map<String, String> resp = new HashMap<>();
        resp.put("message", resultado);
        return resp;
    }
}