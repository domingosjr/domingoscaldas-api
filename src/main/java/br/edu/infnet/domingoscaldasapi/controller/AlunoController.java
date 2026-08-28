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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import jakarta.validation.Valid;

import br.edu.infnet.domingoscaldasapi.domain.Aluno;
import br.edu.infnet.domingoscaldasapi.domain.Conquista;
import br.edu.infnet.domingoscaldasapi.domain.Faixa;
import br.edu.infnet.domingoscaldasapi.domain.Graduacao;
import br.edu.infnet.domingoscaldasapi.domain.Presenca;
import br.edu.infnet.domingoscaldasapi.service.AlunoService;

/**
 * Recurso REST de alunos. Só traduz HTTP <-> Java e delega as regras ao
 * {@link AlunoService}.
 */
@RestController
@RequestMapping("/alunos")
public class AlunoController {

	private final AlunoService alunoService;

	public AlunoController(AlunoService alunoService) {
		this.alunoService = alunoService;
	}

	@Operation(summary = "Lista todos os alunos", description = "Retorna todos os alunos cadastrados na escola")
	@GetMapping
	public ResponseEntity<List<Aluno>> obterLista() {
		return ResponseEntity.ok(alunoService.obterLista());
	}

	@Operation(summary = "Consulta um aluno pelo identificador")
	@GetMapping("/{id}")
	public ResponseEntity<Aluno> obterPorId(@PathVariable Long id) {
		return ResponseEntity.ok(alunoService.obterPorId(id));
	}

	@GetMapping(params = "nome")
	public ResponseEntity<List<Aluno>> obterPorNome(@Parameter(description = "Trecho do nome do aluno") @RequestParam String nome) {
		return ResponseEntity.ok(alunoService.buscarPorNome(nome));
	}

	@GetMapping(params = "faixa")
	public ResponseEntity<List<Aluno>> obterPorFaixa(@RequestParam Faixa faixa) {
		return ResponseEntity.ok(alunoService.obterPorFaixa(faixa));
	}

	@GetMapping(params = "ativos")
	public ResponseEntity<List<Aluno>> obterAtivos(@RequestParam boolean ativos) {
		return ResponseEntity.ok(ativos ? alunoService.obterAtivos() : alunoService.obterLista());
	}

	@GetMapping(params = "aptosGraduacao")
	public ResponseEntity<List<Aluno>> obterAptosParaGraduacao(@RequestParam boolean aptosGraduacao) {
		return ResponseEntity.ok(aptosGraduacao ? alunoService.obterAptosParaGraduacao() : alunoService.obterLista());
	}

	@GetMapping(params = "ordenarPor=frequencia")
	public ResponseEntity<List<Aluno>> ordenarPorFrequencia() {
		return ResponseEntity.ok(alunoService.ordenarPorFrequencia());
	}

	@GetMapping("/{id}/pontos-graduacao")
	public ResponseEntity<Long> obterPontosGraduacao(@PathVariable Long id) {
		return ResponseEntity.ok(alunoService.calcularPontosDesdeUltimaGraduacao(alunoService.obterPorId(id)));
	}

	@PostMapping
	public ResponseEntity<Aluno> incluir(@Valid @RequestBody Aluno aluno) {
		Aluno incluido = alunoService.incluir(aluno);

		return ResponseEntity.created(montarLocation(incluido.getId())).body(incluido);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Aluno> alterar(@PathVariable Long id, @Valid @RequestBody Aluno aluno) {
		return ResponseEntity.ok(alunoService.alterar(id, aluno));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		alunoService.excluir(id);

		return ResponseEntity.noContent().build();
	}

	// ----- sub-recursos do aluno (relacionamentos um-para-muitos) -----

	@GetMapping("/{id}/presencas")
	public ResponseEntity<List<Presenca>> obterPresencas(@PathVariable Long id) {
		return ResponseEntity.ok(alunoService.obterPorId(id).getPresencas());
	}

	@PostMapping("/{id}/presencas")
	public ResponseEntity<Presenca> registrarPresenca(@PathVariable Long id, @Valid @RequestBody Presenca presenca) {
		Presenca registrada = alunoService.registrarPresenca(id, presenca);

		return ResponseEntity.created(montarLocation("/presencas", registrada.getId())).body(registrada);
	}

	@GetMapping("/{id}/conquistas")
	public ResponseEntity<List<Conquista>> obterConquistas(@PathVariable Long id) {
		return ResponseEntity.ok(alunoService.obterPorId(id).getConquistas());
	}

	@PostMapping("/{id}/conquistas")
	public ResponseEntity<Conquista> registrarConquista(@PathVariable Long id, @RequestParam Long campeonatoId,
			@Valid @RequestBody Conquista conquista) {
		Conquista registrada = alunoService.registrarConquista(id, campeonatoId, conquista);

		return ResponseEntity.created(montarLocation("/conquistas", registrada.getId())).body(registrada);
	}

	@GetMapping("/{id}/graduacoes")
	public ResponseEntity<List<Graduacao>> obterGraduacoes(@PathVariable Long id) {
		return ResponseEntity.ok(alunoService.obterPorId(id).getGraduacoes());
	}

	@PostMapping("/{id}/graduacoes")
	public ResponseEntity<Graduacao> registrarGraduacao(@PathVariable Long id, @Valid @RequestBody Graduacao graduacao) {
		Graduacao registrada = alunoService.registrarGraduacao(id, graduacao);

		return ResponseEntity.created(montarLocation("/graduacoes", registrada.getId())).body(registrada);
	}

	private URI montarLocation(Long id) {
		return ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
	}

	/**
	 * Location dos sub-recursos aponta para o recurso próprio (ex.: /presencas/{id}).
	 */
	private URI montarLocation(String recurso, Long id) {
		return ServletUriComponentsBuilder.fromCurrentContextPath().path(recurso).path("/{id}").buildAndExpand(id).toUri();
	}
}
