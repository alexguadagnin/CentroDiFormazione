package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.controller.validator.CertificazioneValidator;
import it.uniroma3.siw.model.Certificazione;
import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Esame;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CertificazioneService;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.EsameService;
import it.uniroma3.siw.service.UtenteService;

@Controller
public class CertificazioneController {

	@Autowired
	private CertificazioneService certificazioneService;

	@Autowired
	private EsameService esameService;

	@Autowired
	private CertificazioneValidator certificazioneValidator;

	@RequestMapping(value = "/certificazione/{id}", method = RequestMethod.GET)
	public String showCertificazione(@PathVariable("id") Long id, Model model) {
		Certificazione certificazione = certificazioneService.findById(id);
		List<Esame> esame = esameService.findPrenotabiliByCertificazione(certificazione);
		model.addAttribute("certificazione", certificazione);
		model.addAttribute("esami", esame);
		return "certificazione";
	}

	@RequestMapping(value = "/admin/nuovaCertificazione", method = RequestMethod.GET)
	public String nuovaCertificazioneForm(Model model) {
		model.addAttribute("certificazione", new Certificazione());
		return "admin/nuovaCertificazione";
	}

	@RequestMapping(value = "/admin/nuovaCertificazione", method = RequestMethod.POST)
	public String nuovoStudente(@ModelAttribute("certificazione") Certificazione certificazione, Model model, BindingResult certificazioneBindingResult) {
		certificazioneValidator.validate(certificazione, certificazioneBindingResult);

		if(!certificazioneBindingResult.hasErrors()) {
			certificazioneService.saveCertificazioni(certificazione);
			return "admin/home";
		}

		model.addAttribute("certificazione", certificazione);
		return "admin/nuovaCertificazione";
	}

	@RequestMapping(value = "/admin/certificazioni", method = RequestMethod.GET)
	public String showCertificazioni(Model model) {
		model.addAttribute("certificazioni", certificazioneService.getAllCertificazioni());
		return "admin/certificazioni";
	}

	@RequestMapping(value = "/admin/modificaCertificazione/{id}", method = RequestMethod.GET)
	public String toModificaCertificazione(@PathVariable("id") Long id, Model model) {
		model.addAttribute("certificazione", certificazioneService.findById(id));
		return "admin/modificaCertificazione";
	}

	@RequestMapping(value = "/admin/modificaCertificazione/{id}", method = RequestMethod.POST)
	public String modificaCertificazione(@PathVariable("id") Long id,@ModelAttribute("certificazione") Certificazione certificazione, BindingResult certificazioneBindingResult, Model model) {
		Certificazione certificazioneDaModificare = certificazioneService.findById(id);
		certificazioneValidator.validate(certificazione, certificazioneBindingResult);
		
		if(!certificazioneBindingResult.hasErrors()) {
			certificazioneDaModificare.setDescrizione(certificazione.getDescrizione());
			certificazioneDaModificare.setNome(certificazione.getNome());
			certificazioneService.saveCertificazioni(certificazioneDaModificare);
			model.addAttribute("certificazioni", certificazioneService.getAllCertificazioni());
			return "admin/certificazioni";
		}
		
		model.addAttribute("credenzaili", certificazioneDaModificare);
		return "admin/modificaCertificazione";
	}

}
