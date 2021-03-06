package it.prova.pokeronline.dto;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import it.prova.pokeronline.model.Tavolo;
import it.prova.pokeronline.model.Utente;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TavoloDTO {
	
	private Long id;
	@NotNull(message = "{esperienzaMinima.notnull}")
	@Min(0)
	private Integer esperienzaMinima;
	@NotNull(message = "{cifraMinima.notnull}")
	@Min(1)
	private Integer cifraMinima;
	@NotBlank(message= "{denominazione.notblank}")
	private String denominazione;
	private Date dataCreazione;
	private Set<UtenteDTO> giocatori = new HashSet<UtenteDTO>(0);
	private UtenteDTO utenteCreazione;
	
	public TavoloDTO(Long id, @Min(0) Integer esperienzaMinima, @Min(1) Integer cifraMinima,
			@NotBlank String denominazione, @NotNull Date dataCreazione, Set<UtenteDTO> giocatori,
			@NotNull UtenteDTO utenteCreazione) {
		super();
		this.id = id;
		this.esperienzaMinima = esperienzaMinima;
		this.cifraMinima = cifraMinima;
		this.denominazione = denominazione;
		this.dataCreazione = dataCreazione;
		this.giocatori = giocatori;
		this.utenteCreazione = utenteCreazione;
	}
	
	public TavoloDTO() {
		super();
	}
	public TavoloDTO(Long id, @Min(0) Integer esperienzaMinima, @Min(1) Integer cifraMinima, @NotBlank String denominazione,
		@NotNull Date dataCreazione) {
	super();
	this.id = id;
	this.esperienzaMinima = esperienzaMinima;
	this.cifraMinima = cifraMinima;
	this.denominazione = denominazione;
	this.dataCreazione = dataCreazione;
}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getEsperienzaMinima() {
		return esperienzaMinima;
	}
	public void setEsperienzaMinima(Integer esperienzaMinima) {
		this.esperienzaMinima = esperienzaMinima;
	}
	public Integer getCifraMinima() {
		return cifraMinima;
	}
	public void setCifraMinima(Integer cifraMinima) {
		this.cifraMinima = cifraMinima;
	}
	public String getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}
	public Date getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(Date dataCreazione) {
		this.dataCreazione = dataCreazione;
	}
	public Set<UtenteDTO> getGiocatori() {
		return giocatori;
	}
	public void setGiocatori(Set<UtenteDTO> giocatori) {
		this.giocatori = giocatori;
	}
	public UtenteDTO getUtenteCreazione() {
		return utenteCreazione;
	}
	public void setUtenteCreazione(UtenteDTO utenteCreazione) {
		this.utenteCreazione = utenteCreazione;
	}

	
	public Tavolo buildTavoloModel() {
		Tavolo result = new Tavolo(this.id, this.esperienzaMinima, this.cifraMinima, this.denominazione, this.dataCreazione);
		
		if(this.giocatori.size() > 1) {
			Set<Utente> set = result.getGiocatori();
			this.giocatori.forEach(utente -> set.add(utente.buildUtenteModel(false)));
		}
		
		if(this.utenteCreazione != null)
			result.setUtenteCreazione(this.utenteCreazione.buildUtenteModel(false));
			
		return result;
	}

	public static TavoloDTO buildTavoloDTOFromModel(Tavolo tavoloModel, boolean includeGiocatori) {

		TavoloDTO result = new TavoloDTO(tavoloModel.getId(), tavoloModel.getEsperienzaMinima(),
				tavoloModel.getCifraMinima(), tavoloModel.getDenominazione(), tavoloModel.getDataCreazione());

		if (tavoloModel.getUtenteCreazione() != null && tavoloModel.getUtenteCreazione().getId() != null
				&& tavoloModel.getUtenteCreazione().getId() > 0) {
			result.setUtenteCreazione(UtenteDTO.buildUtenteDTOFromModel(tavoloModel.getUtenteCreazione()));
		}

		if (tavoloModel.getGiocatori() != null && !tavoloModel.getGiocatori().isEmpty()) {
			result.setGiocatori(UtenteDTO.buildUtenteDTOSetFromModelSet(tavoloModel.getGiocatori()));
		}

		return result;
	}
	
	public static TavoloDTO buildTavoloDTOFromModelNoPassword(Tavolo tavoloModel, boolean includeGiocatori) {

		TavoloDTO result = new TavoloDTO(tavoloModel.getId(), tavoloModel.getEsperienzaMinima(),
				tavoloModel.getCifraMinima(), tavoloModel.getDenominazione(), tavoloModel.getDataCreazione());

		if (tavoloModel.getUtenteCreazione() != null && tavoloModel.getUtenteCreazione().getId() != null
				&& tavoloModel.getUtenteCreazione().getId() > 0) {
			result.setUtenteCreazione(UtenteDTO.buildUtenteDTOFromModelNoPassword(tavoloModel.getUtenteCreazione()));
		}

		if (tavoloModel.getGiocatori() != null && !tavoloModel.getGiocatori().isEmpty()) {
			result.setGiocatori(UtenteDTO.buildUtenteDTOSetFromModelSet(tavoloModel.getGiocatori()));
		}

		return result;
	}

	public static List<TavoloDTO> createTavoloDTOListFromModelList(List<Tavolo> modelListInput,
			boolean includeGiocatori) {

		return modelListInput.stream().map(tavoloEntity -> {
			TavoloDTO result = TavoloDTO.buildTavoloDTOFromModelNoPassword(tavoloEntity, includeGiocatori);

			if (includeGiocatori)
				result.setGiocatori(UtenteDTO.buildUtenteDTOSetFromModelSet(tavoloEntity.getGiocatori()));
			return result;
		}).collect(Collectors.toList());
	}


}








































