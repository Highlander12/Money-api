package br.com.algamoneyapi.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.algamoneyapi.model.Lancamento;
import br.com.algamoneyapi.repository.filter.LancamentoFilter;
import br.com.algamoneyapi.repository.projection.ResumoLancamento;

public interface LancamentoRepositoryQuery {
	
	public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);
    public Page<ResumoLancamento> resumir (LancamentoFilter lancamentoFilter, Pageable pageable);
}
