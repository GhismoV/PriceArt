package it.ghismo.corso1.priceart.services;

import it.ghismo.corso1.priceart.entities.Listino;


public interface ListiniService {
	Listino insert(Listino item);
	Listino read(String key);
	void delete(String key);
	
}
