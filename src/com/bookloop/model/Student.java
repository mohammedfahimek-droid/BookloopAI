package com.bookloop.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Concrete Student entity inheriting from User.
 * Demonstrates Inheritance and Polymorphism.
 */
public class Student extends User {
    private static final long serialVersionUID = 1L;

    private int semester;
    private int graduationYear;
    private final List<String> wishlist;
    private boolean studentCardVerified;

    public Student(String id, String name, String email, String campusId, String department, int semester, int graduationYear) {
        super(id, name, email, campusId, department);
        this.semester = semester;
        this.graduationYear = graduationYear;
        this.wishlist = new ArrayList<>();
        this.studentCardVerified = true;
    }

    @Override
    public String getRoleName() {
        return "Undergraduate Student";
    }

    @Override
    public double calculateTrustScore() {
        // Trust score calculated from verified status, books shared, and eco-karma points
        double base = studentCardVerified ? 3.0 : 1.0;
        double karmaFactor = Math.min(2.0, getKarmaPoints() / 150.0);
        double shareFactor = Math.min(2.0, getBooksSharedCount() * 0.4);
        return Math.min(5.0, base + karmaFactor + shareFactor);
    }

    @Override
    public String getFormattedProfile() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("╔══════════════════════════════════════════════════════════════╗%n"));
        sb.append(String.format("║  STUDENT PROFILE: %-42s ║%n", getName()));
        sb.append(String.format("╠══════════════════════════════════════════════════════════════╣%n"));
        sb.append(String.format("║  Campus ID: %-15s Department: %-23s ║%n", getCampusId(), getDepartment()));
        sb.append(String.format("║  Semester:  %-15d Grad Year:  %-23d ║%n", semester, graduationYear));
        sb.append(String.format("║  Eco-Karma: %-15s Trust Score: %-22.1f★║%n", getKarmaPoints() + " pts", calculateTrustScore()));
        sb.append(String.format("║  Savings:   $%-14.2f Books Shared: %-20d ║%n", getTotalSavings(), getBooksSharedCount()));
        sb.append(String.format("║  Wishlist (%d items):                                         ║%n", wishlist.size()));
        if (wishlist.isEmpty()) {
            sb.append(String.format("║    (No active book requests yet)                             ║%n"));
        } else {
            for (String item : wishlist) {
                sb.append(String.format("║    • %-56s ║%n", item.length() > 56 ? item.substring(0, 53) + "..." : item));
            }
        }
        sb.append(String.format("╚══════════════════════════════════════════════════════════════╝"));
        return sb.toString();
    }

    public void addToWishlist(String bookTitleOrSubject) {
        if (bookTitleOrSubject != null && !bookTitleOrSubject.isBlank()) {
            String trimmed = bookTitleOrSubject.trim();
            if (!wishlist.contains(trimmed)) {
                wishlist.add(trimmed);
            }
        }
    }

    public boolean removeFromWishlist(String item) {
        return wishlist.removeIf(w -> w.equalsIgnoreCase(item.trim()));
    }

    public List<String> getWishlist() {
        return Collections.unmodifiableList(wishlist);
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getGradYear() {
        return graduationYear;
    }

    public void setGradYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    public boolean isStudentCardVerified() {
        return studentCardVerified;
    }

    public void setStudentCardVerified(boolean studentCardVerified) {
        this.studentCardVerified = studentCardVerified;
    }
}
