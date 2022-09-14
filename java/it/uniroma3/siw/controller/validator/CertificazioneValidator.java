package it.uniroma3.siw.controller.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Certificazione;

@Component
public class CertificazioneValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Certificazione.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Certificazione certificazione = (Certificazione) target;
		
		if(certificazione.getNome().trim().isEmpty())
			errors.reject("nome", "vuoto");			
	}

}
