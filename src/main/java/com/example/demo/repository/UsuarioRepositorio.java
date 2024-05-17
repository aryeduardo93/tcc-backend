package com.example.demo.repository;

import com.example.demo.modelEntity.*;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;


public interface UsuarioRepositorio extends CrudRepository<Usuario, Long> {
    Usuario findByEmail(String email);
    boolean existsByEmail(String email);

    Usuario findByEmailAndSenha(String email, String senha);

    Iterable<Usuario> findByDataNascimentoIsBetween(Date start, Date end);

    Iterable<Usuario> findByUltimoLoginAfter(Date lastDay);
}
