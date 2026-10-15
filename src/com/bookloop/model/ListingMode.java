package com.bookloop.model;

/**
 * Supported ownership and sharing modes on BookLoop AI.
 */
public enum ListingMode {
    SELL("Peer-to-Peer Sale", "Direct affordable purchase between students"),
    DONATE("Campus Eco-Donation", "100% Free book transfer that earns high eco-karma"),
    EXCHANGE("Direct Barter Exchange", "Zero-cash reciprocal book trade"),
    REQUEST("Wanted / In Search Of", "Student wishlist listing seeking a campus match");

    private final String displayName;
    private final String description;

    ListingMode(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public static ListingMode fromString(String text) {
        if (text == null) return null;
        for (ListingMode mode : values()) {
            if (mode.name().equalsIgnoreCase(text.trim()) || mode.displayName.equalsIgnoreCase(text.trim())) {
                return mode;
            }
        }
        return null;
    }
}
