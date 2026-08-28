package br.edu.infnet.domingoscaldasapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.infnet.domingoscaldasapi.domain.Presenca;

public interface PresencaRepository extends JpaRepository<Presenca, Long> {

	List<Presenca> findByDataBetween(LocalDate inicio, LocalDate fim);
}
