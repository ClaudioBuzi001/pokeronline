package it.prova.pokeronline.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.prova.pokeronline.model.Tavolo;


public interface TavoloRepository extends CrudRepository<Tavolo, Long>{
	
	@Query("select t from Tavolo t join fetch t.utenteCreazione join fetch t.giocatori")
	List<Tavolo> findAllEager();	

}
