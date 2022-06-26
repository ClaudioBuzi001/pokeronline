package it.prova.pokeronline.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.service.UtenteService;
import it.prova.pokeronline.web.api.exception.CreditoMinimoException;

@RestController
@RequestMapping("api/play")
public class PlayManagementController {

	@Autowired
	private UtenteService utenteService;
	
	/**
	 *@PostMethod CompraCredito ti permette di aumentare il credito .
	 *
	 *@param      Integer       Di quando il tuo credito deve essere aumentato.
	 * 
	 *@return 	  Integer 		Il tuo credito attuale.
	 */
	@PostMapping("/compracredito/{credito}")
	@ResponseStatus(HttpStatus.CREATED)
	public Integer compraCredito(@PathVariable(value = "credito", required = true) Integer credito) {
		//Controllo se il credito sia almeno 1
		if(credito < 1)
			throw new CreditoMinimoException("Impossibile comprare credito minore di 1$, Bisogna Caricare almeno un 1$");
		
		//Chiamo il metodo che mi aumenta il credito
		return utenteService.compraCredito(credito);
	}
	
	
	
}



















