package br.edu.infnet.domingoscaldasapi.service;

import org.springframework.stereotype.Service;

import br.edu.infnet.domingoscaldasapi.domain.Graduacao;

/**
 * Regras de negócio das graduações (CRUD herdado do serviço genérico).
 */
@Service
public class GraduacaoService extends BaseService<Graduacao> {

	/**
	 * Atualiza a graduação existente no lugar (a mesma instância está no
	 * histórico do aluno), preservando o vínculo com o aluno.
	 */
	@Override
	public Graduacao alterar(Graduacao graduacao) {
		Graduacao atual = obterPorId(graduacao.getId());
		atual.setFaixa(graduacao.getFaixa());
		atual.setGrau(graduacao.getGrau());
		atual.setData(graduacao.getData());

		return super.alterar(atual);
	}
}
