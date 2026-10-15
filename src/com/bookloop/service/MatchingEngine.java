package com.bookloop.service;

import com.bookloop.ai.ScoredBookMatch;
import com.bookloop.ai.SearchQueryIntent;
import com.bookloop.ai.SmartSearchPipeline;
import com.bookloop.model.Book;
import com.bookloop.model.BookStatus;
import com.bookloop.model.Student;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Concrete implementation of IMatchingEngine.
 * Integrates the AI heuristic pipeline with user profile context.
 */
public class MatchingEngine implements IMatchingEngine, Serializable {
    private static final long serialVersionUID = 1L;

    private final IBookService bookService;
    private final SmartSearchPipeline aiPipeline;

    public MatchingEngine(IBookService bookService) {
        this.bookService = bookService;
        this.aiPipeline = new SmartSearchPipeline();
    }

    @Override
    public List<ScoredBookMatch> findSmartRecommendations(String naturalLanguageQuery) {
        List<Book> available = bookService.getAvailableBooks();
        return aiPipeline.rankMatches(naturalLanguageQuery, available);
    }

    @Override
    public List<ScoredBookMatch> recommendForStudent(Student student) {
        if (student == null) return Collections.emptyList();

        List<Book> candidates = bookService.getAvailableBooks().stream()
                .filter(b -> b.getOwner() == null || !b.getOwner().getId().equalsIgnoreCase(student.getId()))
                .collect(Collectors.toList());

        // Build a combined interest query from student's department and wishlist
        List<String> queryParts = new ArrayList<>();
        if (student.getDepartment() != null) {
            queryParts.add(student.getDepartment());
        }
        for (String wish : student.getWishlist()) {
            queryParts.add(wish);
        }

        String combinedQuery = String.join(" ", queryParts);
        List<ScoredBookMatch> matches = aiPipeline.rankMatches(combinedQuery, candidates);

        // Fallback: If no direct wishlist matches, recommend top books in their department
        if (matches.isEmpty() && student.getDepartment() != null) {
            matches = aiPipeline.rankMatches(student.getDepartment(), candidates);
        }

        return matches;
    }

    @Override
    public SearchQueryIntent analyzeQuery(String naturalLanguageQuery) {
        return aiPipeline.parseQuery(naturalLanguageQuery);
    }
}
