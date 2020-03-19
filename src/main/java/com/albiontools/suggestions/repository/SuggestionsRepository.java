package com.albiontools.suggestions.repository;


import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.albiontools.suggestions.model.Suggestion;

@Repository
//@Transactional
public interface SuggestionsRepository extends JpaRepository<Suggestion, Long>{

}
