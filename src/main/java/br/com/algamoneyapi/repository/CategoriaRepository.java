package br.com.algamoneyapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.algamoneyapi.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
