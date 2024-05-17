package com.example.demo.repository;

import com.example.demo.modelEntity.Categoria;
import com.example.demo.modelEntity.Produto;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;

public interface ProdutoRepositorio extends CrudRepository<Produto, Long> {
    Iterable<Produto> findByUpdatedAtAfter(Date dataInicio);
    Iterable<Produto> findByCategoria(Categoria categoria);
}
