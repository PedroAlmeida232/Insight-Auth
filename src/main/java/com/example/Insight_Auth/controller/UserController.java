package com.example.Insight_Auth.controller;

import com.example.Insight_Auth.dto.LoginRequestDto;
import com.example.Insight_Auth.dto.RegisterDto;
import com.example.Insight_Auth.dto.TokenResponseDto;
import com.example.Insight_Auth.model.UserModel;
import com.example.Insight_Auth.repository.UserRepository;
import com.example.Insight_Auth.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
        String token = userService.autenticar(dto);
        return ResponseEntity.ok(new TokenResponseDto(token));
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterDto dto) {
        // 1. Valida se o e-mail já existe no PostgreSQL
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("E-mail já cadastrado!");
        }

        // 2. Criptografa a senha do usuário usando BCrypt
        String encryptedPassword = new BCryptPasswordEncoder().encode(dto.getSenha());

        // 3. Instancia e salva a nova entidade no banco
        UserModel newUser = new UserModel();
        newUser.setNome(dto.getNome());
        newUser.setEmail(dto.getEmail());
        newUser.setSenha(encryptedPassword);

        userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}