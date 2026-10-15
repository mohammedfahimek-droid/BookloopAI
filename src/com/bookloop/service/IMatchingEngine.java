package com.bookloop.service;

import com.bookloop.ai.ScoredBookMatch;
import com.bookloop.ai.SearchQueryIntent;
import com.bookloop.model.Student;

import java.util.List;

/**
 * High-level AI matching engine interface orchestrating semantic search and student recommendations.
 */
public interface IMatchingEngine {
    List<ScoredBookMatch> findSmartRecommendations(String naturalLanguageQuery);
    List<ScoredBookMatch> recommendForStudent(Student student);
    SearchQueryIntent analyzeQuery(String naturalLanguageQuery);
}
