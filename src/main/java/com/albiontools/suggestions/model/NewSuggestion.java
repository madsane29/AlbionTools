package com.albiontools.suggestions.model;

import java.time.LocalDateTime;

public class NewSuggestion {
	private long id;
	private String title;
	private String description;
	private LocalDateTime created;
	private Long user_id;
	
	public NewSuggestion() {}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public Long getUser_id() {
		return user_id;
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

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "NewSuggestion [id=" + id + ", title=" + title + ", description=" + description + ", created=" + created
				+ ", user_id=" + user_id + "]";
	}

	
	
}
