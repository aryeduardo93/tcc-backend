package com.example.demo.config;

import com.example.demo.filter.JwtAuthenticationFilter;
import com.example.demo.service.PessoaDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// CLASSE QUE EXIBE AS ROTAS
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final PessoaDetailsService pessoaDetailsService;

    public SecurityConfig(PessoaDetailsService pessoaDetailsService) {
        this.pessoaDetailsService = pessoaDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/pessoa/add").permitAll()
                        .requestMatchers("/pessoa/perfil/**").authenticated()
                        .requestMatchers("/pessoa/update").authenticated()
                        .requestMatchers("/espaco/add").authenticated()
                        .requestMatchers("/espaco/update/**").authenticated()
                        .requestMatchers("/espaco/delete/**").authenticated()
                        .requestMatchers("/reserva/detalhes/porEspaco/**").authenticated()
                        .requestMatchers("/pessoa/all").permitAll()
                        .requestMatchers("/espaco/all").permitAll()
                        .requestMatchers("/espaco/findById").permitAll()
                        .requestMatchers("/espaco/findByNome").permitAll()
                        .requestMatchers("/espaco/findByTipo").permitAll()
                        .requestMatchers("/espaco/findByCidade").permitAll()
                        .requestMatchers("/espaco/find").permitAll()
                        .requestMatchers("/espaco/buscarPorIdEspaco/**").permitAll()
                        .requestMatchers("/fotosEspaco/porEspaco/**").permitAll()
                        .requestMatchers("/fotosEspaco/upload").permitAll()
                        .requestMatchers("fotosEspaco/delete/**").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/espaco/porCliente/**").authenticated()
                        .requestMatchers("/reserva/add").permitAll()
                        .requestMatchers("/reserva/porEspaco/**").permitAll()
                        .requestMatchers("/pessoa/perfil/**").authenticated()
                        .requestMatchers("/espaco/coordenadas").permitAll()
                        .requestMatchers("/reserva/excluir").permitAll()
                        .requestMatchers("/reserva/estatisticas/**").permitAll()
                        .requestMatchers("/reserva/ocupacao/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Opcional, mas útil caso você precise do AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
