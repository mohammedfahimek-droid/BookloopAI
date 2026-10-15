package com.bookloop.service;

import com.bookloop.exception.BarterMismatchException;
import com.bookloop.exception.InvalidModeException;
import com.bookloop.model.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implements automated barter loop detection and reciprocal trade execution.
 */
public class BarterService implements IBarterService, Serializable {
    private static final long serialVersionUID = 1L;

    private final IBookService bookService;
    private final IUserService userService;

    public BarterService(IBookService bookService, IUserService userService) {
        this.bookService = bookService;
        this.userService = userService;
    }

    @Override
    public List<BarterMatch> findReciprocalMatchesForBook(Book bookA) {
        if (bookA == null || bookA.getMode() != ListingMode.EXCHANGE || bookA.getStatus() != BookStatus.AVAILABLE) {
            return Collections.emptyList();
        }

        List<BarterMatch> matches = new ArrayList<>();
        List<Book> exchangeCandidates = bookService.getBooksByMode(ListingMode.EXCHANGE);

        for (Book bookB : exchangeCandidates) {
            if (bookB.getId().equals(bookA.getId())) continue;
            if (bookB.getStatus() != BookStatus.AVAILABLE) continue;

            // Cannot trade with oneself
            if (bookA.getOwner() != null && bookB.getOwner() != null &&
                bookA.getOwner().getId().equalsIgnoreCase(bookB.getOwner().getId())) {
                continue;
            }

            // Check if Book B satisfies Student A
            MatchCriterion satisfiedA = checkSatisfaction(bookA, bookB);

            // Check if Book A satisfies Student B
            MatchCriterion satisfiedB = checkSatisfaction(bookB, bookA);

            if (satisfiedA.isMatch() && satisfiedB.isMatch()) {
                double confidence = calculateConfidence(satisfiedA, satisfiedB);
                matches.add(new BarterMatch(
                        bookA.getOwner(), bookA,
                        bookB.getOwner(), bookB,
                        satisfiedA.getReason(),
                        satisfiedB.getReason(),
                        confidence
                ));
            }
        }

        return matches;
    }

    @Override
    public List<BarterMatch> findAllBarterLoops() {
        List<Book> exchangeBooks = bookService.getBooksByMode(ListingMode.EXCHANGE).stream()
                .filter(b -> b.getStatus() == BookStatus.AVAILABLE)
                .collect(Collectors.toList());

        List<BarterMatch> allLoops = new ArrayList<>();
        Set<String> visitedPairs = new HashSet<>();

        for (int i = 0; i < exchangeBooks.size(); i++) {
            Book bookA = exchangeBooks.get(i);
            for (int j = i + 1; j < exchangeBooks.size(); j++) {
                Book bookB = exchangeBooks.get(j);

                if (bookA.getOwner() != null && bookB.getOwner() != null &&
                    bookA.getOwner().getId().equalsIgnoreCase(bookB.getOwner().getId())) {
                    continue;
                }

                String pairKey = bookA.getId().compareTo(bookB.getId()) < 0
                        ? bookA.getId() + "<->" + bookB.getId()
                        : bookB.getId() + "<->" + bookA.getId();

                if (visitedPairs.contains(pairKey)) continue;

                MatchCriterion satisfiedA = checkSatisfaction(bookA, bookB);
                MatchCriterion satisfiedB = checkSatisfaction(bookB, bookA);

                if (satisfiedA.isMatch() && satisfiedB.isMatch()) {
                    visitedPairs.add(pairKey);
                    double confidence = calculateConfidence(satisfiedA, satisfiedB);
                    allLoops.add(new BarterMatch(
                            bookA.getOwner(), bookA,
                            bookB.getOwner(), bookB,
                            satisfiedA.getReason(),
                            satisfiedB.getReason(),
                            confidence
                    ));
                }
            }
        }

        allLoops.sort((m1, m2) -> Double.compare(m2.getMatchScore(), m1.getMatchScore()));
        return allLoops;
    }

    @Override
    public Transaction executeBarterExchange(String bookAId, String bookBId) {
        Book bookA = bookService.getBookById(bookAId);
        Book bookB = bookService.getBookById(bookBId);

        if (bookA.getStatus() != BookStatus.AVAILABLE || bookB.getStatus() != BookStatus.AVAILABLE) {
            throw new BarterMismatchException("One or both books are no longer available for trade.");
        }

        if (bookA.getMode() != ListingMode.EXCHANGE || bookB.getMode() != ListingMode.EXCHANGE) {
            throw new InvalidModeException("Both books must be listed in EXCHANGE mode to execute a barter trade.");
        }

        if (bookA.getOwner().getId().equalsIgnoreCase(bookB.getOwner().getId())) {
            throw new BarterMismatchException("A student cannot execute a barter exchange with their own books.");
        }

        // Complete the trade
        bookA.setStatus(BookStatus.COMPLETED);
        bookB.setStatus(BookStatus.COMPLETED);

        // Update books shared and award eco-karma to both participants
        int karmaReward = 60; // 60 karma points for zero-cash circular trade
        bookA.getOwner().addKarma(karmaReward);
        bookA.getOwner().incrementBooksShared();

        bookB.getOwner().addKarma(karmaReward);
        bookB.getOwner().incrementBooksShared();

        // Calculate campus savings (sum of original textbook prices avoided)
        double totalSavings = bookA.getOriginalRetailPrice() + bookB.getOriginalRetailPrice();
        bookA.getOwner().recordSavings(bookB.getOriginalRetailPrice());
        bookB.getOwner().recordSavings(bookA.getOriginalRetailPrice());

        // Create Transaction record
        String txId = "TX-BARTER-" + System.currentTimeMillis();
        Transaction transaction = new Transaction(
                txId,
                bookA.getId(),
                bookA.getTitle(),
                bookA.getOwner().getId(),
                bookA.getOwner().getName(),
                bookB.getOwner().getId(),
                bookB.getOwner().getName(),
                ListingMode.EXCHANGE,
                0.00,
                karmaReward,
                totalSavings,
                bookB.getId(),
                bookB.getTitle(),
                "Zero-cash reciprocal barter loop executed successfully."
        );

        bookService.recordTransaction(transaction);
        return transaction;
    }

    private MatchCriterion checkSatisfaction(Book requesterBook, Book offeredBook) {
        String neededTitle = requesterBook.getNeededBookTitle().toLowerCase().trim();
        String neededSubject = requesterBook.getNeededSubject().toLowerCase().trim();
        String offeredTitle = offeredBook.getTitle().toLowerCase().trim();
        String offeredSubject = offeredBook.getSubject().toLowerCase().trim();

        // 1. Direct Title Match or Keyword Containment
        if (!neededTitle.isBlank()) {
            if (offeredTitle.contains(neededTitle) || neededTitle.contains(offeredTitle)) {
                return new MatchCriterion(true, 1.0, "Direct book match: '" + offeredBook.getTitle() + "' matches requested '" + requesterBook.getNeededBookTitle() + "'");
            }
            // Check word overlap
            Set<String> neededWords = new HashSet<>(Arrays.asList(neededTitle.split("\\W+")));
            neededWords.removeAll(Set.of("the", "a", "an", "for", "and", "edition", "book"));
            long overlap = neededWords.stream().filter(offeredTitle::contains).count();
            if (overlap > 0 && neededWords.size() > 0 && (overlap / (double) neededWords.size()) >= 0.5) {
                return new MatchCriterion(true, 0.9, "Strong keyword alignment with requested title '" + requesterBook.getNeededBookTitle() + "'");
            }
        }

        // 2. Subject Match
        if (!neededSubject.isBlank()) {
            if (offeredSubject.contains(neededSubject) || neededSubject.contains(offeredSubject)) {
                return new MatchCriterion(true, 0.8, "Subject match: '" + offeredBook.getSubject() + "' fulfills needed '" + requesterBook.getNeededSubject() + "'");
            }
        }

        // 3. Wishlist Match of Student
        if (requesterBook.getOwner() instanceof Student student) {
            for (String wish : student.getWishlist()) {
                String w = wish.toLowerCase();
                if (offeredTitle.contains(w) || offeredSubject.contains(w) || w.contains(offeredTitle)) {
                    return new MatchCriterion(true, 0.85, "Matches student wishlist item: '" + wish + "'");
                }
            }
        }

        return new MatchCriterion(false, 0.0, "No reciprocal match criteria satisfied");
    }

    private double calculateConfidence(MatchCriterion a, MatchCriterion b) {
        double combined = ((a.getWeight() + b.getWeight()) / 2.0) * 100.0;
        return Math.min(99.0, Math.max(70.0, combined));
    }

    private static class MatchCriterion {
        private final boolean match;
        private final double weight;
        private final String reason;

        public MatchCriterion(boolean match, double weight, String reason) {
            this.match = match;
            this.weight = weight;
            this.reason = reason;
        }

        public boolean isMatch() { return match; }
        public double getWeight() { return weight; }
        public String getReason() { return reason; }
    }
}
