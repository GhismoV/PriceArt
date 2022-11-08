package it.ghismo.corso1.priceart.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.ghismo.corso1.priceart.appconf.AppConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/info")
//@CrossOrigin(value = "http://localhost:4200")
@Slf4j
public class InfoController {
	@Autowired
	private AppConfig confListino;
	

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@Validated
	@SneakyThrows
	public ResponseEntity<AppConfig> getInfoListino() {
		log.debug("**** Verifichiamo il codice listino configurato ****");
		log.debug("Listino trovato: {}", confListino);
		return new ResponseEntity<AppConfig>(confListino, HttpStatus.OK);
	}
	
}
