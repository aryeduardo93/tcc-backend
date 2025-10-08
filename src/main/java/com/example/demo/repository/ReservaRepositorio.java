package com.example.demo.repository;

import com.example.demo.modelEntity.Reserva;
import com.example.demo.modelEntity.Espaco;
import com.example.demo.modelEntity.Pessoa;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repositório responsável pelas consultas relacionadas à entidade Reserva.
 * Contém métodos padrão e queries customizadas utilizadas pelo Dashboard.
 */
public interface ReservaRepositorio extends CrudRepository<Reserva, Integer> {

    // ========================= CONSULTAS BÁSICAS =========================

    /** Retorna todas as reservas de um determinado espaço. */
    Iterable<Reserva> findByEspaco(Espaco espaco);

    /** Retorna todas as reservas feitas por um determinado locatário (pessoa). */
    Iterable<Reserva> findByPessoa(Pessoa pessoa);

    /** Retorna todas as reservas com base no ID do espaço. */
    List<Reserva> findByEspacoIdEspaco(Integer idEspaco);

    /** 💡 NOVO/CORRIGIDO: Retorna todas as reservas de todos os espaços pertencentes a um locador. */
    List<Reserva> findByEspacoPessoaIdPessoa(Long idPessoa);

    /** Busca uma reserva por uma data específica e o ID do espaço. */
    Optional<Reserva> findByDataDisponivelAndEspaco_IdEspaco(LocalDate dataDisponivel, Integer idEspaco);

    // ========================= CONSULTAS CUSTOMIZADAS =========================

    /**
     * 🔥 Busca todas as reservas realizadas nos espaços pertencentes a um determinado locador (idPessoa),
     * dentro de um intervalo de tempo.
     * CORREÇÃO: Filtro de aprovação removido.
     */
    @Query("""
        SELECT r FROM Reserva r
        WHERE r.espaco.pessoa.id = :idPessoa
          AND r.dataDisponivel BETWEEN :inicio AND :fim
    """)
    List<Reserva> findReservasPorPeriodoLocador(
            @Param("idPessoa") Long idPessoa,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    /**
     * 🔹 Busca todas as reservas feitas por um usuário específico (locatário),
     * dentro de um intervalo de tempo.
     * CORREÇÃO: Filtro de aprovação removido.
     */
    @Query("""
        SELECT r FROM Reserva r
        WHERE r.pessoa.id = :idPessoa
          AND r.dataDisponivel BETWEEN :inicio AND :fim
    """)
    List<Reserva> findReservasPorPeriodoLocatario(
            @Param("idPessoa") Long idPessoa,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    /**
     * ⚙️ Query agregada para gerar estatísticas por espaço pertencente a um locador.
     * CORREÇÃO: Filtro de aprovação removido.
     */
    @Query("""
        SELECT 
            r.espaco.idEspaco AS idEspaco,
            r.espaco.nomeEspaco AS nomeEspaco,
            COUNT(r.idReserva) AS totalLocacoes,
            COALESCE(SUM(r.espaco.valor), 0) AS lucroTotal
        FROM Reserva r
        WHERE r.espaco.pessoa.id = :idPessoa
        GROUP BY r.espaco.idEspaco, r.espaco.nomeEspaco
    """)
    List<Object[]> buscarEstatisticasPorLocador(@Param("idPessoa") Long idPessoa);

    /**
     * ⚙️ Query agregada para gerar estatísticas das reservas FEITAS por um usuário (locatário).
     * CORREÇÃO: Filtro de aprovação removido.
     */
    @Query("""
        SELECT 
            r.espaco.idEspaco AS idEspaco,
            r.espaco.nomeEspaco AS nomeEspaco,
            COUNT(r.idReserva) AS totalLocacoes,
            COALESCE(SUM(r.espaco.valor), 0) AS valorTotalGasto
        FROM Reserva r
        WHERE r.pessoa.id = :idPessoa
        GROUP BY r.espaco.idEspaco, r.espaco.nomeEspaco
    """)
    List<Object[]> buscarEstatisticasPorLocatario(@Param("idPessoa") Long idPessoa);
}