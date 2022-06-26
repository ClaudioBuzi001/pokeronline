package it.prova.pokeronline.repository;

import java.util.List;

import it.prova.pokeronline.model.Tavolo;

public interface CustomTavoloRepository {
	
	List<Tavolo> findByExample(Tavolo example);
	
	List<Tavolo> findByExampleSpecialPlayer(Tavolo example, Long id);
	
	List<Tavolo> findByExampleGame(Tavolo example, Integer esperienzaAccomulata);

}
