package br.com.algamoneyapi.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import br.com.algamoneyapi.model.Pessoa;
import br.com.algamoneyapi.repository.PessoaRepository;
import br.com.algamoneyapi.repository.filter.PessoaFilter;
import br.com.algamoneyapi.service.PessoaService;

@RestController
@RequestMapping(value = "/pessoas")
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@GetMapping("/listar")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public ResponseEntity<Page<Pessoa>> Lista(PessoaFilter pessoaFilter, Pageable pageable) {
		return new ResponseEntity<>(pessoaRepository.filtrar(pessoaFilter, pageable), HttpStatus.OK);
	}

	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa Pessoa, HttpServletResponse response) {
		Pessoa PessoaSalva = pessoaRepository.save(Pessoa);

		publisher.publishEvent(new RecursoCreatedEvent(this, response, PessoaSalva.getCodigo()));
		return new ResponseEntity<>(PessoaSalva, HttpStatus.CREATED);
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_PESSOA') and #oauth2.hasScope('read')")
	public ResponseEntity<Pessoa> buscar(@PathVariable(required = true) Long codigo) {
		Pessoa pessoa = pessoaRepository.findOne(codigo);
		return pessoa == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(pessoa);
	}

	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Pessoa> deletar(@PathVariable(required = true) Long codigo) {
		pessoaRepository.delete(codigo);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_ALTERAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Pessoa> alterar(@PathVariable(required = true) Long codigo,
			@Valid @RequestBody Pessoa pessoa) {
		return new ResponseEntity<>(pessoaService.atualizar(codigo, pessoa), HttpStatus.OK);
	}

	@PutMapping("/{codigo}/ativo")
	@PreAuthorize("hasAuthority('ROLE_ALTERAR_PESSOA') and #oauth2.hasScope('write')")
	public ResponseEntity<Pessoa> alterarPropriedadeAtivo(@PathVariable(required = true) Long codigo,
			@RequestBody Boolean ativo) {
		return new ResponseEntity<>(pessoaService.atualizarAtivo(codigo, ativo), HttpStatus.OK);
	}

}
