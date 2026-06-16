package com.estacao.meteorologica.model;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role = "USER"; // Default: USER, pode ser ADMIN

    // Construtores
    public Usuario() {}

    public Usuario(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // ---------- Métodos obrigatórios da interface UserDetails ----------
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Converte a role ("ADMIN" ou "USER") em uma autoridade reconhecida pelo Spring ("ROLE_ADMIN", "ROLE_USER")
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }

    @Override
    public String getPassword() { return password; }

    @Override
    public String getUsername() { return username; }

    // Os próximos métodos indicam se a conta está ativa, expirada, bloqueada, etc.
    // Vamos retornar true para todos, significando que a conta está sempre válida.
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

    // ---------- Getters e Setters ----------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}