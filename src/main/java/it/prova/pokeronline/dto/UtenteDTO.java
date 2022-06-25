package it.prova.pokeronline.dto;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import it.prova.pokeronline.model.Ruolo;
import it.prova.pokeronline.model.StatoUtente;
import it.prova.pokeronline.model.Utente;

public class UtenteDTO {
	private Long id;
	@NotBlank(message = "{username.notblank}")
	@Size(min = 3, max = 15, message = "Il valore inserito '${validatedValue}' deve essere lungo tra {min} e {max} caratteri")
	private String username;
	@NotBlank(message = "{password.notblank}")
	@Size(min = 3, max = 15, message = "Il valore inserito deve essere lungo tra {min} e {max} caratteri")
	private String password;
	@NotBlank(message = "{nome.notblank}")
	private String nome;
	@NotBlank(message = "{cognome.notblank}")
	private String cognome;

	private Date dataRegistrazione;
	@Min(0)
	private Integer esperienzaAccumulata;
	@Min(0)
	private Integer creditoAccumulato;

	private StatoUtente stato;

	private Long[] ruoliIds;

	public UtenteDTO() {
	}

//	utenteModel.getId(), utenteModel.getUsername(), utenteModel.getPassword(),
//	utenteModel.getNome(), utenteModel.getCognome(), utenteModel.getDateRegistrazione(),
//	utenteModel.getStato(), utenteModel.getEsperienzaAccumulata(), utenteModel.getCreditoAccumulato()

	public UtenteDTO(Long id, String username, String nome, String cognome, StatoUtente stato) {
		super();
		this.id = id;
		this.username = username;
		this.nome = nome;
		this.cognome = cognome;
		this.stato = stato;
	}

	public UtenteDTO(Long id, String username, String password, String nome, String cognome, Date dataRegistrazione,
			StatoUtente stato, Integer esperienzaAccumulata, Integer creditoAccumulato ) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.dataRegistrazione = dataRegistrazione;
		this.esperienzaAccumulata = esperienzaAccumulata;
		this.creditoAccumulato = creditoAccumulato;
		this.stato = stato;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public StatoUtente getStato() {
		return stato;
	}

	public Integer getEsperienzaAccumulata() {
		return esperienzaAccumulata;
	}

	public void setEsperienzaAccumulata(Integer esperienzaAccumulata) {
		this.esperienzaAccumulata = esperienzaAccumulata;
	}

	public Integer getCreditoAccumulato() {
		return creditoAccumulato;
	}

	public void setCreditoAccumulato(Integer creditoAccumulato) {
		this.creditoAccumulato = creditoAccumulato;
	}

	public void setStato(StatoUtente stato) {
		this.stato = stato;
	}

	public Date getDataRegistrazione() {
		return dataRegistrazione;
	}

	public void setDataRegistrazione(Date dataregistrazione) {
		this.dataRegistrazione = dataregistrazione;
	}

	public Long[] getRuoliIds() {
		return ruoliIds;
	}

	public void setRuoliIds(Long[] ruoliIds) {
		this.ruoliIds = ruoliIds;
	}

	public Utente buildUtenteModel(boolean includeIdRoles) {
		Utente result = new Utente(this.id, this.username, this.password, this.nome, this.cognome,
				this.dataRegistrazione, this.creditoAccumulato, this.esperienzaAccumulata, this.stato);
		if (includeIdRoles && ruoliIds != null)
			result.setRuoli(Arrays.asList(ruoliIds).stream().map(id -> new Ruolo(id)).collect(Collectors.toSet()));

		return result;
	}

	public static UtenteDTO buildUtenteDTOFromModel(Utente utenteModel) {
		UtenteDTO result = new UtenteDTO(utenteModel.getId(), utenteModel.getUsername(), utenteModel.getPassword(),
				utenteModel.getNome(), utenteModel.getCognome(), utenteModel.getDateRegistrazione(),
				utenteModel.getStato(), utenteModel.getEsperienzaAccumulata(), utenteModel.getCreditoAccumulato());

		if (!utenteModel.getRuoli().isEmpty())
			result.ruoliIds = utenteModel.getRuoli().stream().map(r -> r.getId()).collect(Collectors.toList())
					.toArray(new Long[] {});

		return result;
	}

	public static List<UtenteDTO> buildUtenteDTOListFromModelList(List<Utente> modelList) {
		return modelList.stream().map(entity -> UtenteDTO.buildUtenteDTOFromModel(entity)).collect(Collectors.toList());
	}

	public static Set<UtenteDTO> buildUtenteDTOSetFromModelSet(Set<Utente> modelList) {
		return (Set<UtenteDTO>) modelList.stream().map(entity -> UtenteDTO.buildUtenteDTOFromModel(entity))
				.collect(Collectors.toSet());
	}

}
