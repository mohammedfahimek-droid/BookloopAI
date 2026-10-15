package com.bookloop.service;

import com.bookloop.model.Book;
import com.bookloop.model.Transaction;

import java.util.List;

/**
 * Service interface for reciprocal barter loop detection and zero-cash matching.
 */
public interface IBarterService {
    List<BarterMatch> findReciprocalMatchesForBook(Book book);
    List<BarterMatch> findAllBarterLoops();
    Transaction executeBarterExchange(String bookAId, String bookBId);
}
