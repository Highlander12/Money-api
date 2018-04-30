package br.com.algamoneyapi.event.listener;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.algamoneyapi.event.RecursoCreatedEvent;

@Component
public class RecursoCreatedListener implements ApplicationListener<RecursoCreatedEvent> {

	@Override
	public void onApplicationEvent(RecursoCreatedEvent recursoCreatedEvent) {
		HttpServletResponse response = recursoCreatedEvent.getResponse();
		Long codigo = recursoCreatedEvent.getCodigo();
		AdicionarHeaderLocation(response, codigo);
	}

	private void AdicionarHeaderLocation(HttpServletResponse response, Long codigo) {
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{codigo}").buildAndExpand(codigo).toUri();
		response.setHeader("Location", uri.toASCIIString());
	}

}
