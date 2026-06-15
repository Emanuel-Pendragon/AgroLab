package com.estacao.meteorologica.controller;

import com.estacao.meteorologica.model.Estacao;
import com.estacao.meteorologica.model.Medicao;
import com.estacao.meteorologica.repository.EstacaoRepository;
import com.estacao.meteorologica.repository.MedicaoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DadosController {

    private final MedicaoRepository medicaoRepo;
    private final EstacaoRepository estacaoRepo;

    // Não precisamos mais do serviço de cálculos
    public DadosController(MedicaoRepository medicaoRepo,
                           EstacaoRepository estacaoRepo) {
        this.medicaoRepo = medicaoRepo;
        this.estacaoRepo = estacaoRepo;
    }

    // Listar todas as estações
    @GetMapping("/estacoes")
    public List<Estacao> listarEstacoes() {
        return estacaoRepo.findAll();
    }

    // Receber medição do ESP32 (POST)
    @PostMapping("/dados")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> receberDados(@RequestBody Map<String, Object> payload) {
        String nomeEstacao = (String) payload.get("estacao");
        if (nomeEstacao == null) {
            throw new RuntimeException("Campo 'estacao' obrigatório");
        }

        Estacao estacao = estacaoRepo.findByNome(nomeEstacao)
                .orElseGet(() -> {
                    Estacao nova = new Estacao(nomeEstacao, null);
                    return estacaoRepo.save(nova);
                });

        Medicao medicao = new Medicao();
        medicao.setTimestamp(LocalDateTime.now());
        medicao.setTemperatura(getDouble(payload, "temperature"));
        medicao.setUmidade(getDouble(payload, "humidity"));
        medicao.setPressao(getDouble(payload, "pressure"));
        medicao.setLuminosidade(getDouble(payload, "solar"));      // irradiância
        medicao.setChuva(getDouble(payload, "rain"));
        medicao.setVelocidadeVento(getDouble(payload, "wind_avg"));
        medicao.setDirecaoVento(getDouble(payload, "direction"));
        medicao.setUmidadeSolo(getDouble(payload, "umidadeSolo"));
        medicao.setEstacao(estacao);

        // Cálculos agronômicos REMOVIDOS – salvamos apenas os dados brutos
        medicaoRepo.save(medicao);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", "ok");
        resposta.put("id", medicao.getId());
        return resposta;
    }

    // Listar últimas 50 medições de uma estação
    @GetMapping("/dados")
    public List<Medicao> listarDados(@RequestParam String estacao,
                                     @RequestParam Optional<String> inicio,
                                     @RequestParam Optional<String> fim) {
        if (inicio.isPresent() && fim.isPresent()) {
            LocalDateTime dataInicio = LocalDateTime.parse(inicio.get(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime dataFim = LocalDateTime.parse(fim.get(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return medicaoRepo.findByEstacaoNomeAndTimestampBetweenOrderByTimestampAsc(estacao, dataInicio, dataFim);
        } else {
            return medicaoRepo.findTop50ByEstacaoNomeOrderByTimestampDesc(estacao);
        }

    }

    // Método auxiliar para extrair Double do mapa
    private Double getDouble(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) return null;
        if (value instanceof Number) return ((Number) value).doubleValue();
        return Double.parseDouble(value.toString());
    }

}