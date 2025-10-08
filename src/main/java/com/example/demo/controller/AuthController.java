package com.example.demo.controller;

import com.example.demo.modelEntity.Pessoa;
import com.example.demo.modelEntity.AuthResponse;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

// CONTROLADOR QUE CUIDA DO LOGIN DO USUARIO
@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PessoaController controller;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody Pessoa pessoa) {
        // Verifica se o login é válido
        Pessoa pes = controller.verificaLogin(pessoa.getEmail(), pessoa.getSenha());
        if (pes != null) {
            // Autentica e gera o token
            String token = authService.authenticate(pessoa.getEmail(), pessoa.getSenha());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("idPessoa", pes.getIdPessoa());

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401).body("Credenciais inválidas");
        }
    }
}
