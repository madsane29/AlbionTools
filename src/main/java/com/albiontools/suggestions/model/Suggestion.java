package com.albiontools.suggestions.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.albiontools.security.account.model.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity(name = "suggestions")
public class Suggestion {
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	private String title;
	private String description;
	private LocalDateTime created;

	public Suggestion() {
	}

	public Suggestion(String title, String description, LocalDateTime created, User user) {
		super();
		this.title = title;
		this.description = description;
		this.created = created;
		this.user = user;
	}
	
	public Suggestion(NewSuggestion newSuggestion, User user) {
		this.title = newSuggestion.getTitle();
		this.description = newSuggestion.getDescription();
		this.created = newSuggestion.getCreated();
		this.user = user;
	}
	

	@ManyToOne
	@JsonIgnore
	@JoinColumn(name="user_id")
	private User user;
	
	@JsonProperty(value = "user_id")
	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
	@JsonIdentityReference(alwaysAsId = true) 
	public User getUserIDForSerializing() {
	    return user;
	}

	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return "Suggestion [id=" + id + ", title=" + title + ", description=" + description + ", created=" + created
				+ ", user=" + user.toString() + "]";
	}

	

}
