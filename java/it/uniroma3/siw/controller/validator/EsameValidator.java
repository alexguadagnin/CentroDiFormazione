package it.uniroma3.siw.controller.validator;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Esame;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CredenzialiService;

@Component
public class EsameValidator implements Validator{

	@Autowired
	private CredenzialiService credenzialiService;

	@Override
	public boolean supports(Class<?> clazz) {
		return Esame.class.equals(clazz);
	}

	//controllo sulla data
	@Override
	public void validate(Object target, Errors errors) {
		Esame esame = (Esame) target;

		LocalDate oggi = LocalDate.now();
		//la data deve essere in formato ISO
		if(esame.getData() == null || esame.getData().isEmpty()) {
			errors.reject("data", "blank");
		}else
			//controllo formato data
			if(!esame.getData().matches("^\\d{4}-([0]\\d|1[0-2])-([0-2]\\d|3[01])")) {
				errors.reject("data", "formato-ISO");
			} else {
				LocalDate dataEsame = LocalDate.parse(esame.getData());
				if(oggi.compareTo(dataEsame) > 0 ) {
					//esame oltrepassato
					errors.reject("data", "oltrepassata");
				}
			}
	}
}


