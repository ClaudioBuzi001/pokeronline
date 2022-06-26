package it.prova.pokeronline.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.repository.RuoloRepository;

@Service
public class RuoloServiceImpl implements RuoloService {

	@Autowired
	private RuoloRepository ruoloRepository;

	@Transactional(readOnly = true)
	public List<Ruolo> listAll() {
		return (List<Ruolo>) ruoloRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Ruolo caricaSingoloElemento(Long id) {
		return ruoloRepository.findById(id).orElse(null);
	}

	@Transactional
	public void aggiorna(Ruolo ruoloInstance) {
		// TODO
	}

	@Transactional
	public void inserisciNuovo(Ruolo ruoloInstance) {
		ruoloRepository.save(ruoloInstance);
	}

	@Transactional
	public void rimuovi(Ruolo ruoloInstance) {
		// TODO
	}

	@Transactional(readOnly = true)
	public Ruolo cercaPerDescrizioneECodice(String descrizione, String codice) {
		return ruoloRepository.findByDescrizioneAndCodice(descrizione, codice);
	}
}
