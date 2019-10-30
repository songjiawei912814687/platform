package com.wechatsug.mapper.repository;


import com.wechatsug.model.Suggestions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggesstionsRepository extends JpaRepository<Suggestions, Integer> {


    Suggestions findSuggestionsById(Integer id);

}
