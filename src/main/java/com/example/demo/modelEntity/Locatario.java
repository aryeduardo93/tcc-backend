package com.example.demo.modelEntity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Locatario {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id_locatario;
    private Integer cpf;
    private String nome_locatario;
    private String endereco_locatario;
    private String email;
    private String contato;

    public Integer getId_locatario() {
        return id_locatario;
    }

    public void setId_locatario(Integer id_locatario) {
        this.id_locatario = id_locatario;
    }

    public Integer getCpf() {
        return cpf;
    }

    public void setCpf(Integer cpf) {
        this.cpf = cpf;
    }

    public String getNome_locatario() {
        return nome_locatario;
    }

    public void setNome_locatario(String nome_locatario) {
        this.nome_locatario = nome_locatario;
    }

    public String getEndereco_locatario() {
        return endereco_locatario;
    }

    public void setEndereco_locatario(String endereco_locatario) {
        this.endereco_locatario = endereco_locatario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Locatario locatario = (Locatario) o;
        return id_locatario == locatario.id_locatario && Objects.equals(cpf, locatario.cpf) && Objects.equals(nome_locatario, locatario.nome_locatario) && Objects.equals(endereco_locatario, locatario.endereco_locatario) && Objects.equals(email, locatario.email) && Objects.equals(contato, locatario.contato);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_locatario, nome_locatario, endereco_locatario, email, contato);
    }

    @Override
    public String toString() {
        return "Locatario{" +
                "id_locatario=" + id_locatario +
                ", cpf='" + cpf + '\'' +
                ", nome_locatario='" + nome_locatario + '\'' +
                ", endereco_locatario='" + endereco_locatario + '\'' +
                ", email='" + email + '\'' +
                ", contato='" + contato + '\'' +
                '}';
    }
}
