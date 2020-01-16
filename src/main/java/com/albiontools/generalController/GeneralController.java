package com.albiontools.generalController;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@GetMapping(value = {"/"})
	public String getHome() {
		//logger.info("Application has started!");
		return "index";
	}
}
