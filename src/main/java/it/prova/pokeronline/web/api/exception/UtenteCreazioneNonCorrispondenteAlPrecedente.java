package it.prova.pokeronline.web.api.exception;

public class UtenteCreazioneNonCorrispondenteAlPrecedente extends RuntimeException {
	public UtenteCreazioneNonCorrispondenteAlPrecedente(String mess) {
		super(mess);
	}
}
