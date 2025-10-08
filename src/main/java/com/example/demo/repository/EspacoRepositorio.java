package com.example.demo.repository;

import com.example.demo.modelEntity.Pessoa;
import com.example.demo.modelEntity.Espaco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EspacoRepositorio extends JpaRepository<Espaco, Integer> {

    // BUSCA TODOS OS ESPAÇOS CADASTRADOS POR UMA DETERMINADA PESSOA
    List<Espaco> findByPessoa(Pessoa pessoa);

    // BUSCA ESPAÇOS POR NOMES
    List<Espaco> findByNomeEspacoContainingIgnoreCase(String nomeEspaco);

    // BUSCA DE ESPAÇOS POR TIPO
    List<Espaco> findByTipoEspacoContainingIgnoreCase(String tipoEspaco);

    // BUSCA DE ESPAÇO POR CIDADE
    List<Espaco> findByCidadeContainingIgnoreCase(String cidade);

    // FILTRO MÚLTIPLO SEM DATA
    @Query("""
        SELECT e FROM Espaco e
        WHERE (:nomeEspaco IS NULL OR LOWER(e.nomeEspaco) LIKE LOWER(CONCAT('%', :nomeEspaco, '%')))
        AND (:tipoEspaco IS NULL OR LOWER(e.tipoEspaco) LIKE LOWER(CONCAT('%', :tipoEspaco, '%')))
        AND (:cidade IS NULL OR LOWER(e.cidade) LIKE LOWER(CONCAT('%', :cidade, '%')))
    """)
    List<Espaco> findByFiltros(@Param("nomeEspaco") String nomeEspaco,
                               @Param("tipoEspaco") String tipoEspaco,
                               @Param("cidade") String cidade);

    // FILTRO COM DATA DO EVENTO (retorna espaços disponíveis)
    @Query("""
        SELECT e FROM Espaco e
        WHERE (:nomeEspaco IS NULL OR LOWER(e.nomeEspaco) LIKE LOWER(CONCAT('%', :nomeEspaco, '%')))
        AND (:tipoEspaco IS NULL OR LOWER(e.tipoEspaco) LIKE LOWER(CONCAT('%', :tipoEspaco, '%')))
        AND (:cidade IS NULL OR LOWER(e.cidade) LIKE LOWER(CONCAT('%', :cidade, '%')))
        AND e.idEspaco NOT IN (
            SELECT r.espaco.idEspaco FROM Reserva r
            WHERE r.dataDisponivel = :dataEvento
        )
    """)
    List<Espaco> findDisponiveisPorData(@Param("nomeEspaco") String nomeEspaco,
                                        @Param("tipoEspaco") String tipoEspaco,
                                        @Param("cidade") String cidade,
                                        @Param("dataEvento") LocalDate dataEvento);
}
