package com.bookloop.model;

/**
 * Textbook physical wear and quality condition.
 */
public enum Condition {
    NEW("Brand New", 1.0, "Pristine, unopened or never marked"),
    LIKE_NEW("Like New", 0.9, "Lightly handled, no highlights, clean pages"),
    GOOD("Good", 0.75, "Clean copy, minimal highlighting, sturdy binding"),
    FAIR("Fair", 0.55, "Noticeable wear, highlights, all pages fully intact"),
    POOR("Well-Loved", 0.35, "Heavy markings or worn cover, completely readable");

    private final String label;
    private final double valueMultiplier;
    private final String notes;

    Condition(String label, double valueMultiplier, String notes) {
        this.label = label;
        this.valueMultiplier = valueMultiplier;
        this.notes = notes;
    }

    public String getLabel() {
        return label;
    }

    public double getValueMultiplier() {
        return valueMultiplier;
    }

    public String getNotes() {
        return notes;
    }

    public static Condition fromString(String text) {
        if (text == null) return null;
        for (Condition cond : values()) {
            if (cond.name().equalsIgnoreCase(text.trim()) || cond.label.equalsIgnoreCase(text.trim())) {
                return cond;
            }
        }
        return null;
    }
}
