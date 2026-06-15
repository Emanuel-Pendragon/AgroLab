package com.estacao.meteorologica;

import com.estacao.meteorologica.model.Estacao;
import com.estacao.meteorologica.model.Usuario;
import com.estacao.meteorologica.repository.EstacaoRepository;
import com.estacao.meteorologica.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EstacaoRepository estacaoRepo;
    private final UsuarioRepository usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(EstacaoRepository estacaoRepo,
                           UsuarioRepository usuarioRepo,
                           PasswordEncoder passwordEncoder) {
        this.estacaoRepo = estacaoRepo;
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // Busca a estação IEMA
        Optional<Estacao> optEstacao = estacaoRepo.findByNome("IEMA");
        Estacao iema;

        // Coordenadas oficiais do IEMA Colinas
        final double LATITUDE = -6.062503856425026;
        final double LONGITUDE = -44.253686782930906;

        if (optEstacao.isPresent()) {
            iema = optEstacao.get();
            // Atualiza coordenadas e localização
            iema.setLatitude(LATITUDE);
            iema.setLongitude(LONGITUDE);
            iema.setLocalizacao("IEMA Colinas");
            estacaoRepo.save(iema);
            System.out.println("Estação IEMA Colinas atualizada com coordenadas.");
        } else {
            // Cria a estação com coordenadas
            iema = new Estacao("IEMA", "IEMA Colinas");
            iema.setLatitude(LATITUDE);
            iema.setLongitude(LONGITUDE);
            estacaoRepo.save(iema);
            System.out.println("Estação IEMA Colinas criada com coordenadas.");
        }

        // Cria o usuário admin, se não existir
        if (usuarioRepo.findByUsername("admin").isEmpty()) {
            Usuario admin = new Usuario("admin", passwordEncoder.encode("123456"), "ADMIN");
            usuarioRepo.save(admin);
            System.out.println("Usuário admin criado automaticamente.");
        }
    }
}