package com.bookloop.ai;

import com.bookloop.model.Book;

/**
 * Represents a search recommendation match with a calculated semantic score and rationale.
 */
public class ScoredBookMatch implements Comparable<ScoredBookMatch> {
    private final Book book;
    private final double matchScore; // 0.0 - 100.0
    private final String matchExplanation;

    public ScoredBookMatch(Book book, double matchScore, String matchExplanation) {
        this.book = book;
        this.matchScore = Math.min(100.0, Math.max(0.0, matchScore));
        this.matchExplanation = matchExplanation;
    }

    public Book getBook() {
        return book;
    }

    public double getMatchScore() {
        return matchScore;
    }

    public String getMatchExplanation() {
        return matchExplanation;
    }

    @Override
    public int compareTo(ScoredBookMatch other) {
        // Descending order of match score
        return Double.compare(other.matchScore, this.matchScore);
    }

    @Override
    public String toString() {
        return String.format("[%.1f%% Match] %s | %s", matchScore, book.getTitle(), matchExplanation);
    }
}
