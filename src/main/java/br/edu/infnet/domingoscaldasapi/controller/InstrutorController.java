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

import br.edu.infnet.domingoscaldasapi.domain.Instrutor;
import br.edu.infnet.domingoscaldasapi.service.InstrutorService;

/**
 * Recurso REST de instrutores.
 */
@RestController
@RequestMapping("/instrutores")
public class InstrutorController {

	private final InstrutorService instrutorService;

	public InstrutorController(InstrutorService instrutorService) {
		this.instrutorService = instrutorService;
	}

	@GetMapping
	public ResponseEntity<List<Instrutor>> obterLista() {
		return ResponseEntity.ok(instrutorService.obterLista());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Instrutor> obterPorId(@PathVariable Long id) {
		return ResponseEntity.ok(instrutorService.obterPorId(id));
	}

	@PostMapping
	public ResponseEntity<Instrutor> incluir(@RequestBody Instrutor instrutor) {
		Instrutor incluido = instrutorService.incluir(instrutor);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(incluido.getId()).toUri();

		return ResponseEntity.created(location).body(incluido);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Instrutor> alterar(@PathVariable Long id, @RequestBody Instrutor instrutor) {
		instrutor.setId(id);

		return ResponseEntity.ok(instrutorService.alterar(instrutor));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		instrutorService.excluir(id);

		return ResponseEntity.noContent().build();
	}
}
