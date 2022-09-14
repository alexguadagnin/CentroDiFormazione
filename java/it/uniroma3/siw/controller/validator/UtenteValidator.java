package it.uniroma3.siw.controller.validator;

import org.apache.catalina.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Utente;

@Component
public class UtenteValidator implements Validator{

	final Integer MAX_NAME_LENGTH = 14;
    final Integer MIN_NAME_LENGTH = 4;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Utente.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Utente utente = (Utente) target;
        String nome = utente.getNome().trim();
        String cognome = utente.getCognome().trim();

        if (nome.isEmpty())
            errors.rejectValue("nome", "required");
        else if (nome.length() < MIN_NAME_LENGTH || nome.length() > MAX_NAME_LENGTH)
            errors.rejectValue("nome", "size");

        if (cognome.isEmpty())
            errors.rejectValue("cognome", "required");
        else if (cognome.length() < MIN_NAME_LENGTH || cognome.length() > MAX_NAME_LENGTH)
            errors.rejectValue("cognome", "size");
		
	}

}
