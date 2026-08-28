package br.edu.infnet.domingoscaldasapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.infnet.domingoscaldasapi.domain.Conquista;
import br.edu.infnet.domingoscaldasapi.domain.Medalha;

public interface ConquistaRepository extends JpaRepository<Conquista, Long> {

	List<Conquista> findByMedalha(Medalha medalha);
}
