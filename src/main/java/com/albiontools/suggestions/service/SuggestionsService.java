package com.albiontools.suggestions.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.criteria.Order;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.albiontools.security.account.repository.UserRepository;
import com.albiontools.suggestions.model.NewSuggestion;
import com.albiontools.suggestions.model.Suggestion;
import com.albiontools.suggestions.repository.SuggestionsRepository;

@Service
public class SuggestionsService {
	
	private SuggestionsRepository suggestionsRepository;
	private UserRepository userRepository;
	
	public SuggestionsService(UserRepository userRepo, SuggestionsRepository suggestionsRepo) {
		this.userRepository = userRepo;
		this.suggestionsRepository = suggestionsRepo;
	}
	public void createNewSuggestion(NewSuggestion newSuggestion) {
		Suggestion suggestion = new Suggestion(newSuggestion, userRepository.findById(newSuggestion.getUser_id()).get());
		
		suggestionsRepository.save(suggestion);
	}
	
	public void editSuggestion(NewSuggestion newSuggestion) {
		
	}
	
	public List<Suggestion> getAllSuggestions() {
		return suggestionsRepository.findAll();
	}

}
