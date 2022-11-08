package it.ghismo.corso1.priceart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import it.ghismo.corso1.priceart.entities.DettaglioListino;

public interface DettagliListinoRepo extends JpaRepository<DettaglioListino, String> {
	Optional<DettaglioListino> findByCodArtAndListinoId(String codArt, String idListino);
	void deleteByCodArtAndListinoId(String codArt, String idListino);

}
