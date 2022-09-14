package it.uniroma3.siw.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.controller.validator.CredenzialiValidator;
import it.uniroma3.siw.controller.validator.UtenteValidator;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.UtenteService;

@Controller
public class MainController {

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private CredenzialiService credenzialiService;

	@Autowired
	private UtenteValidator utenteValidator;

	@Autowired
	private CredenzialiValidator credenzialiValidator;

	@RequestMapping(value = "/admin/studenti", method = RequestMethod.GET)
	public String showStudenti(Model model) {
		List<Utente> studenti = utenteService.getAllStudenti();
		model.addAttribute("studenti", studenti);
		return "admin/studenti";
	}

	@RequestMapping(value = "/admin/modificaStudente/{id}", method = RequestMethod.GET)
	public String toModificaStudente(@PathVariable("id") Long id, Model model) {
		Utente studente = utenteService.getUtente(id);
		model.addAttribute("studente", studente);

		return "admin/modificaStudente";
	}

	@RequestMapping(value = "/admin/modificaStudente/{id}", method = RequestMethod.POST)
	public String modificaStudente(@PathVariable("id") Long id,@ModelAttribute("studente") Utente utente, BindingResult utenteBindingResult, BindingResult credenzialiBindingResult, Model model) {

		Utente utenteDaModificare = credenzialiService.getCredenziali(id).getUtente();

		utenteValidator.validate(utente, utenteBindingResult);

		if(!utenteBindingResult.hasErrors() && (utente.getCredenziali().getUsername().length() >= credenzialiValidator.MIN_USERNAME_LENGTH && utente.getCredenziali().getUsername().length() <= credenzialiValidator.MAX_USERNAME_LENGTH)) {
			utenteDaModificare.setNome(utente.getNome());
			utenteDaModificare.setCognome(utente.getCognome());
			utenteDaModificare.setUsername(utente.getUsername());
			credenzialiService.saveCredenziali(utenteDaModificare.getCredenziali());
			List<Utente> studenti = utenteService.getAllStudenti();
			model.addAttribute("studenti", studenti);
			
			return "admin/studenti";
		}
		
		model.addAttribute("studente", credenzialiService.getCredenziali(id).getUtente());
		return "admin/modificaStudente";
	}

}
