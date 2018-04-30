package br.com.algamoneyapi.repository.pessoa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.algamoneyapi.model.Pessoa;
import br.com.algamoneyapi.repository.filter.PessoaFilter;

public interface PessoaRepositoryQuery {
	
	public Page<Pessoa> filtrar(PessoaFilter pessoaFilter, Pageable pageable);

}
