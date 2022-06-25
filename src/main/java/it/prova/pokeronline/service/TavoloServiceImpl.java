package it.prova.pokeronline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.repository.TavoloRepository;

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
	public Tavolo caricaSingoloElemento(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tavolo caricaSingoloElementoEager(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tavolo aggiorna(Tavolo tavoloInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tavolo inserisciNuovo(Tavolo tavoloInstance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rimuovi(Tavolo tavoloInstance) {
		// TODO Auto-generated method stub

	}

}
