package com.example.demo.controller;

import java.util.Objects;

public class MovimentacaoCountDTO {
    private String nome;
    private Long count;

    public MovimentacaoCountDTO(String nome, Long count) {
        this.nome = nome;
        this.count = count;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovimentacaoCountDTO that = (MovimentacaoCountDTO) o;
        return Objects.equals(nome, that.nome) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, count);
    }
}
