package it.ghismo.corso1.priceart.controllers;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.ghismo.corso1.priceart.appconf.AppConfig;
import it.ghismo.corso1.priceart.dto.ResultDto;
import it.ghismo.corso1.priceart.entities.DettaglioListino;
import it.ghismo.corso1.priceart.errors.ResultEnum;
import it.ghismo.corso1.priceart.exceptions.DuplicateException;
import it.ghismo.corso1.priceart.exceptions.NotFoundException;
import it.ghismo.corso1.priceart.services.PrezziService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/prezzi")
//@CrossOrigin(value = {"http://localhost:4200"}) -- ghismo : aggiunto classe FiltersCorsConfig per gestire questa cosa
@Slf4j
@Validated
public class PrezziController {
	
	private static final String IDLIST_REGEX = "[0-9]{1,3}";
	private static final String CODART_REGEX = "[A-Za-z0-9]{8,20}";

	@Autowired
	private PrezziService svc;
	
	@Autowired
	private AppConfig confListino;
	
	
	@GetMapping(value = {"/cerca/{codArt}/{idList}", "/cerca/{codArt}"}, produces = MediaType.APPLICATION_JSON_VALUE)
	@SneakyThrows
	public ResponseEntity<Double> cerca(
			@PathVariable(name = "codArt", required = true)
			@NotNull
			@Pattern(regexp = CODART_REGEX)
			String codArt,
			
			@PathVariable(name = "idList", required = false)
			@Pattern(regexp = IDLIST_REGEX)
			String idListino
			) {
		log.debug("**** Cerchiamo di ottenere il prezzo dell'articolo [{}] in base al listino [{}] ****", codArt, Objects.requireNonNullElse(idListino, "default:" + confListino.getListino()) );
		String idListinoToUse = Objects.requireNonNullElse(idListino, confListino.getListino());
		DettaglioListino prezzo = svc.read(idListinoToUse, codArt);
		log.info("Prezzo: {}", prezzo);
		if(prezzo == null) throw new NotFoundException("Il prezzo", String.format("[%s, %s]", codArt, idListinoToUse));
		return new ResponseEntity<Double>(prezzo.getPrezzo(), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/elimina/{codArt}/{idList}", produces = {MediaType.APPLICATION_JSON_VALUE})
	@SneakyThrows
	public ResponseEntity<ResultDto> delete(
			@PathVariable(name = "codArt", required = true)
			@NotNull
			@Pattern(regexp = CODART_REGEX)
			String codArt,
			
			@PathVariable(name = "idList", required = true)
			@NotNull
			@Pattern(regexp = IDLIST_REGEX)
			String idListino
			) {
		log.debug("**** Cerchiamo di eliminare il prezzo dell'articolo [{}] in base al listino [{}] ****", codArt, idListino);
		
		DettaglioListino dtl = svc.read(idListino, codArt);
		log.info("Prezzo: {}", dtl);
		if(dtl == null) {
			log.warn("Prezzo [{}, {}] non trovato.", codArt, idListino);
			throw new NotFoundException("Prezzo", String.format("[%s, %s]", codArt, idListino));
		} else if(idListino.length() < 3) { // evitiamo la cancellazione dei dati del db del corso
			log.warn("Prezzo [{}, {}] fa parte della base dati del corso e quindi non è eliminabile", codArt, idListino);
			throw new DuplicateException(String.format("[%s, %s]", codArt, idListino));
		}
		svc.delete(idListino, codArt);
		return new ResponseEntity<ResultDto>(ResultEnum.OkParam2.getDto(codArt, idListino), HttpStatus.OK);
	}
	
}
