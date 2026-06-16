package com.estacao.meteorologica.repository;

import com.estacao.meteorologica.model.Estacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
/*
extends JpaRepository<Estacao, Long>: Herda métodos como save(), findAll(), findById().
Os tipos significam: entidade gerenciada = Estacao, tipo do ID = Long.

Optional<Estacao> findByNome(String nome): o Spring Data entende “findBy” + “Nome” (campo da entidade) e
 monta a consulta WHERE nome = ?. Retorna Optional podendo existir ou não.
 */

public interface EstacaoRepository extends JpaRepository<Estacao, Long> {
    Optional<Estacao> findByNome(String nome);
}