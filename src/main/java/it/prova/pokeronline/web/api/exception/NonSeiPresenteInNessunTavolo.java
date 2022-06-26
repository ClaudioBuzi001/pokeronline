package it.prova.pokeronline.web.api.exception;

public class NonSeiPresenteInNessunTavolo extends RuntimeException {
	public NonSeiPresenteInNessunTavolo(String mess) {
		super(mess);
	}
}
