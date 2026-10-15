package com.bookloop.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Audit record representing a completed book exchange, sale, or donation.
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String transactionId;
    private final String primaryBookId;
    private final String primaryBookTitle;
    private final String initiatorId;
    private final String initiatorName;
    private final String providerId;
    private final String providerName;
    private final ListingMode mode;
    private final double amountPaid;
    private final int karmaEarned;
    private final double studentSavings;
    private final String reciprocalBookId;
    private final String reciprocalBookTitle;
    private final LocalDateTime timestamp;
    private final String notes;

    public Transaction(String transactionId, String primaryBookId, String primaryBookTitle,
                       String initiatorId, String initiatorName,
                       String providerId, String providerName,
                       ListingMode mode, double amountPaid, int karmaEarned,
                       double studentSavings, String reciprocalBookId,
                       String reciprocalBookTitle, String notes) {
        this.transactionId = transactionId;
        this.primaryBookId = primaryBookId;
        this.primaryBookTitle = primaryBookTitle;
        this.initiatorId = initiatorId;
        this.initiatorName = initiatorName;
        this.providerId = providerId;
        this.providerName = providerName;
        this.mode = mode;
        this.amountPaid = amountPaid;
        this.karmaEarned = karmaEarned;
        this.studentSavings = studentSavings;
        this.reciprocalBookId = reciprocalBookId;
        this.reciprocalBookTitle = reciprocalBookTitle;
        this.timestamp = LocalDateTime.now();
        this.notes = notes;
    }

    public String generateReceipt() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("┌────────────────────────────────────────────────────────┐%n"));
        sb.append(String.format("│            BOOKLOOP AI TRANSACTION RECEIPT             │%n"));
        sb.append(String.format("├────────────────────────────────────────────────────────┤%n"));
        sb.append(String.format("│ Tx ID:      %-42s │%n", transactionId));
        sb.append(String.format("│ Timestamp:  %-42s │%n", timestamp.format(dtf)));
        sb.append(String.format("│ Mode:       %-42s │%n", mode.getDisplayName()));
        sb.append(String.format("│ Book:       %-42s │%n", primaryBookTitle));
        sb.append(String.format("│ Recipient:  %-42s │%n", initiatorName + " (" + initiatorId + ")"));
        sb.append(String.format("│ Provider:   %-42s │%n", providerName + " (" + providerId + ")"));
        if (mode == ListingMode.EXCHANGE) {
            sb.append(String.format("│ Barter For: %-42s │%n", reciprocalBookTitle));
            sb.append(String.format("│ Cash Paid:  $%-41.2f │%n", 0.00));
        } else if (mode == ListingMode.SELL) {
            sb.append(String.format("│ Cash Paid:  $%-41.2f │%n", amountPaid));
            sb.append(String.format("│ Savings:    $%-41.2f │%n", studentSavings));
        } else if (mode == ListingMode.DONATE) {
            sb.append(String.format("│ Cash Paid:  $%-41.2f │%n", 0.00));
            sb.append(String.format("│ Eco-Karma:  +%-41d │%n", karmaEarned));
        }
        sb.append(String.format("│ Notes:      %-42s │%n", notes != null ? notes : "Direct Campus Match"));
        sb.append(String.format("└────────────────────────────────────────────────────────┘"));
        return sb.toString();
    }

    public String getTransactionId() { return transactionId; }
    public String getPrimaryBookId() { return primaryBookId; }
    public String getPrimaryBookTitle() { return primaryBookTitle; }
    public String getInitiatorId() { return initiatorId; }
    public String getInitiatorName() { return initiatorName; }
    public String getProviderId() { return providerId; }
    public String getProviderName() { return providerName; }
    public ListingMode getMode() { return mode; }
    public double getAmountPaid() { return amountPaid; }
    public int getKarmaEarned() { return karmaEarned; }
    public double getStudentSavings() { return studentSavings; }
    public String getReciprocalBookId() { return reciprocalBookId; }
    public String getReciprocalBookTitle() { return reciprocalBookTitle; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getNotes() { return notes; }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s -> %s | %s", transactionId, mode, initiatorName, providerName, primaryBookTitle);
    }
}
