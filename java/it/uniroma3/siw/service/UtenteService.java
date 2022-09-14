package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.controller.UtenteController;
import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Esame;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.UtenteRepository;

@Service
public class UtenteService {

	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Autowired
	protected UtenteRepository utenteRepository;

	@Transactional
	public Utente getUtente(Long id) {
		Optional<Utente> result = this.utenteRepository.findById(id);
		return result.orElse(null);
	}

	@Transactional
	public Utente saveUtente(Utente utente) {
		return this.utenteRepository.save(utente);
	}

	@Transactional
	public List<Utente> getAllUtenti(){
		List<Utente> result = new ArrayList<>();
		Iterable<Utente> iterable = this.utenteRepository.findAll();
		for(Utente utente : iterable)
			result.add(utente);
		return result;
	}

	@Transactional
	public List<Utente> getAllStudenti() {
		List<Utente> result = new ArrayList<>();
		Iterable<Utente> iterable = this.utenteRepository.findAll();
		for(Utente utente : iterable) 
			if(utente.getCredenziali().getRuolo().equals(Credenziali.DEFAULT_ROLE))
				result.add(utente);

		return result;
	}
}
