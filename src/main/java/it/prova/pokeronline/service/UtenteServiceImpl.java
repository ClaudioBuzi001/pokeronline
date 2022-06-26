package it.prova.pokeronline.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.StatoUtente;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.repository.UtenteRepository;

@Service
public class UtenteServiceImpl implements UtenteService {

	@Autowired
	private UtenteRepository repository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional(readOnly = true)
	public List<Utente> listAllUtenti() {
		return (List<Utente>) repository.findAll();
	}

	@Transactional(readOnly = true)
	public Utente caricaSingoloUtente(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Transactional(readOnly = true)
	public Utente caricaSingoloUtenteConRuoli(Long id) {
		return repository.findByIdConRuoli(id).orElse(null);
	}

	@Transactional
	public Utente aggiorna(Utente utenteInstance) {
		// deve aggiornare solo nome, cognome, username, ruoli
		Utente utenteReloaded = repository.findById(utenteInstance.getId()).orElse(null);
		if (utenteReloaded == null)
			throw new RuntimeException("Elemento non trovato");
		utenteReloaded.setNome(utenteInstance.getNome());
		utenteReloaded.setCognome(utenteInstance.getCognome());
		utenteReloaded.setUsername(utenteInstance.getUsername());
		utenteReloaded.setRuoli(utenteInstance.getRuoli());
		return repository.save(utenteReloaded);
	}

	@Transactional
	public void inserisciNuovo(Utente utenteInstance) {
		utenteInstance.setStato(StatoUtente.CREATO);
		utenteInstance.setPassword(passwordEncoder.encode(utenteInstance.getPassword()));
		utenteInstance.setDateRegistrazione(new Date());
		repository.save(utenteInstance);
	}

	@Transactional
	public void rimuovi(Utente utenteInstance) {
		repository.delete(utenteInstance);
	}

	@Transactional(readOnly = true)
	public List<Utente> findByExample(Utente example) {
		// TODO DA IMPLEMENTARE
		return listAllUtenti();
	}

	@Transactional(readOnly = true)
	public Utente eseguiAccesso(String username, String password) {
		return repository.findByUsernameAndPasswordAndStato(username, password, StatoUtente.ATTIVO);
	}

	@Override
	public Utente findByUsernameAndPassword(String username, String password) {
		return repository.findByUsernameAndPassword(username, password);
	}

	@Transactional
	public void changeUserAbilitation(Long utenteInstanceId) {
		Utente utenteInstance = caricaSingoloUtente(utenteInstanceId);
		if (utenteInstance == null)
			throw new RuntimeException("Elemento non trovato.");

		if (utenteInstance.getStato() == null || utenteInstance.getStato().equals(StatoUtente.CREATO))
			utenteInstance.setStato(StatoUtente.ATTIVO);
		else if (utenteInstance.getStato().equals(StatoUtente.ATTIVO))
			utenteInstance.setStato(StatoUtente.DISABILITATO);
		else if (utenteInstance.getStato().equals(StatoUtente.DISABILITATO))
			utenteInstance.setStato(StatoUtente.ATTIVO);
	}

	@Transactional
	public Utente findByUsername(String username) {
		return repository.findByUsername(username).orElse(null);
	}

	@Override
	@Transactional
	public Integer compraCredito(Integer credito) {
		// Mi carico l utente in sessione, per passarlo come id all metodo del
		// repository
		Utente inSessione = repository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName())
				.get();

		if (inSessione.getCreditoAccumulato() == null)
			inSessione.setCreditoAccumulato(credito);
		else
			inSessione.setCreditoAccumulato(inSessione.getCreditoAccumulato() + credito);

		repository.save(inSessione);
		return inSessione.getCreditoAccumulato();
	}

	/**
	 * Metodo che permette all' utente di inserirsi in una partita, specificando il
	 * tavolo, e di giocare una mano.
	 * 
	 * @param Utente utente che sta giocando la partita.
	 * @param Tavolo Il tavolo su cui l' utente sta giocando.
	 * 
	 * @return Utente L'untente con i dati aggiornati, dopo la partita.
	 */
	@Override
	@Transactional
	public Utente partecipaEGiocaPartita(Utente inSessione, Tavolo tavoloInstance) {
		// Mi aggiunge all set di giocatori.
		tavoloInstance.getGiocatori().add(inSessione);

		// gioco la partita.
		inSessione.setCreditoAccumulato(inSessione.getCreditoAccumulato() + UtenteServiceImpl.simulaPartita());
		if (inSessione.getCreditoAccumulato() < 0)
			inSessione.setCreditoAccumulato(0);

		repository.save(inSessione);
		return inSessione;
	}

	@Override
	@Transactional
	public Utente giocaPartita(Utente inSessione) {
		inSessione.setCreditoAccumulato(inSessione.getCreditoAccumulato() + UtenteServiceImpl.simulaPartita());
		if (inSessione.getCreditoAccumulato() < 0)
			inSessione.setCreditoAccumulato(0);

		repository.save(inSessione);
		return inSessione;
	}

	private static Integer simulaPartita() {

		Double segno = Math.random();
		Double somma = 0.0;
		if (segno >= 0.5)
			somma = Math.random() * 1000;
		else
			somma = Math.random() * -1000;

		return (int) (segno * somma.intValue());
	}
}
