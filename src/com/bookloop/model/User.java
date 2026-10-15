package com.bookloop.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Abstract base class representing a campus community member on BookLoop AI.
 * Demonstrates Abstraction and Encapsulation.
 */
public abstract class User implements Serializable, Comparable<User> {
    private static final long serialVersionUID = 1L;

    private final String id;
    private String name;
    private String email;
    private String campusId;
    private String department;
    private int karmaPoints;
    private double totalSavings;
    private int booksSharedCount;
    private final LocalDateTime registeredAt;

    protected User(String id, String name, String email, String campusId, String department) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("User ID cannot be blank");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("User name cannot be blank");
        this.id = id;
        this.name = name;
        this.email = email;
        this.campusId = campusId;
        this.department = department;
        this.karmaPoints = 50; // Welcome karma boost
        this.totalSavings = 0.0;
        this.booksSharedCount = 0;
        this.registeredAt = LocalDateTime.now();
    }

    // Abstract methods demonstrating Polymorphism and Abstraction
    public abstract String getRoleName();
    public abstract String getFormattedProfile();
    public abstract double calculateTrustScore();

    // Concrete domain operations
    public void addKarma(int points) {
        if (points > 0) {
            this.karmaPoints += points;
        }
    }

    public void deductKarma(int points) {
        this.karmaPoints = Math.max(0, this.karmaPoints - points);
    }

    public void recordSavings(double amount) {
        if (amount > 0) {
            this.totalSavings += amount;
        }
    }

    public void incrementBooksShared() {
        this.booksSharedCount++;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCampusId() {
        return campusId;
    }

    public void setCampusId(String campusId) {
        this.campusId = campusId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getKarmaPoints() {
        return karmaPoints;
    }

    public double getTotalSavings() {
        return totalSavings;
    }

    public int getBooksSharedCount() {
        return booksSharedCount;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    @Override
    public int compareTo(User other) {
        // Higher karma ranks higher on leaderboard
        return Integer.compare(other.karmaPoints, this.karmaPoints);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s | Karma: %d pts", name, campusId, department, karmaPoints);
    }
}
