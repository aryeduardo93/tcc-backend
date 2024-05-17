package com.example.demo.repository;

import com.example.demo.controller.MovimentacaoCountDTO;
import com.example.demo.modelEntity.Categoria;
import com.example.demo.modelEntity.Movimentacao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface MovimentacaoRepositorio extends CrudRepository <Movimentacao, Long> {

    Iterable<Movimentacao> findByProduto_Categoria(Categoria categoria);

    @Query("select new com.example.demo.controller.MovimentacaoCountDTO(p.nome, count(*)) from Movimentacao m join Produto p on m.produto = p where p.categoria.id = :id_categoria group by p.nome")
    List<MovimentacaoCountDTO> countByCategoria(@Param("id_categoria") Long id_categoria);

}