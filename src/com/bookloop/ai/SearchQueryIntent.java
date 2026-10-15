package com.bookloop.ai;

import com.bookloop.model.Condition;
import com.bookloop.model.ListingMode;

import java.util.Collections;
import java.util.Set;

/**
 * Encapsulates the extracted semantic intent of a student's natural language query.
 */
public class SearchQueryIntent {
    private final String rawQuery;
    private final Set<String> keywords;
    private final String targetSubject;
    private final Double maxPriceConstraint;
    private final ListingMode preferredMode;
    private final String difficultyLevel;
    private final String editionPreference;
    private final Condition minCondition;
    private final boolean urgent;

    public SearchQueryIntent(String rawQuery, Set<String> keywords, String targetSubject,
                             Double maxPriceConstraint, ListingMode preferredMode,
                             String difficultyLevel, String editionPreference,
                             Condition minCondition, boolean urgent) {
        this.rawQuery = rawQuery;
        this.keywords = keywords != null ? keywords : Collections.emptySet();
        this.targetSubject = targetSubject != null ? targetSubject : "";
        this.maxPriceConstraint = maxPriceConstraint;
        this.preferredMode = preferredMode;
        this.difficultyLevel = difficultyLevel != null ? difficultyLevel : "ALL";
        this.editionPreference = editionPreference != null ? editionPreference : "";
        this.minCondition = minCondition;
        this.urgent = urgent;
    }

    public String getRawQuery() { return rawQuery; }
    public Set<String> getKeywords() { return keywords; }
    public String getTargetSubject() { return targetSubject; }
    public Double getMaxPriceConstraint() { return maxPriceConstraint; }
    public ListingMode getPreferredMode() { return preferredMode; }
    public String getDifficultyLevel() { return difficultyLevel; }
    public String getEditionPreference() { return editionPreference; }
    public Condition getMinCondition() { return minCondition; }
    public boolean isUrgent() { return urgent; }

    @Override
    public String toString() {
        return String.format("Intent[Subject='%s', MaxPrice=%s, Mode=%s, Level=%s, Keywords=%s, Urgent=%b]",
                targetSubject,
                maxPriceConstraint != null ? "$" + maxPriceConstraint : "Any",
                preferredMode != null ? preferredMode.name() : "Any",
                difficultyLevel,
                keywords,
                urgent);
    }
}
