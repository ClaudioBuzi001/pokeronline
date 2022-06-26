package it.prova.pokeronline.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.CreditoMinimoException;

@RestController
@RequestMapping("api/play")
public class PlayManagementController {

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private TavoloService tavoloService;

	/**
	 * @PostMethod CompraCredito ti permette di aumentare il credito .
	 *
	 * @param Integer Di quando il tuo credito deve essere aumentato.
	 * 
	 * @return Integer Il tuo credito attuale.
	 */
	@PostMapping("/compracredito/{credito}")
	@ResponseStatus(HttpStatus.CREATED)
	public Integer compraCredito(@PathVariable(value = "credito", required = true) Integer credito) {
		// Controllo se il credito sia almeno 1
		if (credito < 1)
			throw new CreditoMinimoException(
					"Impossibile comprare credito minore di 1$, Bisogna Caricare almeno un 1$");

		// Chiamo il metodo che mi aumenta il credito
		return utenteService.compraCredito(credito);
	}

	/**
	 * Returna Un valore solo se sei ancora presente all' interno di un tavolo.
	 * 
	 * @GetMethod dammiLastGame Ti permette di vedere il tavolo in cui sei ancora
	 *            presente
	 * 
	 * @return    Tavolo il tavolo in cui stai ancora giocando
	 */
	@GetMapping("/lastGame")
	public TavoloDTO dammiLastGame() {

		Utente inSessione = utenteService
				.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

		return TavoloDTO.buildTavoloDTOFromModel(tavoloService.lastGame(inSessione.getId()), false);
	}
	
	
	/*
	 * Abbandona Partita, Prendi il tavolo in cui sei prensente, dissoci il player dal set di giocatori
	 * 
	 * 1)Prendo il tavolo in cui sto giocando
	 * 
	 * 2)Dissocio l id del utente Loggato dal dal set Di giocatori del tavolo
	 * 
	 * 3)Mi aumento l esperienza
	 * 
	 * 
	 */
	@Post
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
