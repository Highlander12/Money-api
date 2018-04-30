package br.com.algamoneyapi.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.algamoneyapi.model.Categoria;
import br.com.algamoneyapi.repository.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	CategoriaRepository categoriaRepository;

	public Categoria atualizar(Long codigo, Categoria categoria) {
		Categoria categoriaSalva = BuscaPeloCodigo(codigo);
		BeanUtils.copyProperties(categoria, categoriaSalva, "codigo");
		return categoriaRepository.save(categoriaSalva);
	}

	private Categoria BuscaPeloCodigo(Long codigo) {
		Categoria categoriaSalva = categoriaRepository.findOne(codigo);
		if (categoriaSalva == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return categoriaSalva;
	}
}