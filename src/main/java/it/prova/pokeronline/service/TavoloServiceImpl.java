package it.prova.pokeronline.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.TavoloRepository;
import it.prova.pokeronline.repository.UtenteRepository;
import it.prova.pokeronline.web.api.exception.GiocatoriPresentiException;
import it.prova.pokeronline.web.api.exception.NonFaiParteDiQuestoTavolo;
import it.prova.pokeronline.web.api.exception.NonSeiPresenteInNessunTavolo;
import it.prova.pokeronline.web.api.exception.TavoloNotFoundException;

@Service
public class TavoloServiceImpl implements TavoloService {

	@Autowired
	private TavoloRepository repository;

	@Autowired
	private UtenteRepository utenteRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Tavolo> listAllElements(boolean eager) {
		if (eager)
			return repository.findAllEager();
		return (List<Tavolo>) repository.findAll();

	}

	@Override
	@Transactional(readOnly = true)
	public Tavolo caricaSingoloElemento(Long id) {
		if (SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
				.anyMatch(roleItem -> roleItem.getAuthority().equals(Ruolo.ROLE_SPECIAL_PLAYER))) {
			return repository.findByIdSpecialPlayer(id, utenteRepository
					.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get().getId())
					.orElse(null);
		}

		return repository.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Tavolo caricaSingoloElementoEager(Long id) {
		return repository.findByIdEager(id);
	}

	@Override
	@Transactional
	public Tavolo aggiorna(Tavolo tavoloInstance, Tavolo tavoloCaricatoDalDB) {
		if (tavoloCaricatoDalDB.getGiocatori().size() > 0)
			throw new GiocatoriPresentiException(
					"Impossibile eliminare questo Tavolo, sono ancora presenti dei giocatori al suo interno");

		return repository.save(tavoloInstance);
	}

	@Override
	@Transactional
	public Tavolo inserisciNuovo(Tavolo tavoloInstance) {
		// Se la data Creazione è null, setto da sistema la data di ora
		if (tavoloInstance.getDataCreazione() == null)
			tavoloInstance.setDataCreazione(new Date());

		return repository.save(tavoloInstance);
	}

	@Override
	@Transactional
	public void rimuovi(Long id) {
		// Mi carico il tavolo e verifico che i giocatori siano 0
		Tavolo tavolo = repository.findById(id)
				.orElseThrow(() -> new TavoloNotFoundException("Tavolo con id " + id + " not found"));

		if (tavolo.getGiocatori().size() > 0)
			throw new GiocatoriPresentiException(
					"Impossibile eliminare questo Tavolo, sono ancora presenti dei giocatori al suo interno");

		repository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Tavolo cercaPerDenominazione(String denominazione) {
		return repository.findByDenominazione(denominazione);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Tavolo> findByExample(Tavolo example) {
		return repository.findByExample(example);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Tavolo> findByExampleSpecialPlayer(Tavolo example, Long id) {
		return repository.findByExampleSpecialPlayer(example, id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Tavolo> findAllSpecialPlayer(String name) {
		return repository.findAllSpecialPlayer(utenteRepository.findByUsername(name).get().getId());
	}

	@Override
	@Transactional
	public Tavolo lastGame(Long id) {
		// Se sono presente all interno di un tavolo returno il tavolo.
		Tavolo result = repository.findByGiocatori_id(id).orElse(null);

		// Se non sono presente in nessun tavolo.
		if (result == null)
			throw new NonSeiPresenteInNessunTavolo("Attenzione, non sei stai giocando in nessun tavolo");

		return result;
	}

	@Override
	@Transactional
	public Utente abbandonaPartita(Long idTavolo) {
		// Verifico che il tavolo esista.
		Tavolo tavoloInstance = repository.findByIdEager(idTavolo);
		if (tavoloInstance == null)
			throw new TavoloNotFoundException("Tavolo con id: " + idTavolo + " not Found");

		// Prendo l' utente In Sessione.
		Utente inSessione = utenteRepository
				.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();

		int contatore = 0; // Contatore che mi serve per verificare se l' utente in sessione fa parte del
							// tavolo
		// Elimino l' utente dai giocatori.
		for (Utente utenteItem : tavoloInstance.getGiocatori()) {
			if (utenteItem.getId() == inSessione.getId()) {
				tavoloInstance.getGiocatori().remove(utenteItem);
				contatore++;
			}
		}
		// Se Il contatore == 0 allora l' utente In sessione non faceva parte del tavolo
		// dato. Quindi per evitare Bug di Aumento Esperienza lancio una eccezione
		if (contatore == 0)
			throw new NonFaiParteDiQuestoTavolo("Attenzione, non fai parte di questo tavolo");

		inSessione.setEsperienzaAccumulata(inSessione.getEsperienzaAccumulata() + 1);
		repository.save(tavoloInstance);
		utenteRepository.save(inSessione);

		return inSessione;
	}

	@Override
	@Transactional(readOnly = true)
	public List<Tavolo> findByExampleGame(Tavolo example) {
		Utente inSessione = utenteRepository
				.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).get();
		
		return repository.findByExampleGame(example, inSessione.getEsperienzaAccumulata());
	}

}


























