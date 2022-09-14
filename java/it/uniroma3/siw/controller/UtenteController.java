package it.uniroma3.siw.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.controller.validator.CredenzialiValidator;
import it.uniroma3.siw.controller.validator.UtenteValidator;
import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Esame;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CertificazioneService;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.EsameService;
import it.uniroma3.siw.service.UtenteService;

@Controller
public class UtenteController {

	@Autowired
	private UtenteValidator utenteValidator;

	@Autowired
	private CredenzialiValidator credenzialiValidator;

	@Autowired
	private EsameService esameService;

	@Autowired
	private CredenzialiService credenzialiService;

	@Autowired
	private UtenteService utenteService;

	@Autowired
	private CertificazioneService certificazioneService;

	@RequestMapping(value = "/iscrizioni", method = RequestMethod.GET)
	public String showRegisterForm(Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Utente utente = credenzialiService.findByUsername(userDetails.getUsername()).getUtente();

		List<Esame> annullabili = new ArrayList<>();
		List<Esame> nonAnnullabili = new ArrayList<>();
		List<Esame> iscrizioni = utente.getIscrizioni();

		for(Esame esame : iscrizioni) {
			//data ancora non arrivata?
			if(LocalDate.parse(esame.getData()).compareTo(LocalDate.now()) > 0)
				annullabili.add(esame);
			else nonAnnullabili.add(esame);
		}
		model.addAttribute("credenziali", credenzialiService.findByUsername(userDetails.getUsername()));
		model.addAttribute("annullabili", annullabili);
		model.addAttribute("nonAnnullabili", nonAnnullabili);
		return "iscrizioni";
	}

	@RequestMapping(value = "/toAnnullaPrenotazione/{id}", method = RequestMethod.GET)
	public String toAnnullaPrenotazione(@PathVariable("id") Long id, Model model) {
		Esame esameCorrente = esameService.findById(id);
		model.addAttribute("esame", esameCorrente);

		return "annullaPrenotazione";
	}

	@RequestMapping(value = "/annullaPrenotazione/{id}")
	public String annullaPrenotazione(@PathVariable("id") Long id, Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Utente utente = credenzialiService.findByUsername(userDetails.getUsername()).getUtente();

		Esame esame = esameService.findById(id);
		esame.getIscritti().remove(utente);
		esameService.saveEsame(esame);	

		utente.getIscrizioni().remove(esame);
		utenteService.saveUtente(utente);

		model.addAttribute("credenziali", credenzialiService.findByUsername(userDetails.getUsername()));
		model.addAttribute("certificazioni", certificazioneService.getAllCertificazioni());

		return "home";
	}

	@RequestMapping(value = "/admin/home", method = RequestMethod.GET)
	public String showHome(Model model) {
		return "admin/home";
	}

	@RequestMapping(value = "/admin/nuovoStudente", method = RequestMethod.GET)
	public String nuovoStudenteForm(Model model) {
		model.addAttribute("credenziali", new Credenziali());
		return "admin/nuovoStudente";
	}

	@RequestMapping(value = "/admin/nuovoStudente", method = RequestMethod.POST)
	public String nuovoStudente(@ModelAttribute("credenziali") Credenziali credenziali,
			BindingResult utenteBingingResult,
			BindingResult credenzialiBindingResult,
			Model model) {

		this.utenteValidator.validate(credenziali.getUtente(), utenteBingingResult);
		this.credenzialiValidator.validate(credenziali, credenzialiBindingResult);

		if(!utenteBingingResult.hasErrors() && !credenzialiBindingResult.hasErrors()) {
			credenzialiService.saveCredenziali(credenziali);
			return "admin/home";
		}
		model.addAttribute("credenziali", credenziali);
		return "admin/nuovoStudente";
	}
}