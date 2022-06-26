package it.prova.pokeronline.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import it.prova.pokeronline.model.Tavolo;

public class CustomTavoloRepositoryImpl implements CustomTavoloRepository {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * FindByExample Per l' Admin, Si può ricercare anche per l' UtenteCreazione,
	 * Passando un id Corretto.
	 * 
	 * @param Tavolo        example Tavolo da ricercare
	 * @return List<Tavolo> La lista Di tutti i tavoli corrispondenti all example,
	 *         				DI tutti Gli utenti.
	 */
	@Override
	public List<Tavolo> findByExample(Tavolo example) {
		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereClauses = new ArrayList<String>();

		StringBuilder queryBuilder = new StringBuilder("select t from Tavolo t where t.id = t.id ");

		if (StringUtils.isNotEmpty(example.getDenominazione())) {
			whereClauses.add(" t.denominazione  like :denominazione ");
			paramaterMap.put("denominazione", "%" + example.getDenominazione() + "%");
		}
		if (example.getCifraMinima() != null && example.getCifraMinima() > 0) {
			whereClauses.add(" t.cifraMinima > :cifraMinima ");
			paramaterMap.put("cifraMinima", example.getCifraMinima());
		}
		if (example.getEsperienzaMinima() != null && example.getEsperienzaMinima() > 0) {
			whereClauses.add(" t.esperienzaMinima > :esperienzaMinima ");
			paramaterMap.put("esperienzaMinima", example.getEsperienzaMinima());
		}
		if (example.getDataCreazione() != null) {
			whereClauses.add("t.dataCreazione>= :dataCreazione ");
			paramaterMap.put("dataCreazione", example.getDataCreazione());
		}
		if (example.getUtenteCreazione() != null && example.getUtenteCreazione().getId() != null
				&& example.getUtenteCreazione().getId() > 0) {
			whereClauses.add(" t.utenteCreazione.id = :utenteCreazioneId ");
			paramaterMap.put("utenteCreazioneId", example.getUtenteCreazione().getId());
		}

		queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<Tavolo> typedQuery = entityManager.createQuery(queryBuilder.toString(), Tavolo.class);

		for (String key : paramaterMap.keySet()) {
			typedQuery.setParameter(key, paramaterMap.get(key));
		}

		return typedQuery.getResultList();

	}

	/**
	 * Questo è il FindByExample per lo SpecialPlayer, Trova i tavoli corrispondenti
	 * all example, è solo quelli appartenenti all utente Loggato in sessione.
	 * 
	 * @param Tavolo tavolo da ricercare
	 * @param Long   id da ricercare
	 * 
	 * @return List<Tavolo> I tavoli corrispondenti all' example e appartententi all
	 *         utente in sessione.
	 */
	@Override
	public List<Tavolo> findByExampleSpecialPlayer(Tavolo example, Long id) {
		Map<String, Object> paramaterMap = new HashMap<String, Object>();
		List<String> whereClauses = new ArrayList<String>();

		StringBuilder queryBuilder = new StringBuilder("select t from Tavolo t where t.id = t.id ");

		if (StringUtils.isNotEmpty(example.getDenominazione())) {
			whereClauses.add(" t.denominazione  like :denominazione ");
			paramaterMap.put("denominazione", "%" + example.getDenominazione() + "%");
		}
		if (example.getCifraMinima() != null && example.getCifraMinima() > 0) {
			whereClauses.add(" t.cifraMinima > :cifraMinima ");
			paramaterMap.put("cifraMinima", example.getCifraMinima());
		}
		if (example.getEsperienzaMinima() != null && example.getEsperienzaMinima() > 0) {
			whereClauses.add(" t.esperienzaMinima > :esperienzaMinima ");
			paramaterMap.put("esperienzaMinima", example.getEsperienzaMinima());
		}
		if (example.getDataCreazione() != null) {
			whereClauses.add("t.dataCreazione>= :dataCreazione ");
			paramaterMap.put("dataCreazione", example.getDataCreazione());
		}

		// Aggiungiamo la clausola del id dell' utente In sessione
		whereClauses.add(" t.utenteCreazione.id = :utenteCreazioneId ");
		paramaterMap.put("utenteCreazioneId", id);

		queryBuilder.append(!whereClauses.isEmpty() ? " and " : "");
		queryBuilder.append(StringUtils.join(whereClauses, " and "));
		TypedQuery<Tavolo> typedQuery = entityManager.createQuery(queryBuilder.toString(), Tavolo.class);

		for (String key : paramaterMap.keySet()) {
			typedQuery.setParameter(key, paramaterMap.get(key));
		}

		return typedQuery.getResultList();

	}

}
