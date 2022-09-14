package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Certificazione;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.CertificazioneRepository;

@Service
public class CertificazioneService {

	@Autowired
	protected CertificazioneRepository certificazioneRepository;
	
	@Transactional
	public List<Certificazione> getAllCertificazioni(){
		List<Certificazione> result = new ArrayList<>();
		Iterable<Certificazione> iterable = this.certificazioneRepository.findAll();
		for(Certificazione certificazione : iterable)
			result.add(certificazione);
		return result;
	}
	
	@Transactional
	public Certificazione saveCertificazioni(Certificazione certificazione) {	
		return this.certificazioneRepository.save(certificazione);
	}

	@Transactional
	public Certificazione findById(Long id) {
		return this.certificazioneRepository.findById(id).get();
	}
	
}
