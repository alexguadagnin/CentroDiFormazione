package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Certificazione;
import it.uniroma3.siw.model.Esame;
import it.uniroma3.siw.model.Utente;

public interface EsameRepository extends CrudRepository<Esame, Long>{

	Iterable<Esame> findAllByCertificazione(Certificazione certificazione);

}
