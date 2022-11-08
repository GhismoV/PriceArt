package it.ghismo.corso1.priceart.controllers;

import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.ghismo.corso1.priceart.dto.ResultDto;
import it.ghismo.corso1.priceart.entities.DettaglioListino;
import it.ghismo.corso1.priceart.entities.Listino;
import it.ghismo.corso1.priceart.errors.ResultEnum;
import it.ghismo.corso1.priceart.exceptions.BindingValidationException;
import it.ghismo.corso1.priceart.exceptions.DuplicateException;
import it.ghismo.corso1.priceart.exceptions.NotFoundException;
import it.ghismo.corso1.priceart.services.ListiniService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/listino")
//@CrossOrigin(value = {"http://localhost:4200"}) -- ghismo : aggiunto classe FiltersCorsConfig per gestire questa cosa
@Slf4j
@Validated
public class ListiniController {
	
	private static final String IDLIST_REGEX = "[0-9]{3}";
	
	@Autowired
	private ListiniService svc;
	
	
	@Autowired
	private ResourceBundleMessageSource rb;
	
	
	@GetMapping(value = "/cerca/id/{idList}", produces = MediaType.APPLICATION_JSON_VALUE)
	@SneakyThrows
	public ResponseEntity<Listino> cerca(
			@PathVariable(name = "idList", required = true)
			@NotNull
			@Pattern(regexp = IDLIST_REGEX)
			String idListino) {
		
		log.debug("**** Cerchiamo di ottenere il listino attraverso il codice [{}] ****", idListino);
		
		Listino listino = svc.read(idListino);
		log.info("Listino: {}", listino);
		
		if(listino == null) throw new NotFoundException("Il listino", idListino);
		
		return new ResponseEntity<Listino>(listino, HttpStatus.OK);
	}
	
	@PostMapping(value = "/inserisci", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
	@SneakyThrows
	public ResponseEntity<ResultDto> create(
			@RequestBody(required = true)
			@Valid
			Listino in,
			
			BindingResult bindingResult
			) {
		log.debug("**** Cerchiamo di inserire il listino [{}]", in.getId());

		/* abilitiamo anche la modifica
		Listino listinoChk = checkInputArt(in, bindingResult);
		if(listinoChk != null) {
			log.warn("Listino [{}] già esistente!", in.getId());
			throw new DuplicateException(in.getId());
		}
		*/

		Set<DettaglioListino> dettagli = in.getDettagli();
		if(dettagli != null) {
			dettagli.stream().forEach(b -> b.setListino(in));
		}
		
		Listino outEntity = svc.insert(in);
		
		return new ResponseEntity<ResultDto>(ResultEnum.OkParam1.getDto(outEntity.getId()), HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/elimina/{idList}", produces = {MediaType.APPLICATION_JSON_VALUE})
	@SneakyThrows
	public ResponseEntity<ResultDto> delete(
			@PathVariable(name = "idList", required = true)
			@NotNull
			@Pattern(regexp = IDLIST_REGEX)
			String idListino) {
		log.debug("**** Cerchiamo di eliminare il listino attraverso il codice [{}] ****", idListino);
		
		Listino listino = svc.read(idListino);
		log.info("Listino: {}", listino);
		if(listino == null) {
			log.warn("Listino [{}] non trovato.", idListino);
			throw new NotFoundException("Listino", idListino);
		} else if(idListino.length() < 3) { // evitiamo la cancellazione dei dati del db del corso
			log.warn("Listino [{}] fa parte della base dati del corso e quindi non è eliminabile", idListino);
			throw new DuplicateException(idListino);
		}

		svc.delete(idListino);
		
		return new ResponseEntity<ResultDto>(ResultEnum.OkParam1.getDto(idListino), HttpStatus.OK);
	}
	
	
	@SneakyThrows
	private Listino checkInputArt(Listino in, BindingResult bindingResult) {
		log.debug("Listino: {}", in.toString());
		if(bindingResult.hasErrors()) {
			FieldError f = bindingResult.getFieldError();
			throw new BindingValidationException(f, rb);
		}
		return svc.read(in.getId());
	}
	
	
}
