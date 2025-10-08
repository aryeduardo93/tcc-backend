package com.example.demo.modelEntity;

import jakarta.persistence.*;

// REPRESENTA A TABELA FOTOS ESPAÇO DENTRO DO BANCO DE DADOS
@Entity
@Table(name = "Fotos_Espaco")
public class FotosEspaco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFoto;

    @ManyToOne
    @JoinColumn(name = "id_espaco", nullable = false)
    private Espaco espaco;

    @Column(nullable = false)
    private String caminhoFoto;

    private String legenda;


    public FotosEspaco() {
    }

    public FotosEspaco(Espaco espaco, String caminhoFoto, String legenda) {
        this.espaco = espaco;
        this.caminhoFoto = caminhoFoto;
        this.legenda = legenda;
    }


    public Long getIdFoto() {
        return idFoto;
    }

    public void setIdFoto(Long idFoto) {
        this.idFoto = idFoto;
    }

    public Espaco getEspaco() {
        return espaco;
    }

    public void setEspaco(Espaco espaco) {
        this.espaco = espaco;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public String getLegenda() {
        return legenda;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }
}
