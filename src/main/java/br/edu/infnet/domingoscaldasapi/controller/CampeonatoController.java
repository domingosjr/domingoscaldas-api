package br.edu.infnet.domingoscaldasapi.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.infnet.domingoscaldasapi.domain.Campeonato;
import br.edu.infnet.domingoscaldasapi.service.CampeonatoService;

/**
 * Recurso REST de campeonatos.
 */
@RestController
@RequestMapping("/campeonatos")
public class CampeonatoController {

	private final CampeonatoService campeonatoService;

	public CampeonatoController(CampeonatoService campeonatoService) {
		this.campeonatoService = campeonatoService;
	}

	@GetMapping
	public ResponseEntity<List<Campeonato>> obterLista() {
		return ResponseEntity.ok(campeonatoService.obterLista());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Campeonato> obterPorId(@PathVariable Long id) {
		return ResponseEntity.ok(campeonatoService.obterPorId(id));
	}

	@PostMapping
	public ResponseEntity<Campeonato> incluir(@RequestBody Campeonato campeonato) {
		Campeonato incluido = campeonatoService.incluir(campeonato);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(incluido.getId()).toUri();

		return ResponseEntity.created(location).body(incluido);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Campeonato> alterar(@PathVariable Long id, @RequestBody Campeonato campeonato) {
		campeonato.setId(id);

		return ResponseEntity.ok(campeonatoService.alterar(campeonato));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		campeonatoService.excluir(id);

		return ResponseEntity.noContent().build();
	}
}
