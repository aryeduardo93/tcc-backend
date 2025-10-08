package com.example.demo.modelEntity;

import jakarta.persistence.*;
import java.util.Objects;

// REPRESENTA A TABELA PESSOA DENTRO DO BANCO DE DADOS
@Entity
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPessoa;

    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String senha;

    private String foto_perfil; // ✔️ Caminho da foto de perfil

    // 🔥 Getters e Setters
    public Long getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Long idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(String caminhoFoto) {
        this.foto_perfil = caminhoFoto;
    }

    // COMPARA SE DUAS PESSOAS SÃO IGUAIS EM TODOS OS ATRIBUTOS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pessoa)) return false;
        Pessoa pessoa = (Pessoa) o;
        return Objects.equals(idPessoa, pessoa.idPessoa) &&
                Objects.equals(nome, pessoa.nome) &&
                Objects.equals(cpf, pessoa.cpf) &&
                Objects.equals(email, pessoa.email) &&
                Objects.equals(telefone, pessoa.telefone) &&
                Objects.equals(senha, pessoa.senha) &&
                Objects.equals(foto_perfil, pessoa.foto_perfil);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPessoa, nome, cpf, email, telefone, senha, foto_perfil);
    }

    // OBJETO
    @Override
    public String toString() {
        return "Pessoa{" +
                "idPessoa=" + idPessoa +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", senha='" + senha + '\'' +
                ", foto_perfil='" + foto_perfil + '\'' +
                '}';
    }
}
