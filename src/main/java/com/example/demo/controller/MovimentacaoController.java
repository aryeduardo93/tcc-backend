package com.example.demo.controller;

import com.example.demo.modelEntity.*;
import com.example.demo.repository.MovimentacaoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/movimentacao")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoRepositorio movimentacaoRepositorio;
    @Autowired
    private ProdutoController produtoController;
    @Autowired
    private UsuarioControlller usuarioControlller;
    @Autowired
    private CategoriaController categoriaController;
    @PostMapping(path="/add")
    public Movimentacao add(
            @RequestParam Long id_usuario,
            @RequestParam Long id_produto,
            @RequestParam TipoMovimentacao acao,
            @RequestParam int quantidade
    ){
        Optional<Produto> produto = produtoController.findById(id_produto);
        Optional<Usuario> usuario = usuarioControlller.pegarPorId(id_usuario);
        if (produto.isPresent() && usuario.isPresent()){
            Movimentacao movimentacao = new Movimentacao();
            movimentacao.setProduto(produto.get());
            movimentacao.setUsuario(usuario.get());
            movimentacao.setAcao(acao);
            movimentacao.setQuantidade(quantidade);
            movimentacaoRepositorio.save(movimentacao);
            return movimentacao;
        }else{
            return null;
        }

    }

    @GetMapping(path="/all")
    public Iterable<Movimentacao> findall(){
        return movimentacaoRepositorio.findAll();
    }

    @GetMapping(path="findByCategoria")
    public Iterable<Movimentacao> findByCategoria(
            @RequestParam Long id_categoria
    ){
        Optional<Categoria> categora = categoriaController.findById(id_categoria);
        if (categora.isPresent()){
            return movimentacaoRepositorio.findByProduto_Categoria(categora.get());
        }
        return null;
    }

    @GetMapping(path="/countByCategoria")
    public List<MovimentacaoCountDTO> getCountByCategoria(
            @RequestParam Long id_categoria
    ){
        return movimentacaoRepositorio.countByCategoria(id_categoria);
    }
}

