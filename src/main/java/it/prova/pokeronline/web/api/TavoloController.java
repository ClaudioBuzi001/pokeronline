package it.prova.pokeronline.web.api;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.prova.pokeronline.dto.TavoloDTO;
import it.prova.pokeronline.service.TavoloService;


@RestController
@RequestMapping("api/tavolo")
public class TavoloController {
	
	private TavoloService tavoloService;
	
	@GetMapping
	public List<TavoloDTO> getAll() {
		// senza DTO qui hibernate dava il problema del N + 1 SELECT
		// (probabilmente dovuto alle librerie che serializzano in JSON)
//		return RegistaDTO.createRegistaDTOListFromModelList(registaService.listAllElementsEager(), true);
	


}


















