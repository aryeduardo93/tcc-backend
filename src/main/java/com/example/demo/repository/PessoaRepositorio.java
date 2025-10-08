package com.example.demo.repository;

import com.example.demo.modelEntity.*;
import org.springframework.data.repository.CrudRepository;


// INTERAÇÃO COM O BANCO DE DADOS
public interface PessoaRepositorio extends CrudRepository<Pessoa, Long> {

    //BUSCA UMA PESSOA PELO CAMPO EMAIL
    Pessoa findByEmail(String email);

    // VERIFICA SE JÁ EXISTE UMA PESSOA CADASTRADA COM O EMAIL INFORMADO
    boolean existsByEmail(String email);

    // BUSCA UMA PESSOA PELO EMAIL E SENHA DURANTE A AUTENTICAÇÃO
    Pessoa findByEmailAndSenha(String email, String senha);


}