package br.edu.infnet.domingoscaldasapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import br.edu.infnet.domingoscaldasapi.domain.Graduacao;
import br.edu.infnet.domingoscaldasapi.service.GraduacaoService;

/**
 * Recurso REST do histórico de graduações. A inclusão é feita pelo
 * sub-recurso do aluno (POST /alunos/{id}/graduacoes).
 */
@RestController
@RequestMapping("/graduacoes")
public class GraduacaoController {

	private final GraduacaoService graduacaoService;

	public GraduacaoController(GraduacaoService graduacaoService) {
		this.graduacaoService = graduacaoService;
	}

	@GetMapping
	public ResponseEntity<List<Graduacao>> obterLista() {
		return ResponseEntity.ok(graduacaoService.obterLista());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Graduacao> obterPorId(@PathVariable Long id) {
		return ResponseEntity.ok(graduacaoService.obterPorId(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Graduacao> alterar(@PathVariable Long id, @Valid @RequestBody Graduacao graduacao) {
		return ResponseEntity.ok(graduacaoService.alterar(id, graduacao));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		graduacaoService.excluir(id);

		return ResponseEntity.noContent().build();
	}
}
