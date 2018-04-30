package br.com.algamoneyapi.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.jasperreports.JasperReportsPdfView;

import br.com.algamoneyapi.event.RecursoCreatedEvent;
import br.com.algamoneyapi.exception.PessoaInexistenteOuInativaException;
import br.com.algamoneyapi.model.Lancamento;
import br.com.algamoneyapi.model.Pessoa;
import br.com.algamoneyapi.repository.LancamentoRepository;
import br.com.algamoneyapi.repository.PessoaRepository;


@Service
public class LancamentoService {

	@Autowired
	LancamentoRepository lancamentoRepository;

	@Autowired
	PessoaRepository pessoaRepository;
	
	@Autowired
    private ApplicationContext appContext;

	@Autowired
	private ApplicationEventPublisher publisher;

	public Lancamento atualizar(Long codigo, Lancamento lancamento) {
		Lancamento lancamentoSalvo = BuscaPeloCodigo(codigo);
		if (!lancamento.getPessoa().equals(lancamentoSalvo.getPessoa()))
			validPessoa(lancamento);
		BeanUtils.copyProperties(lancamento, lancamentoSalvo, "codigo");
		return lancamentoRepository.save(lancamentoSalvo);
	}

	public Lancamento atualizarDataPagamento(Long codigo, LocalDate dataPagamento) {
		Lancamento lancamentoSalvo = BuscaPeloCodigo(codigo);
		lancamentoSalvo.setDataPagamento(dataPagamento);
		return lancamentoRepository.save(lancamentoSalvo);
	}

	private Lancamento BuscaPeloCodigo(Long codigo) {
		Lancamento lancamentoSalvo = lancamentoRepository.findOne(codigo);
		if (lancamentoSalvo == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return lancamentoSalvo;
	}

	private void validPessoa(Lancamento lancamento) {
		Pessoa pessoa = null;
		Long codigo = lancamento.getPessoa().getCodigo();
		if (codigo != null) {
			pessoa = pessoaRepository.findOne(codigo);
		}
		if (pessoa == null || !pessoa.isAtivo()) {
			throw new PessoaInexistenteOuInativaException();
		}
	}

	public Lancamento atualizarObservacao(Long codigo, String observacoes) {
		Lancamento lancamentoSalvo = lancamentoRepository.findOne(codigo);
		lancamentoSalvo.setObservacao(observacoes);
		return lancamentoRepository.save(lancamentoSalvo);
	}

	public Lancamento salvar(Lancamento lancamento, HttpServletResponse response) {
		Pessoa pessoa = pessoaRepository.findOne(lancamento.getPessoa().getCodigo());
		if (!pessoa.isAtivo()) {
			throw new PessoaInexistenteOuInativaException();
		}
		Lancamento lancamentoSalvo = lancamentoRepository.save(lancamento);
		publisher.publishEvent(new RecursoCreatedEvent(this, response, lancamentoSalvo.getCodigo()));

		return lancamentoSalvo;
	}
	
	public List<Lancamento> listarLancamentos (){
		return lancamentoRepository.findAll();
	}

	public ModelAndView gerarRelatorio() {
		JasperReportsPdfView view = new JasperReportsPdfView();
		view.setUrl("classpath:relatorios/lancamentos.jrxml");
		view.setApplicationContext(appContext);
		List<Lancamento> relatorio = listarLancamentos();
		Map<String, Object> params = new HashMap<>();
		params.put("datasource", relatorio);
		return new ModelAndView(view, params);
	}
}
