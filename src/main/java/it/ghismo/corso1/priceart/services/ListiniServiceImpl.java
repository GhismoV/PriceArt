package it.ghismo.corso1.priceart.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.ghismo.corso1.priceart.entities.Listino;
import it.ghismo.corso1.priceart.repository.ListiniRepo;

@Service
@Transactional(readOnly = true)
public class ListiniServiceImpl implements ListiniService {
	@Autowired
	private ListiniRepo rep;
	
	
	@Transactional
	@Override
	public Listino insert(Listino item) {
		return rep.save(item);
	}
	
	@Override
	public Listino read(String key) {
		return rep.findById(key).orElse(null);
	}
	
	@Transactional
	@Override
	public void delete(String key) {
		rep.deleteById(key);
	}


}
