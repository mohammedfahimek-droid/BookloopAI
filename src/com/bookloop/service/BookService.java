package com.bookloop.service;

import com.bookloop.exception.BookNotFoundException;
import com.bookloop.exception.DuplicateListingException;
import com.bookloop.model.Book;
import com.bookloop.model.BookStatus;
import com.bookloop.model.ListingMode;
import com.bookloop.model.Transaction;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Concrete BookService managing textbook inventory and campus circulation metrics.
 */
public class BookService implements IBookService, Serializable {
    private static final long serialVersionUID = 1L;

    private final Map<String, Book> catalog;
    private final List<Transaction> transactionHistory;

    public BookService() {
        this.catalog = new ConcurrentHashMap<>();
        this.transactionHistory = new CopyOnWriteArrayList<>();
    }

    @Override
    public void addBook(Book book) {
        if (book == null) throw new IllegalArgumentException("Book cannot be null");

        // Check for duplicate active listing by same user
        boolean exists = catalog.values().stream()
                .anyMatch(b -> b.getOwner() != null &&
                               b.getOwner().equals(book.getOwner()) &&
                               b.getTitle().equalsIgnoreCase(book.getTitle()) &&
                               b.getStatus() == BookStatus.AVAILABLE);
        if (exists) {
            throw new DuplicateListingException("An active listing for '" + book.getTitle() + "' already exists for this student.");
        }

        catalog.put(book.getId(), book);
    }

    @Override
    public Optional<Book> findBookById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(catalog.get(id));
    }

    @Override
    public Book getBookById(String id) {
        return findBookById(id).orElseThrow(() -> BookNotFoundException.forId(id));
    }

    @Override
    public List<Book> getAllBooks() {
        return new ArrayList<>(catalog.values());
    }

    @Override
    public List<Book> getAvailableBooks() {
        return catalog.values().stream()
                .filter(b -> b.getStatus() == BookStatus.AVAILABLE)
                .sorted()
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getBooksByOwner(String userId) {
        return catalog.values().stream()
                .filter(b -> b.getOwner() != null && b.getOwner().getId().equalsIgnoreCase(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getBooksByMode(ListingMode mode) {
        return catalog.values().stream()
                .filter(b -> b.getStatus() == BookStatus.AVAILABLE && b.getMode() == mode)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getBooksBySubject(String subject) {
        return catalog.values().stream()
                .filter(b -> b.getStatus() == BookStatus.AVAILABLE && b.getSubject().equalsIgnoreCase(subject))
                .collect(Collectors.toList());
    }

    @Override
    public void updateBook(Book book) {
        if (book != null && catalog.containsKey(book.getId())) {
            catalog.put(book.getId(), book);
        }
    }

    @Override
    public void removeBook(String id) {
        catalog.remove(id);
    }

    @Override
    public List<Book> searchBooks(String query) {
        if (query == null || query.isBlank()) return getAvailableBooks();
        String q = query.toLowerCase().trim();
        return catalog.values().stream()
                .filter(b -> b.getStatus() == BookStatus.AVAILABLE)
                .filter(b -> b.getTitle().toLowerCase().contains(q) ||
                             b.getAuthor().toLowerCase().contains(q) ||
                             b.getSubject().toLowerCase().contains(q) ||
                             b.getTags().stream().anyMatch(t -> t.contains(q)))
                .collect(Collectors.toList());
    }

    @Override
    public void recordTransaction(Transaction transaction) {
        if (transaction != null) {
            transactionHistory.add(transaction);
        }
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return Collections.unmodifiableList(transactionHistory);
    }

    @Override
    public double calculateTotalCampusSavings() {
        return transactionHistory.stream()
                .mapToDouble(Transaction::getStudentSavings)
                .sum();
    }

    @Override
    public int getTotalBooksReused() {
        return transactionHistory.size();
    }
}
