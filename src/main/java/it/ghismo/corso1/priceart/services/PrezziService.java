package it.ghismo.corso1.priceart.services;

import it.ghismo.corso1.priceart.entities.DettaglioListino;


public interface PrezziService {
	DettaglioListino read(String idListino, String codArt);
	void delete(String idListino, String codArt);
	
}
