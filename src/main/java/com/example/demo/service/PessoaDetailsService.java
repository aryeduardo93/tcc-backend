package com.example.demo.service;

import com.example.demo.modelEntity.Pessoa;
import com.example.demo.repository.PessoaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class PessoaDetailsService implements UserDetailsService {

    @Autowired
    private PessoaRepositorio pessoaRepositorio;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Pessoa pessoa = pessoaRepositorio.findByEmail(email);
        if (pessoa == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + email);
        }

        return User.withUsername(pessoa.getEmail())
                .password(pessoa.getSenha())
                .authorities("USER") // ou "ADMIN" se quiser
                .build();
    }
}
