package com.example.demo.controller;

import com.example.demo.modelEntity.Usuario;
import com.example.demo.repository.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping(path="/usuario")
public class UsuarioControlller {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @PostMapping(path="/add")
    public String addNewUser(
            @RequestParam String nome,
            @RequestParam String email,
            @RequestParam String senha,
            @RequestParam @DateTimeFormat(pattern="dd/MM/yyyy") Date dataNascimento
    ){
        if (usuarioRepositorio.findByEmail(email) != null){
            return "Email já existe";
        }else {
            Usuario usuario = new Usuario();
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(senha);
            usuario.setDataNascimento(dataNascimento);
            usuarioRepositorio.save(usuario);
            return usuario + " salvo";
        }
    }

    @GetMapping(path="/all")
    public Iterable<Usuario> getAllUsers(){
        return usuarioRepositorio.findAll();
    }

    @GetMapping(path="/pegarporemail")
    public Usuario pegarPorEmail(
            @RequestParam String email
    ){
        return usuarioRepositorio.findByEmail(email);
    }

    @PostMapping(path="/verificalogin")
    public String verificaLogin(
            @RequestParam String email,
            @RequestParam String senha
    ){
        Usuario usuario = usuarioRepositorio.findByEmailAndSenha(email, senha);
        if(usuario != null){
            usuario.setUltimoLogin(new Date(System.currentTimeMillis()));
            usuarioRepositorio.save(usuario);
            return "Logou";
        }else{
            return "Não Logou";
        }
    }

    @GetMapping(path="/nascimento")
    public Iterable<Usuario> pegarAniversariantes(
            @RequestParam @DateTimeFormat(pattern="dd/MM/yyyy") Date dataInicio,
            @RequestParam @DateTimeFormat(pattern="dd/MM/yyyy") Date dataFim
    ){
        return usuarioRepositorio.findByDataNascimentoIsBetween(dataInicio, dataFim);
    }

    @GetMapping(path="/{id}")
    public Optional<Usuario> pegarPorId(
            @PathVariable Long id
    ){
        return usuarioRepositorio.findById(id);
    }
    @PostMapping(path="/atualizardatanascimento")
    public String atualizarDataNascimento(
            @RequestParam Long id,
            @RequestParam @DateTimeFormat(pattern="dd/MM/yyyy") Date dataNascimento
    ){
        Optional <Usuario> usuario = usuarioRepositorio.findById(id);
        if(usuario.isPresent()){
            usuario.get().setDataNascimento(dataNascimento);
            usuarioRepositorio.save(usuario.get());
            return "Atualizou";
        }else{
            return "Não Encontrou";
        }
    }

    @GetMapping(path="/pegarlogados")
    public Iterable<Usuario> getLogados(){
        Date lastDay = new Date(System.currentTimeMillis() - (1000*3600*24));
        return usuarioRepositorio.findByUltimoLoginAfter(lastDay);
    }
}
