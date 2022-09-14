package it.uniroma3.siw.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Certificazione;
import it.uniroma3.siw.model.Esame;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.EsameRepository;

@Service
public class EsameService {

	@Autowired
	protected EsameRepository esameRepository;
	
	@Transactional
	public Esame findById(Long id) {
		return esameRepository.findById(id).get();
	}

	@Transactional
	public void saveEsame(Esame esame) {
		esameRepository.save(esame);
	}

	@Transactional
	public List<Esame> findPrenotabiliByCertificazione(Certificazione certificazione) {
		List<Esame> result = new ArrayList<>();
		Iterable<Esame> iterable = this.esameRepository.findAllByCertificazione(certificazione);
		for(Esame esame: iterable) {
			if(LocalDate.parse(esame.getData()).compareTo(LocalDate.now()) > 0 ) {
				result.add(esame);
			}
		}
		return result;
	}

}
