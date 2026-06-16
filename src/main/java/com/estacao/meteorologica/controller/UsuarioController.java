package com.estacao.meteorologica.controller;

import com.estacao.meteorologica.model.Usuario;
import com.estacao.meteorologica.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class UsuarioController {

    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    // Injeção via construtor
    public UsuarioController(UsuarioRepository usuarioRepo,
                             PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;

    }


    @PostMapping("/usuarios")
    @PreAuthorize("hasRole('ADMIN')")   // Só usuários com role ADMIN podem acessar
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> criarUsuario(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String role = body.getOrDefault("role", "USER");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário e senha são obrigatórios");
        }

        // Verifica se já existe
        if (usuarioRepo.findByUsername(username).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Nome de usuário já existe");
        }

        // Cria e salva o novo usuário com senha criptografada
        Usuario novo = new Usuario(username, passwordEncoder.encode(password), role.toUpperCase());
        usuarioRepo.save(novo);

        return Map.of("status", "ok", "message", "Usuário criado com sucesso");
    }


    @GetMapping("/usuario/atual")
    public Map<String, String> usuarioAtual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        String role = auth.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER")          // caso inesperado, assume USER
                .replace("ROLE_", "");        // remove o prefixo
        return Map.of("username", username, "role", role);
        }

    @GetMapping("/usuarios")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Usuario> listarUsuarios() {
        return usuarioRepo.findAll();
    }

    @PutMapping("/usuarios/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> atualizarUsuario(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Usuario usuario = usuarioRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
        String novoUsername = body.get("username");
        String novoPassword = body.get("password");
        String novoRole = body.get("role");

        if (novoUsername != null && !novoUsername.isBlank()) {
            usuario.setUsername(novoUsername);
        }
        if (novoPassword != null && !novoPassword.isBlank()) {
            usuario.setPassword(passwordEncoder.encode(novoPassword));
        }
        if (novoRole != null && !novoRole.isBlank()) {
            usuario.setRole(novoRole.toUpperCase());
        }
        usuarioRepo.save(usuario);
        return Map.of("status", "ok", "message", "Usuário atualizado com sucesso");
    }

    @DeleteMapping("/usuarios/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> deletarUsuario(@PathVariable Long id) {
        if (!usuarioRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
        usuarioRepo.deleteById(id);
        return Map.of("status", "ok", "message", "Usuário removido com sucesso");
    }

}
