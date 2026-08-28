package br.edu.infnet.domingoscaldasapi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import br.edu.infnet.domingoscaldasapi.domain.Conquista;
import br.edu.infnet.domingoscaldasapi.domain.Medalha;
import br.edu.infnet.domingoscaldasapi.service.ConquistaService;

/**
 * Recurso REST de conquistas em campeonatos. A inclusão é feita pelo
 * sub-recurso do aluno (POST /alunos/{id}/conquistas?campeonatoId=...).
 */
@RestController
@RequestMapping("/conquistas")
public class ConquistaController {

	private final ConquistaService conquistaService;

	public ConquistaController(ConquistaService conquistaService) {
		this.conquistaService = conquistaService;
	}

	@GetMapping
	public ResponseEntity<List<Conquista>> obterLista() {
		return ResponseEntity.ok(conquistaService.obterLista());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Conquista> obterPorId(@PathVariable Long id) {
		return ResponseEntity.ok(conquistaService.obterPorId(id));
	}

	@GetMapping(params = "medalha")
	public ResponseEntity<List<Conquista>> obterPorMedalha(@RequestParam Medalha medalha) {
		return ResponseEntity.ok(conquistaService.obterPorMedalha(medalha));
	}

	@GetMapping("/quadro-medalhas")
	public ResponseEntity<Map<Medalha, Long>> obterQuadroDeMedalhas() {
		return ResponseEntity.ok(conquistaService.obterQuadroDeMedalhas());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Conquista> alterar(@PathVariable Long id, @Valid @RequestBody Conquista conquista) {
		return ResponseEntity.ok(conquistaService.alterar(id, conquista));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		conquistaService.excluir(id);

		return ResponseEntity.noContent().build();
	}
}
