package com.estacao.meteorologica.repository;

import com.estacao.meteorologica.model.Medicao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.time.LocalDateTime;

public interface MedicaoRepository extends JpaRepository<Medicao, Long> {
    List<Medicao> findTop50ByEstacaoNomeOrderByTimestampDesc(String nomeEstacao);

    // Novo método: busca por estação e intervalo, ordenado ascendente
    List<Medicao> findByEstacaoNomeAndTimestampBetweenOrderByTimestampAsc(
            String nomeEstacao, LocalDateTime inicio, LocalDateTime fim);
}

