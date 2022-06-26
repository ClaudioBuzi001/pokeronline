package it.prova.pokeronline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;
import it.prova.pokeronline.service.RuoloService;
import it.prova.pokeronline.service.TavoloService;
import it.prova.pokeronline.service.UtenteService;

@SpringBootApplication
public class PokeronlineApplication implements CommandLineRunner {

	@Autowired
	private RuoloService ruoloService;
	
	@Autowired
	private UtenteService utenteService;
	
	@Autowired
	private TavoloService tavoloService;
	
	public static void main(String[] args) {
		SpringApplication.run(PokeronlineApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (ruoloService.cercaPerDescrizioneECodice("Administrator", Ruolo.ROLE_ADMIN) == null) {
			ruoloService.inserisciNuovo(new Ruolo("Administrator", Ruolo.ROLE_ADMIN));
		}
		if (ruoloService.cercaPerDescrizioneECodice("Player User", Ruolo.ROLE_PLAYER) == null) {
			ruoloService.inserisciNuovo(new Ruolo("Player User", Ruolo.ROLE_PLAYER));
		}
		if (ruoloService.cercaPerDescrizioneECodice("Special Player User", Ruolo.ROLE_SPECIAL_PLAYER) == null) {
			ruoloService.inserisciNuovo(new Ruolo("Special Player User", Ruolo.ROLE_SPECIAL_PLAYER));
		}

		Utente admin = utenteService.findByUsername("admin");
		if (admin == null) {
			admin = new Utente("admin", "admin", "Mario", "Rossi");
			admin.getRuoli().add(ruoloService.cercaPerDescrizioneECodice("Administrator", Ruolo.ROLE_ADMIN));

			utenteService.inserisciNuovo(admin);
			utenteService.changeUserAbilitation(admin.getId());
		}
		if (utenteService.findByUsername("player") == null) {
			Utente playerUser = new Utente("player", "player", "Antonio", "Verdi");
			playerUser.getRuoli()
					.add(ruoloService.cercaPerDescrizioneECodice("Player User", Ruolo.ROLE_PLAYER));

			utenteService.inserisciNuovo(playerUser);
			utenteService.changeUserAbilitation(playerUser.getId());
		}
		Utente specialUser = utenteService.findByUsername("special");
		if (specialUser == null) {
			specialUser = new Utente("special", "special", "Giovanni", "Bianchi");
			specialUser.getRuoli().add(
					ruoloService.cercaPerDescrizioneECodice("Special Player User", Ruolo.ROLE_SPECIAL_PLAYER));

			utenteService.inserisciNuovo(specialUser);
			utenteService.changeUserAbilitation(specialUser.getId());
		}

		if (tavoloService.cercaPerDenominazione("Tavolo1") == null) {
			Tavolo tavoloAdmin = new Tavolo("Tavolo1", 0, 0);
			tavoloAdmin.setUtenteCreazione(admin);
			tavoloService.inserisciNuovo(tavoloAdmin);
		}
		if (tavoloService.cercaPerDenominazione("Tavolo2") == null) {
			Tavolo tavoloSpecial = new Tavolo("Tavolo2", 0, 0);
			tavoloSpecial.setUtenteCreazione(specialUser);
			tavoloService.inserisciNuovo(tavoloSpecial);
		}
		
	}

}
