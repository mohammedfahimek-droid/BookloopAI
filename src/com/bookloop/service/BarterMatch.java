package com.bookloop.service;

import com.bookloop.model.Book;
import com.bookloop.model.User;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Represents a detected reciprocal 2-way barter loop between two campus students.
 */
public class BarterMatch implements Serializable {
    private static final long serialVersionUID = 1L;

    private final User studentA;
    private final Book bookA;
    private final User studentB;
    private final Book bookB;
    private final String reasonA; // Why bookB satisfies studentA
    private final String reasonB; // Why bookA satisfies studentB
    private final double matchScore;
    private final LocalDateTime detectedAt;

    public BarterMatch(User studentA, Book bookA, User studentB, Book bookB,
                       String reasonA, String reasonB, double matchScore) {
        this.studentA = studentA;
        this.bookA = bookA;
        this.studentB = studentB;
        this.bookB = bookB;
        this.reasonA = reasonA;
        this.reasonB = reasonB;
        this.matchScore = matchScore;
        this.detectedAt = LocalDateTime.now();
    }

    public String getFormattedAlert() {
        StringBuilder sb = new StringBuilder();
        sb.append("╔══════════════════════════════════════════════════════════════════════════════╗\n");
        sb.append("║                   ⚡ INSTANT ZERO-CASH BARTER LOOP ALERT! ⚡                 ║\n");
        sb.append("╠══════════════════════════════════════════════════════════════════════════════╣\n");
        sb.append(String.format("║  Match Confidence: %-5.1f%%                                                  ║\n", matchScore));
        sb.append("╟──────────────────────────────────────────────────────────────────────────────╢\n");
        sb.append(String.format("║  👤 Student A: %-60s ║\n", studentA.getName() + " (" + studentA.getCampusId() + ")"));
        sb.append(String.format("║     • HAS:   [%s] %-50s ║\n", bookA.getId(), truncate(bookA.getTitle(), 50)));
        sb.append(String.format("║     • WANTS: %-61s ║\n", truncate(bookA.getNeededBookTitle().isBlank() ? bookA.getNeededSubject() : bookA.getNeededBookTitle(), 61)));
        sb.append("╟──────────────────────────────────────────────────────────────────────────────╢\n");
        sb.append(String.format("║  👤 Student B: %-60s ║\n", studentB.getName() + " (" + studentB.getCampusId() + ")"));
        sb.append(String.format("║     • HAS:   [%s] %-50s ║\n", bookB.getId(), truncate(bookB.getTitle(), 50)));
        sb.append(String.format("║     • WANTS: %-61s ║\n", truncate(bookB.getNeededBookTitle().isBlank() ? bookB.getNeededSubject() : bookB.getNeededBookTitle(), 61)));
        sb.append("╟──────────────────────────────────────────────────────────────────────────────╢\n");
        sb.append(String.format("║  🤝 Reciprocal Trade Summary:                                                ║\n"));
        sb.append(String.format("║     • %-70s ║\n", truncate(studentA.getName() + " gets " + bookB.getTitle(), 70)));
        sb.append(String.format("║     • %-70s ║\n", truncate(studentB.getName() + " gets " + bookA.getTitle(), 70)));
        sb.append(String.format("║  💰 Cash Exchanged: $0.00 | 🌱 Eco-Karma Earned: +60 pts each                 ║\n"));
        sb.append("╚══════════════════════════════════════════════════════════════════════════════╝");
        return sb.toString();
    }

    private String truncate(String text, int maxLen) {
        if (text == null) return "";
        return text.length() > maxLen ? text.substring(0, maxLen - 3) + "..." : text;
    }

    public User getStudentA() { return studentA; }
    public Book getBookA() { return bookA; }
    public User getStudentB() { return studentB; }
    public Book getBookB() { return bookB; }
    public String getReasonA() { return reasonA; }
    public String getReasonB() { return reasonB; }
    public double getMatchScore() { return matchScore; }
    public LocalDateTime getDetectedAt() { return detectedAt; }
}
