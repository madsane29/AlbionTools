package com.albiontools.generalController;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.albiontools.logger.CustomLogger;

@Controller
public class GeneralController {

	private CustomLogger logger;
	
	public GeneralController() {
		logger = new CustomLogger(getClass());
	}
	
	@GetMapping(value = {"/"})
	public String getHome(HttpServletRequest request, Locale locale) {
		//System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		logger.loggerInfoWithHttpServletRequestParam(request);
		return "index";
	}
	

}
