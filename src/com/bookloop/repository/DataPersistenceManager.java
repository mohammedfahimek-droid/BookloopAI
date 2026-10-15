package com.bookloop.repository;

import com.bookloop.model.Book;
import com.bookloop.model.Transaction;
import com.bookloop.model.User;
import com.bookloop.service.IBookService;
import com.bookloop.service.IUserService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages serialization and deserialization of the BookLoop state to disk.
 */
public class DataPersistenceManager {
    private static final String DATA_DIR = ".bookloop_data";
    private static final String STATE_FILE = DATA_DIR + "/bookloop_state.dat";

    public static synchronized boolean saveState(IUserService userService, IBookService bookService) {
        try {
            Path dir = Paths.get(DATA_DIR);
            if (!Files.exists(dir)) {
                Files.createDirectories(dir);
            }

            StateSnapshot snapshot = new StateSnapshot(
                    new ArrayList<>(userService.getAllUsers()),
                    new ArrayList<>(bookService.getAllBooks()),
                    new ArrayList<>(bookService.getAllTransactions()),
                    userService.getActiveUser() != null ? userService.getActiveUser().getId() : null
            );

            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STATE_FILE))) {
                oos.writeObject(snapshot);
                return true;
            }
        } catch (IOException e) {
            System.err.println("Failed to persist BookLoop state: " + e.getMessage());
            return false;
        }
    }

    public static synchronized boolean loadState(IUserService userService, IBookService bookService) {
        File file = new File(STATE_FILE);
        if (!file.exists()) {
            return false;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            StateSnapshot snapshot = (StateSnapshot) ois.readObject();

            for (User u : snapshot.users) {
                userService.registerUser(u);
            }
            if (snapshot.activeUserId != null) {
                userService.findUserById(snapshot.activeUserId).ifPresent(userService::setActiveUser);
            }
            for (Book b : snapshot.books) {
                bookService.addBook(b);
            }
            for (Transaction tx : snapshot.transactions) {
                bookService.recordTransaction(tx);
            }

            return true;
        } catch (Exception e) {
            System.err.println("Warning: Could not read existing snapshot (" + e.getMessage() + "). Seeding demo data instead.");
            return false;
        }
    }

    private static class StateSnapshot implements Serializable {
        private static final long serialVersionUID = 1L;

        final List<User> users;
        final List<Book> books;
        final List<Transaction> transactions;
        final String activeUserId;

        StateSnapshot(List<User> users, List<Book> books, List<Transaction> transactions, String activeUserId) {
            this.users = users;
            this.books = books;
            this.transactions = transactions;
            this.activeUserId = activeUserId;
        }
    }
}
