package com.albiontools.generalController;

import java.time.LocalDateTime;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.albiontools.logger.CustomLogger;
import com.albiontools.security.account.repository.UserRepository;
import com.albiontools.suggestions.model.Suggestion;
import com.albiontools.suggestions.repository.SuggestionsRepository;

@Controller
public class GeneralController {

	private CustomLogger logger;
	
	public GeneralController() {
		logger = new CustomLogger(getClass());
	}

	
	@GetMapping(value = { "/" })
	public String getHome(HttpServletRequest request, Locale locale) {
		// System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		logger.loggerInfoWithHttpServletRequestParam(request);
		
		System.out.println();

		return "index";
	}
	

}
