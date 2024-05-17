package com.example.demo.controller;

import com.example.demo.modelEntity.Categoria;
import com.example.demo.repository.CategoriaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/categoria")
public class CategoriaController {
    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    @PostMapping(path = "/add")
    public String add(
            @RequestParam String nome
    ){
        Categoria categoria = new Categoria();
        categoria.setNome(nome);
        categoriaRepositorio.save(categoria);
        return "Categoria Adicionada";
    }

    @GetMapping(path="/all")
    public Iterable<Categoria> getAll(){
        return categoriaRepositorio.findAll();
    }

    @GetMapping(path="/findById")
    public Optional<Categoria> findById(
            @RequestParam Long id
    ){
        return categoriaRepositorio.findById(id);
    }
}
