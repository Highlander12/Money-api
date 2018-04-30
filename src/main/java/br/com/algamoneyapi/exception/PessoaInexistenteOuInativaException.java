package br.com.algamoneyapi.exception;

public class PessoaInexistenteOuInativaException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public PessoaInexistenteOuInativaException() {
		super("Operacao não permitida pessoa inexistente ou inativa.");
	}


}
