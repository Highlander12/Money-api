package br.com.algamoneyapi.controller;

import java.time.LocalDate;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.servlet.ModelAndView;

import br.com.algamoneyapi.model.Lancamento;
import br.com.algamoneyapi.repository.LancamentoRepository;
import br.com.algamoneyapi.repository.filter.LancamentoFilter;
import br.com.algamoneyapi.repository.projection.ResumoLancamento;
import br.com.algamoneyapi.service.LancamentoService;

@RestController
@RequestMapping(value = "/lancamentos")
public class LancamentoController {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
    
	@Autowired
	private LancamentoService lancamentoService;
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Page<Lancamento>> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable){
		return new ResponseEntity<>(lancamentoRepository.filtrar(lancamentoFilter,pageable), HttpStatus.OK);
	}
	
	@GetMapping(params = "resumo")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Page<ResumoLancamento>> resumir(LancamentoFilter lancamentoFilter, Pageable pageable){
		return new ResponseEntity<>(lancamentoRepository.resumir(lancamentoFilter,pageable), HttpStatus.OK);
	}
	
	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Lancamento> buscar(@PathVariable(required = true) Long codigo) {
		Lancamento lancamento = lancamentoRepository.findOne(codigo);
		return lancamento == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(lancamento);
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> criar(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		return new ResponseEntity<>(lancamentoService.salvar(lancamento, response), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and #oauth2.hasScope('read')")
	public ResponseEntity<Lancamento> delete(@PathVariable(required = true) Long codigo) {
		lancamentoRepository.delete(codigo);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_ALTERAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> alterar(@PathVariable(required = true) Long codigo,
			@Valid @RequestBody Lancamento lancamento) {
		return new ResponseEntity<>(lancamentoService.atualizar(codigo, lancamento), HttpStatus.OK);
	}
	
	@PutMapping("/{codigo}/data-pagamento")
	@PreAuthorize("hasAuthority('ROLE_ALTERAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> alterarDataPagamento(@PathVariable(required = true) Long codigo,
			@RequestBody LocalDate datapagamento) {
		return new ResponseEntity<>(lancamentoService.atualizarDataPagamento(codigo, datapagamento), HttpStatus.OK);
	}
	
	@PutMapping("/{codigo}/observacao")
	@PreAuthorize("hasAuthority('ROLE_ALTERAR_LANCAMENTO') and #oauth2.hasScope('write')")
	public ResponseEntity<Lancamento> alterarObservacao(@PathVariable (required = true) Long codigo,
			@RequestBody String observacoes){
		return new ResponseEntity<>(lancamentoService.atualizarObservacao(codigo, observacoes), HttpStatus.OK);
	}
	
	@GetMapping(value="/pdf")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and #oauth2.hasScope('read')")
	public ModelAndView relatorio() {
		ModelAndView pdf =  lancamentoService.gerarRelatorio();
		return pdf;
	}
}
