package com.example.demo.dto;

import com.example.demo.modelEntity.Espaco;

import java.time.LocalDate;

// RETORNA INFORMAÇÕES DETALHADAS SOBRE RESERVA
public class EspacoDTO {
    private Integer idEspaco;
    private String nomeEspaco;
    private String tipoEspaco;
    private String descricao;
    private String regras;
    private Double valor;
    private String caminhoFoto;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String telefoneDono;
    private Double latitude;
    private Double longitude;


    public EspacoDTO(Espaco espaco) {
        this.idEspaco = espaco.getIdEspaco();
        this.nomeEspaco = espaco.getNomeEspaco();
        this.tipoEspaco = espaco.getTipoEspaco();
        this.descricao = espaco.getDescricao();
        this.regras = espaco.getRegras();
        this.valor = espaco.getValor();
        this.caminhoFoto = espaco.getCaminhoFoto();
        this.endereco = espaco.getEndereco();
        this.bairro = espaco.getBairro();
        this.cidade = espaco.getCidade();
        this.estado = espaco.getEstado();
        this.cep = espaco.getCep();
        this.telefoneDono = (espaco.getPessoa() != null) ? espaco.getPessoa().getTelefone() : null;
        this.latitude = espaco.getLatitude();
        this.longitude = espaco.getLongitude();
    }


    public String getTelefoneDono() {
        return telefoneDono;
    }

    public Integer getIdEspaco() {
        return idEspaco;
    }

    public String getNomeEspaco() {
        return nomeEspaco;
    }

    public String getTipoEspaco() {
        return tipoEspaco;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getRegras() {
        return regras;
    }

    public Double getValor() {
        return valor;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getCep() {
        return cep;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

}
