package com.albiontools.generalController;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.albiontools.logger.CustomLogger;

@Controller
public class GeneralController {

	private CustomLogger logger;
	
	public GeneralController() {
		logger = new CustomLogger(getClass());
	}
	
	@GetMapping(value = {"/"})
	public String getHome(HttpServletRequest request) {
		logger.loggerInfoWithHttpServletRequestParam(request);
		return "index";
	}
}
