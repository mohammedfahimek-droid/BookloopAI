package com.bookloop.service;

import com.bookloop.model.Book;
import com.bookloop.model.ListingMode;
import com.bookloop.model.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for textbook inventory, transactions, and campus metrics.
 */
public interface IBookService {
    void addBook(Book book);
    Optional<Book> findBookById(String id);
    Book getBookById(String id);
    List<Book> getAllBooks();
    List<Book> getAvailableBooks();
    List<Book> getBooksByOwner(String userId);
    List<Book> getBooksByMode(ListingMode mode);
    List<Book> getBooksBySubject(String subject);
    void updateBook(Book book);
    void removeBook(String id);
    List<Book> searchBooks(String query);
    void recordTransaction(Transaction transaction);
    List<Transaction> getAllTransactions();
    double calculateTotalCampusSavings();
    int getTotalBooksReused();
}
