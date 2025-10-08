package com.example.demo.modelEntity;

// CLASSE QUE ENVIA UM TOKEN JWT DE VOLTA PARA O FRONT APÓS A AUTENTICAÇÃO
public class AuthResponse {
    private String token;

    // CONSTRUTOR QUE RECEBE UM TOKEN PARA AO CRIAR UM NOVO AUTH REPONSE
    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}