package it.prova.pokeronline.web.api.exception;

public class CreditoInsufficenteException extends RuntimeException {

	public CreditoInsufficenteException(String mess) {
		super(mess);
	}
}
