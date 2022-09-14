package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.CredenzialiRepository;

@Service
public class CredenzialiService {

	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Autowired
	protected CredenzialiRepository credenzialiRepository;

	@Transactional
	public Credenziali getCredenziali(Long id) {
		Optional<Credenziali> result = this.credenzialiRepository.findById(id);
		return result.orElse(null);
	}

	@Transactional
	public Credenziali getCredenziali(String username) {
		Optional<Credenziali> result = this.credenzialiRepository.findByUsername(username);
		return result.orElse(null);
	}

	@Transactional
	public Credenziali saveCredenziali(Credenziali credentials) {
		credentials.setRuolo(Credenziali.DEFAULT_ROLE);
		credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
		return this.credenzialiRepository.save(credentials);
	}

	@Transactional
	public Credenziali findByUsername(String username) {
		return credenzialiRepository.findByUsername(username).get();
	}

	public Credenziali findById(Long id) {
		return credenzialiRepository.findById(id).get();
	}

}
