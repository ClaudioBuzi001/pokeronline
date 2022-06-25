package it.prova.pokeronline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.repository.TavoloRepository;
import it.prova.pokeronline.web.api.exception.GiocatoriPresentiException;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;

@Service
public class TavoloServiceImpl implements TavoloService {

	@Autowired
	private TavoloRepository repository;
	
	@Override
	@Transactional(readOnly = true)
	public List<Tavolo> listAllElements(boolean eager) {
		if(eager) 
			return repository.findAllEager();
		return (List<Tavolo>) repository.findAll();
		
	}

	@Override
	@Transactional(readOnly = true)
	public Tavolo caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Tavolo caricaSingoloElementoEager(Long id) {
		return repository.findByIdEager(id) ;
	}

	@Override
	@Transactional
	public Tavolo aggiorna(Tavolo tavoloInstance, Tavolo tavoloCaricatoDalDB) {
		if(tavoloCaricatoDalDB.getGiocatori().size() > 0)
			throw new GiocatoriPresentiException("Impossibile eliminare questo Tavolo, sono ancora presenti dei giocatori al suo interno");
		
		return repository.save(tavoloInstance);
	}

	@Override
	@Transactional
	public Tavolo inserisciNuovo(Tavolo tavoloInstance) {
		return repository.save(tavoloInstance);
	}

	@Override
	@Transactional
	public void rimuovi(Long id) {
		//Mi carico il tavolo e verifico che i giocatori siano 0
		Tavolo tavolo = repository.findById(id).orElseThrow(() -> new TavoloNotFoundException("Tavolo con id "+ id + " not found"));

		if(tavolo.getGiocatori().size() > 0)
			throw new GiocatoriPresentiException("Impossibile eliminare questo Tavolo, sono ancora presenti dei giocatori al suo interno");
		
		repository.deleteById(id);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
