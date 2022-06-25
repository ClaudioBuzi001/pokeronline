package it.prova.pokeronline.web.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.dto.UtenteDTO;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.IdNotNullForInsertException;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;
import it.prova.pokeronline.web.api.exception.UtenteCreazioneNonCorrispondenteAlPrecedente;
import it.prova.pokeronline.web.api.exception.UtenteCreazioneNonValidoException;
import it.prova.pokeronline.web.api.exception.UtenteCreazioneNotNullException;


@RestController
@RequestMapping("api/tavolo")
public class TavoloController {
	
	@Autowired
	private TavoloService tavoloService;
	
	@Autowired
	private UtenteService utenteService;
	
	@GetMapping
	public List<TavoloDTO> getAll() {
		return TavoloDTO.createTavoloDTOListFromModelList(tavoloService.listAllElements(true), true);
	}
	@GetMapping("/{id}")
	public TavoloDTO findById(@PathVariable(value = "id", required = true) long id) {
		Tavolo tavolo = tavoloService.caricaSingoloElementoEager(id);

		if (tavolo== null)
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);

		return TavoloDTO.buildTavoloDTOFromModel(tavolo, true);
	}

	// gli errori di validazione vengono mostrati con 400 Bad Request ma
	// elencandoli grazie al ControllerAdvice
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TavoloDTO createNew(@Valid @RequestBody TavoloDTO tavoloInput) {
		if(tavoloInput.getId() != null)
			throw new IdNotNullForInsertException("Non è ammesso fornire un id per la creazione");
		/**
		Controllo che l utenteCreazione sia != null se cosi lancio eccezione, perche non si può decidere per chi creare il tavolo,
		L utente Creazione viene settato da sistema con l utente in sessione.
		*/
		if(tavoloInput.getUtenteCreazione() != null)
			throw new UtenteCreazioneNotNullException("Non è ammesso fornire l' UtenteCreazione, impossibile Creare un tavolo per un Utente Specifico");
		
		//Carico l UtenteCreazione dentro il tavolo con l' utente in sessione
		tavoloInput.setUtenteCreazione(UtenteDTO.buildUtenteDTOFromModel(utenteService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())));
		Tavolo tavoloInserito = tavoloService.inserisciNuovo(tavoloInput.buildTavoloModel());
		
		return TavoloDTO.buildTavoloDTOFromModel(tavoloInserito, false);
	}


	@PutMapping("/{id}")
	public TavoloDTO update(@Valid @RequestBody TavoloDTO tavoloInput, @PathVariable(required = true) Long id) {
		Tavolo tavolo = tavoloService.caricaSingoloElementoEager(id);

		if (tavolo == null)
			throw new TavoloNotFoundException("Tavolo not found con id: " + id);

		/**
		 * 1)NoN dobbiamo controllare che l'utenteCreazione sia lo stesso di quello che sta facendo l update, perche L update può essere fatto anche
		 * da un admin, quindi l admin può modificare i tavoli di tutti. 
		 * 
		 * 2)Questa Volta però L' utenteCreazione non viene settato da sistema, quindi dobbiamo validarlo.
		 * 
		 * 3)NoN si può cambiare l' utenteCreazione quindi dobbiamo verificare che l' utenteCreazione Nuovo sia lo stesso di quello di prima.
		 */
		if(tavoloInput.getUtenteCreazione() == null || tavoloInput.getUtenteCreazione().getId() == null || tavoloInput.getUtenteCreazione().getId() < 1)
			throw new UtenteCreazioneNonValidoException("UtenteCreazione non valid, inserire correttamente il campo UtenteCreazione");

		//Per verificare il punto 3, confrontiamo l' id del utenteCreazione del tavolo caricato dal DB e l' id del utenteCreazione del tavoloInput
		if(tavolo.getUtenteCreazione().getId() != tavoloInput.getUtenteCreazione().getId())
			throw new UtenteCreazioneNonCorrispondenteAlPrecedente("Impossibile modificare L' UtenteCreazione inserendo un Utente Diverso, L' UtenteCreazione deve rimanere LO STESSO");
		
		tavoloInput.setId(id);
		Tavolo tavoloAggiornato = tavoloService.aggiorna(tavoloInput.buildTavoloModel());
		return TavoloDTO.buildTavoloDTOFromModel(tavoloAggiornato, false);
	}

}


















