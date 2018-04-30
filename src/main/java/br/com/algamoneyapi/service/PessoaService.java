package br.com.algamoneyapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.algamoneyapi.model.Pessoa;
import br.com.algamoneyapi.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	PessoaRepository pessoaRepository;

	public Pessoa atualizar(Long codigo, Pessoa pessoa) {
		Pessoa pessoaSalva = BuscaPeloCodigo(codigo);
		BeanUtils.copyProperties(pessoa, pessoaSalva, "codigo");
		return pessoaRepository.save(pessoaSalva);
	}

	public Pessoa atualizarAtivo(Long codigo, Boolean ativo) {
		Pessoa pessoaSalva = BuscaPeloCodigo(codigo);
		pessoaSalva.setAtivo(ativo);
		return pessoaRepository.save(pessoaSalva);
	}

	private Pessoa BuscaPeloCodigo(Long codigo) {
		Pessoa pessoaSalva = pessoaRepository.findOne(codigo);
		if (pessoaSalva == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return pessoaSalva;
	}
}
