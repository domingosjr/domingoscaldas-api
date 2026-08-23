package br.edu.infnet.domingoscaldasapi.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.infnet.domingoscaldasapi.domain.Presenca;
import br.edu.infnet.domingoscaldasapi.service.PresencaService;

/**
 * Recurso REST de presenças. A inclusão é feita pelo sub-recurso do aluno
 * (POST /alunos/{id}/presencas), pois toda presença pertence a um aluno.
 */
@RestController
@RequestMapping("/presencas")
public class PresencaController {

	private final PresencaService presencaService;

	public PresencaController(PresencaService presencaService) {
		this.presencaService = presencaService;
	}

	@GetMapping
	public ResponseEntity<List<Presenca>> obterLista() {
		return ResponseEntity.ok(presencaService.obterLista());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Presenca> obterPorId(@PathVariable Long id) {
		return ResponseEntity.ok(presencaService.obterPorId(id));
	}

	@GetMapping(params = { "inicio", "fim" })
	public ResponseEntity<List<Presenca>> obterPorPeriodo(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
		return ResponseEntity.ok(presencaService.obterPorPeriodo(inicio, fim));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Presenca> alterar(@PathVariable Long id, @RequestBody Presenca presenca) {
		presenca.setId(id);

		return ResponseEntity.ok(presencaService.alterar(presenca));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		presencaService.excluir(id);

		return ResponseEntity.noContent().build();
	}
}
