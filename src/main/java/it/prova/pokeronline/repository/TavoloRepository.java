package it.prova.pokeronline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronline.model.Tavolo;


public interface TavoloRepository extends CrudRepository<Tavolo, Long>, CustomTavoloRepository{
	
	@Query("select t from Tavolo t join fetch t.utenteCreazione join fetch t.giocatori")
	List<Tavolo> findAllEager();	

	@Query("select t from Tavolo t join fetch t.utenteCreazione join fetch t.giocatori where t.id = ?1")
	Tavolo findByIdEager(Long id);
	
	Tavolo findByDenominazione(String denominazione);
}
