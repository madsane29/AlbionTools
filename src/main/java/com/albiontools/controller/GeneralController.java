package com.albiontools.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeneralController {
	
	@GetMapping(value = {"/"})
	public String getHome() {
		return "index";
	}

	@GetMapping("/trading")
	public String getTradingPage() {
		return "trading";
	}

	
	
	
	
}
