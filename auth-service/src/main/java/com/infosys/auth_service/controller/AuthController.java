package com.infosys.auth_service.controller;

import com.infosys.auth_service.entity.Teller;
import com.infosys.auth_service.repository.TellerRepo;
import com.infosys.auth_service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    TellerRepo repo;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/register")
    public Teller register(@RequestBody Teller teller) {
        return repo.save(teller); // plaintext for hackathon simplicity; note this in README
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Teller loginRequest) {
        Teller teller = repo.findByUsername(loginRequest.getUsername());
        Map<String, String> response = new HashMap<>();

        if (teller == null || !teller.getPassword().equals(loginRequest.getPassword())) {
            response.put("error", "Invalid username or password");
            return response;
        }

        String token = jwtUtil.generateToken(teller.getUsername(), teller.getRole());
        response.put("token", token);
        response.put("role", teller.getRole());
        response.put("username", teller.getUsername());
        return response;
    }

    @GetMapping("/validate")
    public Map<String, Object> validate(@RequestParam String token) {
        Map<String, Object> response = new HashMap<>();
        boolean valid = jwtUtil.isTokenValid(token);
        response.put("valid", valid);
        if (valid) {
            response.put("username", jwtUtil.extractUsername(token));
            response.put("role", jwtUtil.extractRole(token));
        }
        return response;
    }
}