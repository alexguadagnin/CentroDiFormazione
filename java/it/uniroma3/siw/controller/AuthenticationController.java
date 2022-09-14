package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.controller.validator.CredenzialiValidator;
import it.uniroma3.siw.controller.validator.UtenteValidator;
import it.uniroma3.siw.model.Certificazione;
import it.uniroma3.siw.model.Credenziali;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CertificazioneService;
import it.uniroma3.siw.service.CredenzialiService;
import it.uniroma3.siw.service.UtenteService;

@Controller
public class AuthenticationController {

	@Autowired
	private UtenteValidator utenteValidator;

	@Autowired
	private CredenzialiValidator credenzialiValidator;

	@Autowired
	private CredenzialiService credenzialiService;

	@Autowired
	private CertificazioneService certificazioneService;

	@Autowired
	private UtenteService utenteService;

	@RequestMapping(value = "/login", method = RequestMethod.GET) 
	public String showLoginForm (Model model) {
		model.addAttribute("credenziali", new Credenziali());
		return "loginForm";
	}

	@RequestMapping(value = "/login-failure", method = RequestMethod.POST) 
	public String showLoginFormError (@ModelAttribute() Credenziali credenziali, BindingResult credenzialiBindingResult, Model model) {
		credenzialiBindingResult.reject("login.credenziali");
		model.addAttribute("credenziali", credenziali);
		return "loginForm";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET) 
	public String logout(Model model) {
		return "index";
	}

	@RequestMapping(value = "/registerAllievo", method = RequestMethod.GET)
	public String showRegisterForm(Model model) {
		model.addAttribute("credenziali", new Credenziali());
		return "registerAllievo";
	}

	@RequestMapping(value = "/registerAllievo", method = RequestMethod.POST)
	public String registerAllievo(/*@ModelAttribute("utente") Utente utente,*/
			@ModelAttribute("credenziali") Credenziali credenziali,
			BindingResult utenteBingingResult,
			BindingResult credenzialiBindingResult,
			Model model) {


		//validate dell'utente e delle credenziali
		this.utenteValidator.validate(credenziali.getUtente(), utenteBingingResult);
		this.credenzialiValidator.validate(credenziali, credenzialiBindingResult);

		//se non sono presenti errori salviamo nel DB
		if(!utenteBingingResult.hasErrors() && !credenzialiBindingResult.hasErrors()) {
			utenteService.saveUtente(credenziali.getUtente());
			credenzialiService.saveCredenziali(credenziali);
			return "loginForm";
		}
		return "registerAllievo";
	}

	@RequestMapping(value = "/default", method = RequestMethod.GET)
	public String successfulLoginAllievo(Model model) {

		model.addAttribute("certificazioni", certificazioneService.getAllCertificazioni());

		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credenziali credenziali = credenzialiService.getCredenziali(userDetails.getUsername());
		model.addAttribute("credenziali", credenziali);
		if(credenziali.getRuolo().equals(Credenziali.ADMIN_ROLE)) {
			return "admin/home";
		}	
		return "home";
	}

}
