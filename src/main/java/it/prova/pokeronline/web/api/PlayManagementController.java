package it.prova.pokeronline.web.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.CreditoInsufficenteException;
import it.prova.pokeronline.web.api.exception.CreditoMinimoException;
import it.prova.pokeronline.web.api.exception.EsperienzaAccomulataNonSufficente;

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
	@ResponseStatus(HttpStatus.OK)
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
	 * @return Tavolo il tavolo in cui stai ancora giocando
	 */
	@GetMapping("/lastGame")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public TavoloDTO dammiLastGame() {

		Utente inSessione = utenteService
				.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

		return TavoloDTO.buildTavoloDTOFromModelNoPassword(tavoloService.lastGame(inSessione.getId()), false);
	}

	/**
	 * Abbandona la partita nel tavolo specificato.
	 * 
	 * @PostMethod AbbandonaPartita Ti permette di Abbandonare la partita nel tavolo
	 *             Specificato.
	 * 
	 * @param Long id Del tavolo che vuoi abbandonare.
	 * 
	 * @return UtenteDTO l' utente in sessione, con l' esperienza Aumentata.
	 * 
	 * @throws NonFaiParteDiQuestoTavoloException se non fai parte dell' tavolo
	 *                                            specificato tramite id.
	 */
	@PostMapping("/abbandonaPartita/{idTavolo}")
	@ResponseStatus(HttpStatus.OK)
	public UtenteDTO abbandonaPartita(@PathVariable(value = "idTavolo", required = true) Long idTavolo) {
		return UtenteDTO.buildUtenteDTOFromModelNoPassword(tavoloService.abbandonaPartita(idTavolo));
	}

	/**
	 * Ricerca Partita Solo i tavoli esperienza minima <= esperienza accumulata
	 *
	 * @return La lista dei tavoli cui l' utente può accedere.
	 */
	@GetMapping("/ricerca")
	public List<TavoloDTO> ricercaPartita(@RequestBody TavoloDTO example) {
		return TavoloDTO.createTavoloDTOListFromModelList(tavoloService.findByExampleGame(example.buildTavoloModel()),
				false);
	}

	/**
	 * Questo metodo simula una mano del gioco. Se non fai parte del tavolo dato in
	 * input il metodo comunque ti aggiunge alla partita, e ti fa giocare.
	 * 
	 * @param Long l' id del tavolo in cui giocare .
	 * 
	 * @return UtenteDTO l' utente con i dati aggiornati dopo la simulazione della
	 *         partita.
	 */
	@GetMapping("/gioca/{idTavolo}")
	public UtenteDTO giocaPartita(@PathVariable(value = "idTavolo", required = true) Long idTavolo) {

		// Eseguo il metodo che controlla se faccio parte del tavolo mandato in input,
		// se si bene senno mi inserisce
		Tavolo tavoloInstance = tavoloService.caricaSingoloElemento(idTavolo);
		Utente inSessione = utenteService
				.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

		// Controllo che l' esperienza accomulata dall' utente sia sufficente
		if (tavoloInstance.getEsperienzaMinima() > inSessione.getEsperienzaAccumulata())
			throw new EsperienzaAccomulataNonSufficente(
					"Impossibile partecipare a questa partita, la tua esperienza è troppo bassa!");

		// Controllo il credito
		if (tavoloInstance.getCifraMinima() > inSessione.getCreditoAccumulato())
			throw new CreditoInsufficenteException(
					"Impossibile giocarte questa partita, il tuo credito è insufficente!");

		if (!tavoloInstance.getGiocatori().stream().anyMatch(giocatore -> giocatore.getId() == inSessione.getId())) {
			// Chiamo il metodo che mi aggiunge e gioca la partita
			return UtenteDTO.buildUtenteDTOFromModelNoPassword(
					utenteService.partecipaEGiocaPartita(inSessione, tavoloInstance));
		}

		// se faccio parte del tavolo gioco direttamente
		return UtenteDTO.buildUtenteDTOFromModelNoPassword(utenteService.giocaPartita(inSessione));
	}

}
