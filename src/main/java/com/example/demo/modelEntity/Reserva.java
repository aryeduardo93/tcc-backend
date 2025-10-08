package com.example.demo.modelEntity;

import jakarta.persistence.*;
import java.time.LocalDate;

// REPRESENTA A TABELA DE RESERVAS NO BANCO DE DADOS
@Entity
public class Reserva {

    // DEFINE A CHAVE PRIMÁRIA DA TABELA
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idReserva;
    private LocalDate dataDisponivel;  // Alteração para LocalDate
    private Boolean aprovado;

    // RESERVAS PODEM ESTAR LIGADAS A UM UNICO ESPAÇO
    @ManyToOne
    @JoinColumn(name = "Espaco_id_espaco")
    private Espaco espaco;

    // UMA PESSOA PODE FAZER VARIAS RESERVAS
    @ManyToOne
    @JoinColumn(name = "Pessoa_id_locatario")
    private Pessoa pessoa;

    public Integer getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Integer idReserva) {
        this.idReserva = idReserva;
    }

    public LocalDate getDataDisponivel() {  // Alterado para LocalDate
        return dataDisponivel;
    }

    public void setDataDisponivel(LocalDate dataDisponivel) {  // Alterado para LocalDate
        this.dataDisponivel = dataDisponivel;
    }

    public Boolean getAprovado() {
        return aprovado;
    }

    public void setAprovado(Boolean aprovado) {
        this.aprovado = aprovado;
    }

    public Espaco getEspaco() {
        return espaco;
    }

    public void setEspaco(Espaco espaco) {
        this.espaco = espaco;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
}
