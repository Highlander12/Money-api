package br.com.algamoneyapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.algamoneyapi.model.Lancamento;
import br.com.algamoneyapi.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery , PagingAndSortingRepository<Lancamento, Long>{
	
	

}
