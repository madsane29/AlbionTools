package com.albiontools.suggestions.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.albiontools.security.account.model.Role;
import com.albiontools.security.account.model.User;
import com.albiontools.security.account.repository.RoleRepository;
import com.albiontools.security.account.repository.UserRepository;
import com.albiontools.suggestions.model.NewSuggestion;
import com.albiontools.suggestions.model.Suggestion;
import com.albiontools.suggestions.repository.SuggestionsRepository;
import com.albiontools.suggestions.service.SuggestionsService;

@RestController
public class SuggestionsRestController {


	private SuggestionsService suggestionsService;
	
	public SuggestionsRestController(SuggestionsService suggestionsService) {
		this.suggestionsService = suggestionsService;
	}

	@GetMapping("/suggestions")
	public List<Suggestion> returnAllSuggestions() {
		return suggestionsService.getAllSuggestions();
	}
	
	@PutMapping("/edit-suggestion")
	public List<Suggestion> editSuggestion(@RequestBody NewSuggestion newSuggestion) {
		suggestionsService.editSuggestion(newSuggestion);
		return suggestionsService.getAllSuggestions();
	}
	
	@PostMapping("/new-suggestion")
	public List<Suggestion> createNewSuggestion(@RequestBody NewSuggestion newSuggestion) {
		suggestionsService.createNewSuggestion(newSuggestion);
		return suggestionsService.getAllSuggestions();
	}

	
}
