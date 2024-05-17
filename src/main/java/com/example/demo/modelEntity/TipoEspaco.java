package com.example.demo.modelEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Objects;

@Entity
public class TipoEspaco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_tipo_espaco;
    private String titulo;

    public Integer getId_tipo_espaco() {
        return id_tipo_espaco;
    }

    public void setId_tipo_espaco(Integer id_tipo_espaco) {
        this.id_tipo_espaco = id_tipo_espaco;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipoEspaco tipoEspaco = (TipoEspaco) o;
        return Objects.equals(id_tipo_espaco, tipoEspaco.id_tipo_espaco) && Objects.equals(titulo, tipoEspaco.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_tipo_espaco, titulo);
    }
}
