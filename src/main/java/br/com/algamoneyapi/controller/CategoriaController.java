package br.com.algamoneyapi.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import br.com.algamoneyapi.event.RecursoCreatedEvent;
import br.com.algamoneyapi.model.Categoria;
import br.com.algamoneyapi.repository.CategoriaRepository;
import br.com.algamoneyapi.service.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")

public class CategoriaController {
	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private CategoriaService categoriaService;

	@GetMapping("/listar")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public ResponseEntity<List<Categoria>> Lista() {
		return new ResponseEntity<>(categoriaRepository.findAll(), HttpStatus.OK);
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_CATEGORIA') and #oauth2.hasScope('read')")
	public ResponseEntity<Categoria> buscar(@PathVariable(required = true) Long codigo) {
		Categoria categoria = categoriaRepository.findOne(codigo);
		return categoria == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(categoria);
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_CATEGORIA') and #oauth2.hasScope('write')")
	public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		Categoria categoriaSalva = categoriaRepository.save(categoria);
		publisher.publishEvent(new RecursoCreatedEvent(this, response, categoriaSalva.getCodigo()));
		return new ResponseEntity<>(categoriaSalva, HttpStatus.CREATED);
	}


	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_DELETAR_CATEGORIA') and #oauth2.hasScope('write')")
	public ResponseEntity<Categoria> delete(@PathVariable(required = true) Long codigo) {
		categoriaRepository.delete(codigo);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	} 
	
	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_ALTERAR_CATEGORIA') and #oauth2.hasScope('write')")
	public ResponseEntity<Categoria> alterar(@PathVariable(required = true) Long codigo,
			@Valid @RequestBody Categoria categoria) {
		return new ResponseEntity<>(categoriaService.atualizar(codigo, categoria), HttpStatus.OK);
	}
}
