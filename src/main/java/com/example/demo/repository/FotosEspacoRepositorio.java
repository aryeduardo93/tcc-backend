package com.example.demo.repository;

import com.example.demo.modelEntity.FotosEspaco;
import com.example.demo.modelEntity.Espaco;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

// INTERFACE QUE CUIDA DAS CONSULTAS RELACIONADAS A TABELA DE FOTOS

public interface FotosEspacoRepositorio extends CrudRepository<FotosEspaco, Long> {

    // BUSCA TODAS AS FOTOS VINCULADAS A UM ESPAÇO ESPECIFICO
    List<FotosEspaco> findByEspaco(Espaco espaco);

    // BUSCA TODAS AS FOTOS DE UM ESPAÇO USANDO ID
    List<FotosEspaco> findByEspacoIdEspaco(Long idEspaco);
}
