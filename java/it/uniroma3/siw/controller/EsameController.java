package it.uniroma3.siw.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.controller.validator.EsameValidator;
import it.uniroma3.siw.model.Certificazione;
import it.uniroma3.siw.model.Esame;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CertificazioneService;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.EsameService;
import it.uniroma3.siw.service.UtenteService;

@Controller
public class EsameController {

	@Autowired
	private EsameService esameService;

	@Autowired
	private CredenzialiService credenzialiService;

	@Autowired
	private CertificazioneService certificazioneService;

	@Autowired
	private EsameValidator esameValidator;

	@Autowired
	private UtenteService utenteService;

	@RequestMapping(value = "/confermaPrenotazione/{id}", method = RequestMethod.GET)
	public String toConfermaPrenotazioneEsame(@PathVariable("id") Long id, Model model) {

		Esame esameCorrente = esameService.findById(id);

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Utente utenteCorrente = credenzialiService.findByUsername(userDetails.getUsername()).getUtente();

		model.addAttribute("utente", utenteCorrente);
		model.addAttribute("esame", esameCorrente);

		return "prenota";

	}

	//Le date sono sicuramente prenotabili, controllo se già iscritto
	@RequestMapping(value = "/prenota/{id}", method = RequestMethod.POST)
	public String confermaPrenotazioneEsame(@PathVariable("id") Long id, Model model) {

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Utente utente = credenzialiService.findByUsername(userDetails.getUsername()).getUtente();
		Esame esameCorrente = esameService.findById(id);

		model.addAttribute("credenziali", credenzialiService.findByUsername(userDetails.getUsername()));

		//controllo se l'utente corrente è già iscritto
		if(esameCorrente.getIscritti().contains(utente)) {
			model.addAttribute("errorMessage", "Puoi iscriverti solo ad esami a cui non sei già iscritto!");
			model.addAttribute("certificazione", esameCorrente.getCertificazione());
			List<Esame> esame = esameService.findPrenotabiliByCertificazione(esameCorrente.getCertificazione());
			model.addAttribute("esami", esame);
			return "certificazione";
		}

		esameCorrente.getIscritti().add(utente);
		utente.getIscrizioni().add(esameCorrente);
		esameService.saveEsame(esameCorrente);
		utenteService.saveUtente(utente);


		model.addAttribute("certificazioni", certificazioneService.getAllCertificazioni());

		return "home";
	}

	@RequestMapping(value = "/admin/modificaEsame/{id}", method = RequestMethod.GET)
	public String toModificaCertificazione(@PathVariable("id") Long id, Model model) {
		model.addAttribute("esame", esameService.findById(id));
		return "admin/modificaEsame";
	}

	@RequestMapping(value = "/admin/modificaEsame/{id}", method = RequestMethod.POST)
	public String toModificaCertificazione(@PathVariable("id") Long id, @ModelAttribute("esame") Esame esame, BindingResult esameBindingResult , Model model) {
		Esame esameDaModificare = esameService.findById(id);
		//Si suppone che solo la data debba essere certa, le altre info
		//possono essere inserite successivamente
		esameValidator.validate(esame, esameBindingResult);
		if(!esameBindingResult.hasErrors()) {		
			esameDaModificare.setAula(esame.getAula());
			esameDaModificare.setData(esame.getData());
			esameDaModificare.setOra(esame.getOra());
			model.addAttribute("certificazioni", certificazioneService.getAllCertificazioni());
			return "admin/certificazioni";
		}
		
		model.addAttribute("errore", "errore");
		model.addAttribute("esame", esameDaModificare);
		return "admin/modificaEsame";
	}

	@RequestMapping(value = "/admin/nuovoEsame/{id}", method = RequestMethod.GET)
	public String toAggiungiEsame(@PathVariable("id") Long id, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("esame", new Esame());
		return "admin/nuovoEsame";
	}

	@RequestMapping(value = "/admin/nuovoEsame/{id}", method = RequestMethod.POST)
	public String aggiungiEsame(@PathVariable("id") Long id, @ModelAttribute("esame") Esame esame, BindingResult esameBindingResult, Model model) {
		esameValidator.validate(esame, esameBindingResult);

		if(!esameBindingResult.hasErrors()) {
			esame.setCertificazione(certificazioneService.findById(id));
			esameService.saveEsame(esame);
			model.addAttribute("certificazioni", certificazioneService.getAllCertificazioni());
			return "admin/certificazioni";
		}

		model.addAttribute("esame", esame);
		return "admin/nuovoEsame";
	}
	
}
