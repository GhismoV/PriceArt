package it.ghismo.corso1.priceart.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.ghismo.corso1.priceart.entities.DettaglioListino;
import it.ghismo.corso1.priceart.repository.DettagliListinoRepo;

@Service
@Transactional(readOnly = true)
public class PrezziServiceImpl implements PrezziService {
	@Autowired
	private DettagliListinoRepo rep;
	
	@Override
	public DettaglioListino read(String idListino, String codArt) {
		return rep.findByCodArtAndListinoId(codArt, idListino).orElse(null);
	}
	
	@Transactional
	@Override
	public void delete(String idListino, String codArt) {
		rep.deleteByCodArtAndListinoId(codArt, idListino);
	}


}
