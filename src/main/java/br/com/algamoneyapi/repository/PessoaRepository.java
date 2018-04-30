package br.com.algamoneyapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.algamoneyapi.model.Pessoa;
import br.com.algamoneyapi.repository.pessoa.PessoaRepositoryQuery;

public interface PessoaRepository extends JpaRepository<Pessoa, Long>, PessoaRepositoryQuery{
	

}
