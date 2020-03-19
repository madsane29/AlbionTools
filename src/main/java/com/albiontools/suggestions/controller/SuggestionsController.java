package com.albiontools.suggestions.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SuggestionsController {

	@GetMapping("/suggestionss")
	public String getSuggestionsPage() {
		return "/suggestions/suggestions";
	}
	
	
}
