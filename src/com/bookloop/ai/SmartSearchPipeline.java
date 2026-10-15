package com.bookloop.ai;

import com.bookloop.model.Book;
import com.bookloop.model.Condition;
import com.bookloop.model.ListingMode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * AI-Powered Semantic Search & Intent Parsing Pipeline.
 * Parses natural language queries into structured intent and computes ranking scores.
 */
public class SmartSearchPipeline {

    private static final Set<String> STOP_WORDS = Set.of(
            "a", "an", "the", "and", "or", "in", "on", "at", "to", "for", "of", "with",
            "by", "from", "is", "are", "i", "need", "want", "looking", "search", "find",
            "book", "books", "textbook", "textbooks", "copy", "edition", "please", "can"
    );

    private static final Map<String, String> SUBJECT_MAPPINGS = new LinkedHashMap<>();
    static {
        // Computer Science
        addSubjectKeywords("Computer Science", "java", "python", "c++", "javascript", "algorithms",
                "clrs", "data structures", "dbms", "database", "sql", "operating systems",
                "os", "compiler", "networking", "network", "clean code", "programming", "software");
        // Mathematics
        addSubjectKeywords("Mathematics", "calculus", "linear algebra", "discrete math",
                "statistics", "probability", "stewart", "differential equations", "geometry");
        // Electrical Engineering
        addSubjectKeywords("Electrical Engineering", "circuits", "signals", "vlsi",
                "electronics", "microprocessor", "digital logic", "dsp");
        // Physics
        addSubjectKeywords("Physics", "mechanics", "thermodynamics", "quantum",
                "electromagnetism", "optics", "halliday", "resnick");
        // Business & Economics
        addSubjectKeywords("Business", "economics", "finance", "accounting", "marketing",
                "macroeconomics", "microeconomics", "management");
    }

    private static void addSubjectKeywords(String subject, String... keywords) {
        for (String kw : keywords) {
            SUBJECT_MAPPINGS.put(kw.toLowerCase(), subject);
        }
    }

    /**
     * Parses a raw natural language query into a structured SearchQueryIntent.
     */
    public SearchQueryIntent parseQuery(String query) {
        if (query == null || query.isBlank()) {
            return new SearchQueryIntent("", Collections.emptySet(), "", null, null, "ALL", "", null, false);
        }

        String lowerQuery = query.toLowerCase();

        // 1. Detect Price Constraints
        Double maxPrice = null;
        Pattern pricePattern = Pattern.compile("(?:under|below|less than|max|within|cheap(?:er)? than)\\s*\\$?(\\d+(?:\\.\\d{1,2})?)");
        Matcher priceMatcher = pricePattern.matcher(lowerQuery);
        if (priceMatcher.find()) {
            maxPrice = Double.parseDouble(priceMatcher.group(1));
        } else if (lowerQuery.contains("free") || lowerQuery.contains("zero dollar") || lowerQuery.contains("giveaway")) {
            maxPrice = 0.0;
        } else if (lowerQuery.contains("cheap") || lowerQuery.contains("budget")) {
            maxPrice = 25.0;
        } else if (lowerQuery.contains("affordable")) {
            maxPrice = 35.0;
        }

        // 2. Detect Desired Listing Mode
        ListingMode preferredMode = null;
        if (lowerQuery.contains("free") || lowerQuery.contains("donate") || lowerQuery.contains("donation") || lowerQuery.contains("gift")) {
            preferredMode = ListingMode.DONATE;
        } else if (lowerQuery.contains("exchange") || lowerQuery.contains("barter") || lowerQuery.contains("swap") || lowerQuery.contains("trade")) {
            preferredMode = ListingMode.EXCHANGE;
        } else if (lowerQuery.contains("buy") || lowerQuery.contains("purchase") || lowerQuery.contains("sell") || lowerQuery.contains("cheap") || lowerQuery.contains("affordable")) {
            preferredMode = ListingMode.SELL;
        }

        // 3. Detect Difficulty Level Intent
        String level = "ALL";
        if (lowerQuery.contains("beginner") || lowerQuery.contains("intro") || lowerQuery.contains("introduction") ||
            lowerQuery.contains("basics") || lowerQuery.contains("fundamentals") || lowerQuery.contains("learning") || lowerQuery.contains("101")) {
            level = "BEGINNER";
        } else if (lowerQuery.contains("advanced") || lowerQuery.contains("complex") || lowerQuery.contains("deep") || lowerQuery.contains("expert")) {
            level = "ADVANCED";
        }

        // 4. Detect Subject
        String detectedSubject = "";
        for (Map.Entry<String, String> entry : SUBJECT_MAPPINGS.entrySet()) {
            if (lowerQuery.contains(entry.getKey())) {
                detectedSubject = entry.getValue();
                break;
            }
        }

        // 5. Detect Edition
        String editionPref = "";
        Pattern edPattern = Pattern.compile("(\\d+)(?:st|nd|rd|th)?\\s*(?:edition|ed)");
        Matcher edMatcher = edPattern.matcher(lowerQuery);
        if (edMatcher.find()) {
            editionPref = edMatcher.group(1);
        }

        // 6. Detect Condition Preference
        Condition minCondition = null;
        if (lowerQuery.contains("brand new") || lowerQuery.contains("new copy")) {
            minCondition = Condition.NEW;
        } else if (lowerQuery.contains("like new") || lowerQuery.contains("mint")) {
            minCondition = Condition.LIKE_NEW;
        } else if (lowerQuery.contains("good condition") || lowerQuery.contains("clean")) {
            minCondition = Condition.GOOD;
        }

        // 7. Detect Urgency
        boolean urgent = lowerQuery.contains("urgent") || lowerQuery.contains("asap") || lowerQuery.contains("today") || lowerQuery.contains("exam");

        // 8. Extract Filtered Keywords
        Set<String> keywords = Arrays.stream(lowerQuery.split("\\W+"))
                .filter(w -> w.length() > 2)
                .filter(w -> !STOP_WORDS.contains(w))
                .collect(Collectors.toSet());

        return new SearchQueryIntent(query, keywords, detectedSubject, maxPrice, preferredMode, level, editionPref, minCondition, urgent);
    }

    /**
     * Executes the smart search pipeline against the provided book collection.
     */
    public List<ScoredBookMatch> rankMatches(String rawQuery, Collection<Book> catalog) {
        SearchQueryIntent intent = parseQuery(rawQuery);
        List<ScoredBookMatch> results = new ArrayList<>();

        for (Book book : catalog) {
            double score = 0.0;
            List<String> explanations = new ArrayList<>();

            // A. Subject Alignment (Max 30 pts)
            if (!intent.getTargetSubject().isBlank()) {
                if (book.getSubject().equalsIgnoreCase(intent.getTargetSubject())) {
                    score += 30.0;
                    explanations.add("Subject matched '" + book.getSubject() + "' (+30 pts)");
                }
            }

            // B. Keyword & Title Overlap (Max 35 pts)
            String titleLower = book.getTitle().toLowerCase();
            long matchedKwCount = intent.getKeywords().stream()
                    .filter(kw -> titleLower.contains(kw) || book.getTags().contains(kw))
                    .count();
            if (!intent.getKeywords().isEmpty()) {
                double kwScore = Math.min(35.0, (matchedKwCount / (double) Math.min(intent.getKeywords().size(), 3)) * 35.0);
                if (kwScore > 0) {
                    score += kwScore;
                    explanations.add(String.format("Keyword overlap (%d terms matched) (+%.1f pts)", matchedKwCount, kwScore));
                }
            }

            // C. Mode Match (Max 15 pts)
            if (intent.getPreferredMode() != null) {
                if (book.getMode() == intent.getPreferredMode()) {
                    score += 15.0;
                    explanations.add("Exact mode match [" + book.getMode() + "] (+15 pts)");
                }
            }

            // D. Price Constraint Match (Max 15 pts)
            if (intent.getMaxPriceConstraint() != null) {
                if (book.getPrice() <= intent.getMaxPriceConstraint()) {
                    score += 15.0;
                    explanations.add(String.format("Budget satisfied ($%.2f <= $%.2f) (+15 pts)", book.getPrice(), intent.getMaxPriceConstraint()));
                } else {
                    // Penalty if exceeds price constraint
                    score = Math.max(0, score - 15.0);
                    explanations.add(String.format("Exceeds target budget ($%.2f > $%.2f)", book.getPrice(), intent.getMaxPriceConstraint()));
                }
            }

            // E. Difficulty / Beginner Fit (Max 10 pts)
            if ("BEGINNER".equals(intent.getDifficultyLevel())) {
                if (titleLower.contains("intro") || titleLower.contains("beginner") ||
                    titleLower.contains("head first") || titleLower.contains("fundamentals") ||
                    titleLower.contains("learning")) {
                    score += 10.0;
                    explanations.add("Optimized for beginner learners (+10 pts)");
                }
            }

            // F. Condition and Trust Multiplier (Max 5 pts)
            if (book.getCondition() == Condition.NEW || book.getCondition() == Condition.LIKE_NEW) {
                score += 5.0;
                explanations.add("High condition (" + book.getCondition().getLabel() + ") (+5 pts)");
            }

            // Only retain books with positive relevance score
            if (score > 15.0 || (intent.getKeywords().isEmpty() && score > 0)) {
                String explanationStr = String.join(", ", explanations);
                results.add(new ScoredBookMatch(book, score, explanationStr));
            }
        }

        // Sort descending by score
        Collections.sort(results);
        return results;
    }
}
