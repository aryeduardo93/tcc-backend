package com.example.demo.controller;

import com.example.demo.modelEntity.Categoria;
import com.example.demo.modelEntity.Produto;
import com.example.demo.repository.CategoriaRepositorio;
import com.example.demo.repository.ProdutoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping(path="/produto")
public class ProdutoController {
    @Autowired
    ProdutoRepositorio produtoRepositorio;

    @Autowired
    CategoriaRepositorio categoriaRepositorio;

    @PostMapping(path="/add")
    public String add(
            @RequestParam String nome,
            @RequestParam String descricao,
            @RequestParam int quantidadeEstoque,
            @RequestParam Long fkIdCategoria
    ){
        Optional<Categoria> categoria = categoriaRepositorio.findById(fkIdCategoria);
        if(categoria.isPresent()){
            Produto produto = new Produto();
            produto.setNome(nome);
            produto.setDescricao(descricao);
            produto.setQuantidadeEstoque(quantidadeEstoque);
            produto.setCategoria(categoria.get());
            produtoRepositorio.save(produto);
            return produto + "[Salvo]";
        }else{
            return "[Erro] - Categoria não encontrada";
        }

    }

    @GetMapping(path="findById")
    public Optional<Produto> findById(
            @RequestParam Long id
    ){
        return produtoRepositorio.findById(id);
    }

    @GetMapping(path="/all")
    public Iterable<Produto> findAll(){
        return produtoRepositorio.findAll();
    }

    @PostMapping(path="/updateEstoque")
    public String udpateEstoque(
            @RequestParam Long id,
            @RequestParam int quantidadeEstoque
    ){
        Optional<Produto> produto = produtoRepositorio.findById(id);
        if (produto.isPresent()){
            produto.get().setQuantidadeEstoque(quantidadeEstoque);
            produtoRepositorio.save(produto.get());
            return "Atualizado";
        }else{
            return "Produto não encontrado";
        }
    }

    @GetMapping(path="/alterados")
    public Iterable<Produto> findAlterados(){
        Date lastDay = new Date(System.currentTimeMillis() - (3600 * 1000 * 24));
        return produtoRepositorio.findByUpdatedAtAfter(lastDay);
    }

    @GetMapping(path="/pegarporcategoria")
    public Iterable<Produto> pegarPorCategoria(
            @RequestParam Long fkIdCategoria
    ){
        Optional<Categoria> categoria = categoriaRepositorio.findById(fkIdCategoria);
        if(categoria.isPresent()) {
            return produtoRepositorio.findByCategoria(categoria.get());
        } else{
          return null;
        }
    }
}
