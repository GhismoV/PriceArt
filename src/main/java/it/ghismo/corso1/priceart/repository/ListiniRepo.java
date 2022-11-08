package it.ghismo.corso1.priceart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.ghismo.corso1.priceart.entities.Listino;

public interface ListiniRepo extends JpaRepository<Listino, String> {

}
