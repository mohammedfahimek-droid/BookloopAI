package com.bookloop.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Core Book listing entity on the BookLoop platform.
 * Supports SELL, DONATE, EXCHANGE, and REQUEST modes.
 */
public class Book implements Serializable, Comparable<Book> {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String title;
    private String author;
    private String edition;
    private String subject;
    private Condition condition;
    private ListingMode mode;
    private double price;
    private double originalRetailPrice;
    private User owner;
    private String neededSubject;
    private String neededBookTitle;
    private final Set<String> tags;
    private BookStatus status;
    private final LocalDateTime listedAt;

    public Book(String id, String title, String author, String edition, String subject,
                Condition condition, ListingMode mode, double price, double originalRetailPrice,
                User owner, String neededSubject, String neededBookTitle) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Book ID cannot be blank");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Book title cannot be blank");
        if (subject == null || subject.isBlank()) throw new IllegalArgumentException("Subject cannot be blank");
        if (condition == null) throw new IllegalArgumentException("Condition cannot be null");
        if (mode == null) throw new IllegalArgumentException("Listing mode cannot be null");

        this.id = id;
        this.title = title.trim();
        this.author = (author != null && !author.isBlank()) ? author.trim() : "Unknown Author";
        this.edition = (edition != null && !edition.isBlank()) ? edition.trim() : "Standard";
        this.subject = subject.trim();
        this.condition = condition;
        this.mode = mode;
        this.price = (mode == ListingMode.DONATE || mode == ListingMode.REQUEST) ? 0.0 : Math.max(0.0, price);
        this.originalRetailPrice = Math.max(this.price, originalRetailPrice);
        this.owner = owner;
        this.neededSubject = (neededSubject != null) ? neededSubject.trim() : "";
        this.neededBookTitle = (neededBookTitle != null) ? neededBookTitle.trim() : "";
        this.tags = new HashSet<>();
        this.status = BookStatus.AVAILABLE;
        this.listedAt = LocalDateTime.now();

        populateDefaultTags();
    }

    private void populateDefaultTags() {
        for (String word : title.toLowerCase().split("\\W+")) {
            if (word.length() > 2) tags.add(word);
        }
        for (String word : subject.toLowerCase().split("\\W+")) {
            if (word.length() > 2) tags.add(word);
        }
        tags.add(mode.name().toLowerCase());
        tags.add(condition.name().toLowerCase());
    }

    public void addTag(String tag) {
        if (tag != null && !tag.isBlank()) {
            tags.add(tag.trim().toLowerCase());
        }
    }

    public double calculateSavings() {
        if (originalRetailPrice <= 0) return 0.0;
        return Math.max(0.0, originalRetailPrice - price);
    }

    public double getDiscountPercentage() {
        if (originalRetailPrice <= 0 || price >= originalRetailPrice) return 0.0;
        return ((originalRetailPrice - price) / originalRetailPrice) * 100.0;
    }

    public String getFormattedSummary() {
        String modeBadge = switch (mode) {
            case SELL -> String.format("[SELL - $%.2f (Save $%.2f)]", price, calculateSavings());
            case DONATE -> "[DONATE - 100% Free Eco-Gift]";
            case EXCHANGE -> String.format("[EXCHANGE - Wants: %s]",
                    !neededBookTitle.isBlank() ? neededBookTitle : (!neededSubject.isBlank() ? neededSubject : "Any"));
            case REQUEST -> "[REQUEST - In Search Of]";
        };

        return String.format("[%s] %s by %s (%s, %s) - %s | Listed by: %s",
                id, title, author, edition, condition.getLabel(), modeBadge,
                owner != null ? owner.getName() : "Anonymous");
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getEdition() { return edition; }
    public void setEdition(String edition) { this.edition = edition; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public Condition getCondition() { return condition; }
    public void setCondition(Condition condition) { this.condition = condition; }
    public ListingMode getMode() { return mode; }
    public void setMode(ListingMode mode) { this.mode = mode; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public double getOriginalRetailPrice() { return originalRetailPrice; }
    public void setOriginalRetailPrice(double originalRetailPrice) { this.originalRetailPrice = originalRetailPrice; }
    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
    public String getNeededSubject() { return neededSubject; }
    public void setNeededSubject(String neededSubject) { this.neededSubject = neededSubject; }
    public String getNeededBookTitle() { return neededBookTitle; }
    public void setNeededBookTitle(String neededBookTitle) { this.neededBookTitle = neededBookTitle; }
    public Set<String> getTags() { return new HashSet<>(tags); }
    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }
    public LocalDateTime getListedAt() { return listedAt; }

    @Override
    public int compareTo(Book other) {
        return this.title.compareToIgnoreCase(other.title);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getFormattedSummary();
    }
}
