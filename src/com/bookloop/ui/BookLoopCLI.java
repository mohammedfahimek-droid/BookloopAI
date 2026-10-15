package com.bookloop.ui;

import com.bookloop.ai.ScoredBookMatch;
import com.bookloop.ai.SearchQueryIntent;
import com.bookloop.exception.BookLoopException;
import com.bookloop.model.*;
import com.bookloop.repository.DataPersistenceManager;
import com.bookloop.repository.MockDataSeeder;
import com.bookloop.service.BarterMatch;
import com.bookloop.service.IBarterService;
import com.bookloop.service.IBookService;
import com.bookloop.service.IMatchingEngine;
import com.bookloop.service.IUserService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static com.bookloop.ui.ConsoleTheme.*;

/**
 * Interactive Console / Terminal User Interface for BookLoop AI.
 */
public class BookLoopCLI {
    private final IUserService userService;
    private final IBookService bookService;
    private final IMatchingEngine matchingEngine;
    private final IBarterService barterService;
    private final Scanner scanner;
    private boolean running = true;

    public BookLoopCLI(IUserService userService, IBookService bookService,
                       IMatchingEngine matchingEngine, IBarterService barterService) {
        this.userService = userService;
        this.bookService = bookService;
        this.matchingEngine = matchingEngine;
        this.barterService = barterService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        while (running) {
            displayDashboardHeader();
            displayMenu();
            System.out.print(cyan("Select an option (0-9): "));
            String input = scanner.nextLine().trim();

            System.out.println();
            try {
                switch (input) {
                    case "1" -> handleSmartSearch();
                    case "2" -> handleBarterLoopDetection();
                    case "3" -> handleBrowseCatalog();
                    case "4" -> handleListBook();
                    case "5" -> handleExecuteTransaction();
                    case "6" -> handleStudentDashboard();
                    case "7" -> handleLeaderboard();
                    case "8" -> handleSwitchUser();
                    case "9" -> handlePersistence();
                    case "0" -> {
                        System.out.println(green("🌿 Thank you for participating in BookLoop AI — Empowering a Circular Campus Economy!"));
                        running = false;
                    }
                    default -> System.out.println(red("Invalid selection. Please choose an option from 0 to 9."));
                }
            } catch (BookLoopException e) {
                System.out.println(red("⚠️ Operation Error: " + e.getMessage()));
            } catch (Exception e) {
                System.out.println(red("⚠️ Unexpected error: " + e.getMessage()));
            }

            if (running) {
                System.out.println();
                System.out.print(DIM + "Press [Enter] to return to the main menu..." + RESET);
                scanner.nextLine();
            }
        }
    }

    private void displayDashboardHeader() {
        User activeUser = userService.getActiveUser();
        double totalSavings = bookService.calculateTotalCampusSavings();
        int booksReused = bookService.getTotalBooksReused();
        double co2AvoidedKg = booksReused * 2.5; // ~2.5kg CO2 avoided per manufactured textbook reused

        System.out.println(CYAN + """
        ╔══════════════════════════════════════════════════════════════════════════════╗
        ║       ____              _     _                            _    ___          ║
        ║      |  _ \\            | |   | |                          / \\  |_ _|         ║
        ║      | |_) | ___   ___ | | __| |     ___   ___  _ __     / _ \\  | |          ║
        ║      |  _ < / _ \\ / _ \\| |/ /| |    / _ \\ / _ \\| '_ \\   / ___ \\ | |          ║
        ║      | |_) | (_) | (_) |   < | |___| (_) | (_) | |_) | / /   \\ \\| |          ║
        ║      |____/ \\___/ \\___/|_|\\_\\|______\\___/ \\___/| .__/ /_/     \\_\\___|         ║
        ║                                                |_|                           ║
        ║           Campus Textbook Reuse, Sharing & AI Barter Matching               ║
        ╚══════════════════════════════════════════════════════════════════════════════╝""" + RESET);

        System.out.println(yellow("🌱 CAMPUS PILOT IMPACT: ") +
                bold(String.format("Books Reused: %d", booksReused)) + " | " +
                green(String.format("Student Savings: $%.2f", totalSavings)) + " | " +
                cyan(String.format("CO2 Offset: ~%.1f kg", co2AvoidedKg)));

        if (activeUser != null) {
            System.out.println(purple("👤 ACTIVE SESSION: ") +
                    bold(activeUser.getName()) + " (" + activeUser.getCampusId() + ") - " + activeUser.getDepartment() +
                    " | Eco-Karma: " + green(activeUser.getKarmaPoints() + " pts") +
                    " | Trust: " + yellow(String.format("%.1f★", activeUser.calculateTrustScore())));
        }
        printDivider();
    }

    private void displayMenu() {
        System.out.println(bold("MAIN NAVIGATION:"));
        System.out.println("  [" + cyan("1") + "] 🔍 AI Natural Language Smart Search  (e.g., 'affordable beginner book for Java')");
        System.out.println("  [" + cyan("2") + "] 🔄 Automated Barter Loop Detection   (Instant 2-way reciprocal trade detector)");
        System.out.println("  [" + cyan("3") + "] 📚 Browse & Filter Textbook Catalog   (Sell, Donate, Exchange, by Subject)");
        System.out.println("  [" + cyan("4") + "] ➕ List a Textbook for Students       (Peer Sale, Eco-Donation, or Barter)");
        System.out.println("  [" + cyan("5") + "] 🤝 Execute Book Transaction          (Purchase, Claim Donation, or Swap)");
        System.out.println("  [" + cyan("6") + "] 👤 Student Profile & Wishlist         (Karma, Savings & Desired Books)");
        System.out.println("  [" + cyan("7") + "] 🏆 Sustainability Leaderboard         (Top Campus Eco-Karma Champions)");
        System.out.println("  [" + cyan("8") + "] 👥 Switch Active Student Persona      (Simulate reciprocal peer trading)");
        System.out.println("  [" + cyan("9") + "] 💾 Save & Reload Platform State       (Disk Serialization & Snapshots)");
        System.out.println("  [" + cyan("0") + "] 🚪 Exit BookLoop AI");
        printThinDivider();
    }

    private void handleSmartSearch() {
        System.out.println(bold("=== 🔍 AI SMART SEARCH & SEMANTIC INTENT PARSER ==="));
        System.out.println(DIM + "Try natural queries like:" + RESET);
        System.out.println(DIM + "  • 'affordable beginner book for learning Java'" + RESET);
        System.out.println(DIM + "  • 'free algorithms textbook in clean condition'" + RESET);
        System.out.println(DIM + "  • 'cheap calculus under 25 dollars'" + RESET);
        System.out.println(DIM + "  • 'exchange operating systems for networking'" + RESET);
        System.out.println();
        System.out.print(cyan("Enter your search prompt: "));
        String query = scanner.nextLine().trim();

        if (query.isBlank()) {
            System.out.println(yellow("Empty query. Displaying all available books instead."));
            query = "all";
        }

        // 1. Show Intent Analysis
        SearchQueryIntent intent = matchingEngine.analyzeQuery(query);
        System.out.println("\n" + purple("🧠 AI INTENT PARSER EXTRACTION:"));
        System.out.println("  • Target Subject:     " + (intent.getTargetSubject().isBlank() ? "(Generic / Broad)" : green(intent.getTargetSubject())));
        System.out.println("  • Target Budget:      " + (intent.getMaxPriceConstraint() != null ? green("$" + intent.getMaxPriceConstraint()) : "(No price ceiling)"));
        System.out.println("  • Desired Mode:       " + (intent.getPreferredMode() != null ? green(intent.getPreferredMode().name()) : "(Any mode)"));
        System.out.println("  • Difficulty Level:   " + (intent.getDifficultyLevel().equals("ALL") ? "(All Levels)" : green(intent.getDifficultyLevel())));
        System.out.println("  • Filtered Keywords:  " + cyan(intent.getKeywords().toString()));

        // 2. Perform Ranking
        List<ScoredBookMatch> matches = matchingEngine.findSmartRecommendations(query);

        System.out.println("\n" + bold(String.format("MATCH RECOMMENDATIONS (%d found):", matches.size())));
        if (matches.isEmpty()) {
            System.out.println(yellow("No direct matches found. Try broadening your keywords or check our full catalog."));
            return;
        }

        int rank = 1;
        for (ScoredBookMatch match : matches) {
            Book b = match.getBook();
            String priceStr = switch (b.getMode()) {
                case SELL -> String.format("$%.2f (Save $%.2f)", b.getPrice(), b.calculateSavings());
                case DONATE -> green("FREE (Eco-Gift)");
                case EXCHANGE -> yellow("Swap: " + (b.getNeededBookTitle().isBlank() ? b.getNeededSubject() : b.getNeededBookTitle()));
                case REQUEST -> blue("Wanted");
            };

            System.out.printf("%n  [%d] %s (%.1f%% Confidence)%n", rank++, bold(b.getTitle()), match.getMatchScore());
            System.out.printf("      ID: %s | Author: %s | Ed: %s | Cond: %s%n", b.getId(), b.getAuthor(), b.getEdition(), b.getCondition().getLabel());
            System.out.printf("      Mode: %s | Value: %s | Owner: %s%n", b.getMode(), priceStr, b.getOwner() != null ? b.getOwner().getName() : "Campus");
            System.out.printf("      %s%s%n", purple("AI Reason: "), DIM + match.getMatchExplanation() + RESET);
        }

        System.out.println();
        System.out.print(cyan("Enter Book ID to acquire/view details (or press Enter to skip): "));
        String choice = scanner.nextLine().trim();
        if (!choice.isBlank()) {
            executePurchaseOrClaim(choice);
        }
    }

    private void handleBarterLoopDetection() {
        System.out.println(bold("=== 🔄 AUTOMATED RECIPROCAL BARTER LOOP DETECTOR ==="));
        System.out.println(DIM + "Scanning campus textbook requests for mutual zero-cash circular exchanges..." + RESET);
        System.out.println();

        List<BarterMatch> loops = barterService.findAllBarterLoops();

        if (loops.isEmpty()) {
            System.out.println(yellow("No active 2-way barter loops detected right now."));
            System.out.println(DIM + "Tip: List books in EXCHANGE mode specifying your needed subject or book to trigger automatic matches!" + RESET);
            return;
        }

        System.out.println(green(String.format("Found %d active reciprocal zero-cash trade loop(s) across campus!", loops.size())));
        System.out.println();

        for (int i = 0; i < loops.size(); i++) {
            BarterMatch loop = loops.get(i);
            System.out.println(bold(String.format("--- BARTER LOOP #%d ---", i + 1)));
            System.out.println(loop.getFormattedAlert());
            System.out.println();
        }

        System.out.print(cyan("Enter Barter Loop # to execute zero-cash trade (or Enter to skip): "));
        String input = scanner.nextLine().trim();
        if (!input.isBlank()) {
            try {
                int index = Integer.parseInt(input) - 1;
                if (index >= 0 && index < loops.size()) {
                    BarterMatch selected = loops.get(index);
                    Transaction tx = barterService.executeBarterExchange(selected.getBookA().getId(), selected.getBookB().getId());
                    System.out.println("\n" + green("🎉 ZERO-CASH RECIPROCAL TRADE COMPLETED SUCCESSFULLY!"));
                    System.out.println(tx.generateReceipt());
                } else {
                    System.out.println(red("Invalid loop selection."));
                }
            } catch (NumberFormatException e) {
                System.out.println(red("Please enter a valid numeric loop number."));
            }
        }
    }

    private void handleBrowseCatalog() {
        System.out.println(bold("=== 📚 BROWSE TEXTBOOK CATALOG ==="));
        System.out.println("Filter by: [1] All Available  [2] Peer Sell  [3] Eco-Donations (Free)  [4] Barter Exchange  [5] By Subject");
        System.out.print(cyan("Enter filter choice (1-5): "));
        String filter = scanner.nextLine().trim();

        List<Book> books = switch (filter) {
            case "2" -> bookService.getBooksByMode(ListingMode.SELL);
            case "3" -> bookService.getBooksByMode(ListingMode.DONATE);
            case "4" -> bookService.getBooksByMode(ListingMode.EXCHANGE);
            case "5" -> {
                System.out.print("Enter subject (e.g. Computer Science, Mathematics, Electrical Engineering): ");
                String subj = scanner.nextLine().trim();
                yield bookService.getBooksBySubject(subj);
            }
            default -> bookService.getAvailableBooks();
        };

        if (books.isEmpty()) {
            System.out.println(yellow("No books match the selected filter."));
            return;
        }

        printCatalogTable(books);
    }

    private void printCatalogTable(List<Book> books) {
        System.out.println();
        System.out.printf("%-8s | %-32s | %-20s | %-9s | %-12s | %-15s%n",
                "ID", "Title", "Subject", "Condition", "Mode", "Price / Trade");
        printDivider();

        for (Book b : books) {
            String title = b.getTitle().length() > 32 ? b.getTitle().substring(0, 29) + "..." : b.getTitle();
            String subj = b.getSubject().length() > 20 ? b.getSubject().substring(0, 17) + "..." : b.getSubject();
            String priceOrTrade = switch (b.getMode()) {
                case SELL -> String.format("$%.2f (Save $%.0f)", b.getPrice(), b.calculateSavings());
                case DONATE -> green("FREE (+Karma)");
                case EXCHANGE -> yellow("Needs: " + (b.getNeededSubject().isBlank() ? "Trade" : b.getNeededSubject()));
                case REQUEST -> "Wanted";
            };

            System.out.printf("%-8s | %-32s | %-20s | %-9s | %-12s | %-15s%n",
                    b.getId(), title, subj, b.getCondition().name(), b.getMode().name(), priceOrTrade);
        }
        printDivider();
        System.out.printf("Total Listings: %d%n", books.size());
    }

    private void handleListBook() {
        User activeUser = userService.getActiveUser();
        System.out.println(bold("=== ➕ LIST A TEXTBOOK FOR CAMPUS PEERS ==="));
        System.out.printf("Listing as: %s (%s)%n", bold(activeUser.getName()), activeUser.getCampusId());

        System.out.print("Book Title: ");
        String title = scanner.nextLine().trim();
        if (title.isBlank()) {
            System.out.println(red("Title cannot be blank."));
            return;
        }

        System.out.print("Author(s): ");
        String author = scanner.nextLine().trim();

        System.out.print("Edition (e.g. 3rd Edition, Standard): ");
        String edition = scanner.nextLine().trim();

        System.out.print("Subject (e.g. Computer Science, Mathematics, Physics): ");
        String subject = scanner.nextLine().trim();
        if (subject.isBlank()) subject = "General";

        System.out.println("Select Physical Condition:");
        System.out.println("  [1] Brand New  [2] Like New  [3] Good  [4] Fair  [5] Well-Loved");
        System.out.print("Choice (1-5): ");
        String condChoice = scanner.nextLine().trim();
        Condition condition = switch (condChoice) {
            case "1" -> Condition.NEW;
            case "2" -> Condition.LIKE_NEW;
            case "4" -> Condition.FAIR;
            case "5" -> Condition.POOR;
            default -> Condition.GOOD;
        };

        System.out.println("Select Sharing Mode:");
        System.out.println("  [1] SELL     - Set student peer-to-peer price");
        System.out.println("  [2] DONATE   - 100% Free Eco-Gift (earn high eco-karma)");
        System.out.println("  [3] EXCHANGE - Direct zero-cash barter trade");
        System.out.print("Choice (1-3): ");
        String modeChoice = scanner.nextLine().trim();

        ListingMode mode = switch (modeChoice) {
            case "1" -> ListingMode.SELL;
            case "2" -> ListingMode.DONATE;
            case "3" -> ListingMode.EXCHANGE;
            default -> ListingMode.SELL;
        };

        double price = 0.0;
        double retailPrice = 0.0;
        String neededSubj = "";
        String neededTitle = "";

        if (mode == ListingMode.SELL) {
            System.out.print("Enter your asking price ($): ");
            try {
                price = Double.parseDouble(scanner.nextLine().trim());
            } catch (Exception e) {
                price = 15.00;
            }
            System.out.print("Original retail price ($) [for campus savings stats]: ");
            try {
                retailPrice = Double.parseDouble(scanner.nextLine().trim());
            } catch (Exception e) {
                retailPrice = price * 2;
            }
        } else if (mode == ListingMode.DONATE) {
            System.out.print("Original retail price ($) [to track student savings]: ");
            try {
                retailPrice = Double.parseDouble(scanner.nextLine().trim());
            } catch (Exception e) {
                retailPrice = 75.00;
            }
        } else if (mode == ListingMode.EXCHANGE) {
            System.out.print("What subject book are you looking for in return?: ");
            neededSubj = scanner.nextLine().trim();
            System.out.print("Specific book title wanted (optional, or press Enter): ");
            neededTitle = scanner.nextLine().trim();
            System.out.print("Estimated retail value ($): ");
            try {
                retailPrice = Double.parseDouble(scanner.nextLine().trim());
            } catch (Exception e) {
                retailPrice = 80.00;
            }
        }

        String newBookId = "BK-" + (100 + bookService.getAllBooks().size() + 1);
        Book newBook = new Book(newBookId, title, author, edition, subject, condition,
                mode, price, retailPrice, activeUser, neededSubj, neededTitle);

        bookService.addBook(newBook);
        System.out.println("\n" + green("✅ Book successfully listed in catalog!"));
        System.out.println(newBook.getFormattedSummary());

        // Instant Barter Alert Check if listing was in exchange mode
        if (mode == ListingMode.EXCHANGE) {
            List<BarterMatch> matches = barterService.findReciprocalMatchesForBook(newBook);
            if (!matches.isEmpty()) {
                System.out.println("\n" + bold(yellow("⚡ INSTANT BARTER MATCH DETECTED UPON LISTING! ⚡")));
                System.out.println(matches.get(0).getFormattedAlert());
            }
        }
    }

    private void handleExecuteTransaction() {
        System.out.println(bold("=== 🤝 EXECUTE BOOK TRANSACTION ==="));
        System.out.print(cyan("Enter the Book ID you wish to acquire (e.g. BK-105): "));
        String bookId = scanner.nextLine().trim();
        executePurchaseOrClaim(bookId);
    }

    private void executePurchaseOrClaim(String bookId) {
        Optional<Book> optBook = bookService.findBookById(bookId);
        if (optBook.isEmpty()) {
            System.out.println(red("Book with ID '" + bookId + "' does not exist."));
            return;
        }

        Book book = optBook.get();
        User activeUser = userService.getActiveUser();

        if (book.getStatus() != BookStatus.AVAILABLE) {
            System.out.println(yellow("This book is already " + book.getStatus() + "."));
            return;
        }

        if (book.getOwner() != null && book.getOwner().getId().equalsIgnoreCase(activeUser.getId())) {
            System.out.println(yellow("You already own this listing!"));
            return;
        }

        if (book.getMode() == ListingMode.EXCHANGE) {
            System.out.println(yellow("This book is in EXCHANGE mode. Use the Barter Loop Detector (Option 2) or trade a matching book!"));
            List<BarterMatch> potentialMatches = barterService.findReciprocalMatchesForBook(book);
            if (!potentialMatches.isEmpty()) {
                System.out.println(green("Active barter loops available for this book:"));
                System.out.println(potentialMatches.get(0).getFormattedAlert());
            }
            return;
        }

        System.out.println("\nSelected Book: " + book.getFormattedSummary());
        System.out.printf("Acquiring as: %s (%s)%n", bold(activeUser.getName()), activeUser.getCampusId());

        if (book.getMode() == ListingMode.SELL) {
            System.out.printf("Total Peer Purchase Amount: %s (Original Retail: $%.2f | You Save: $%.2f)%n",
                    green(String.format("$%.2f", book.getPrice())), book.getOriginalRetailPrice(), book.calculateSavings());
            System.out.print(cyan("Confirm purchase? (y/n): "));
        } else if (book.getMode() == ListingMode.DONATE) {
            System.out.println(green("100% Free Campus Eco-Donation! You earn eco-points for giving this book a second life."));
            System.out.print(cyan("Confirm claim? (y/n): "));
        }

        String confirm = scanner.nextLine().trim().toLowerCase();
        if (!confirm.startsWith("y")) {
            System.out.println("Transaction cancelled.");
            return;
        }

        // Process Transaction
        book.setStatus(BookStatus.COMPLETED);
        double savings = book.calculateSavings();
        int karmaEarned = (book.getMode() == ListingMode.DONATE) ? 50 : 15;

        activeUser.recordSavings(savings);
        activeUser.addKarma(karmaEarned);

        if (book.getOwner() != null) {
            book.getOwner().incrementBooksShared();
            int ownerKarma = (book.getMode() == ListingMode.DONATE) ? 80 : 25;
            book.getOwner().addKarma(ownerKarma);
        }

        String txId = "TX-" + System.currentTimeMillis();
        Transaction tx = new Transaction(
                txId,
                book.getId(),
                book.getTitle(),
                activeUser.getId(),
                activeUser.getName(),
                book.getOwner() != null ? book.getOwner().getId() : "Campus",
                book.getOwner() != null ? book.getOwner().getName() : "Campus Shared",
                book.getMode(),
                book.getPrice(),
                karmaEarned,
                savings,
                "", "",
                "Completed successfully on BookLoop AI platform"
        );

        bookService.recordTransaction(tx);

        System.out.println("\n" + green("🎉 TRANSACTION COMPLETED SUCCESSFULLY!"));
        System.out.println(tx.generateReceipt());
    }

    private void handleStudentDashboard() {
        User activeUser = userService.getActiveUser();
        System.out.println(bold("=== 👤 STUDENT DASHBOARD & ECO-PROFILE ==="));
        System.out.println(activeUser.getFormattedProfile());

        // Display current active listings
        List<Book> myListings = bookService.getBooksByOwner(activeUser.getId());
        System.out.println("\n" + bold(String.format("MY ACTIVE LISTINGS (%d):", myListings.size())));
        if (myListings.isEmpty()) {
            System.out.println("  (No active listings. Use Option 4 to list a textbook!)");
        } else {
            for (Book b : myListings) {
                System.out.printf("  • [%s] %s - Mode: %s | Status: %s%n",
                        b.getId(), b.getTitle(), b.getMode(), b.getStatus());
            }
        }

        // Wishlist management if activeUser is Student
        if (activeUser instanceof Student student) {
            System.out.println("\n" + bold("WISHLIST MANAGEMENT:"));
            System.out.println("  [1] Add Book / Subject to Wishlist  [2] Remove Wishlist Item  [3] Return");
            System.out.print(cyan("Choice: "));
            String opt = scanner.nextLine().trim();
            if ("1".equals(opt)) {
                System.out.print("Enter book title or academic topic desired: ");
                String item = scanner.nextLine().trim();
                student.addToWishlist(item);
                System.out.println(green("Added '" + item + "' to your wishlist. Smart Match Engine will alert you!"));
            } else if ("2".equals(opt)) {
                System.out.print("Enter item text to remove: ");
                String item = scanner.nextLine().trim();
                boolean removed = student.removeFromWishlist(item);
                if (removed) {
                    System.out.println(green("Removed from wishlist."));
                } else {
                    System.out.println(yellow("Item not found in wishlist."));
                }
            }
        }
    }

    private void handleLeaderboard() {
        System.out.println(bold("=== 🏆 CAMPUS SUSTAINABILITY & ECO-KARMA LEADERBOARD ==="));
        System.out.println(DIM + "Recognizing students spearheading textbook reuse and zero-waste education:" + RESET);
        System.out.println();

        List<User> leaders = userService.getSustainabilityLeaderboard(10);
        System.out.printf("%-4s | %-20s | %-15s | %-10s | %-8s | %-10s%n",
                "Rank", "Student Name", "Campus ID", "Eco-Karma", "Trust", "Shared");
        printDivider();

        int rank = 1;
        for (User u : leaders) {
            String badge = switch (rank) {
                case 1 -> "🥇";
                case 2 -> "🥈";
                case 3 -> "🥉";
                default -> String.format("%2d.", rank);
            };
            System.out.printf("%-4s | %-20s | %-15s | %-10s | %-8.1f★| %-10d%n",
                    badge, u.getName(), u.getCampusId(), u.getKarmaPoints() + " pts",
                    u.calculateTrustScore(), u.getBooksSharedCount());
            rank++;
        }
        printDivider();
    }

    private void handleSwitchUser() {
        System.out.println(bold("=== 👥 SWITCH ACTIVE STUDENT PERSONA ==="));
        System.out.println(DIM + "Switch personas to simulate peer interactions and test reciprocal barter loops:" + RESET);
        System.out.println();

        List<Student> students = userService.getAllStudents();
        for (int i = 0; i < students.size(); i++) {
            Student s = students.get(i);
            boolean isCurrent = s.equals(userService.getActiveUser());
            System.out.printf("  [%d] %s - %s (%s)%s%n",
                    i + 1, s.getName(), s.getDepartment(), s.getCampusId(),
                    isCurrent ? green(" [ACTIVE]") : "");
        }

        System.out.println("  [N] Register New Student Persona");
        System.out.print(cyan("Select student number (or press Enter to keep current): "));
        String choice = scanner.nextLine().trim();

        if (choice.equalsIgnoreCase("N")) {
            System.out.print("Student Full Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Campus Email: ");
            String email = scanner.nextLine().trim();
            System.out.print("Campus ID: ");
            String cid = scanner.nextLine().trim();
            System.out.print("Department: ");
            String dept = scanner.nextLine().trim();

            String newId = "U-" + (100 + students.size() + 1);
            Student newStudent = new Student(newId, name, email, cid, dept, 1, 2027);
            userService.registerUser(newStudent);
            userService.setActiveUser(newStudent);
            System.out.println(green("Registered and switched to: " + name));
        } else if (!choice.isBlank()) {
            try {
                int idx = Integer.parseInt(choice) - 1;
                if (idx >= 0 && idx < students.size()) {
                    Student selected = students.get(idx);
                    userService.setActiveUser(selected);
                    System.out.println(green("Switched active persona to: " + selected.getName()));
                } else {
                    System.out.println(red("Invalid student number."));
                }
            } catch (NumberFormatException e) {
                System.out.println(red("Invalid input."));
            }
        }
    }

    private void handlePersistence() {
        System.out.println(bold("=== 💾 PLATFORM STATE & PERSISTENCE ==="));
        System.out.println("  [1] Save current state snapshot to disk (.bookloop_data/bookloop_state.dat)");
        System.out.println("  [2] Reload state snapshot from disk");
        System.out.println("  [3] Reset & re-seed default campus pilot demo data");
        System.out.print(cyan("Choice (1-3): "));
        String pChoice = scanner.nextLine().trim();

        if ("1".equals(pChoice)) {
            boolean ok = DataPersistenceManager.saveState(userService, bookService);
            if (ok) {
                System.out.println(green("✅ Platform state saved successfully to disk."));
            } else {
                System.out.println(red("❌ Failed to save state snapshot."));
            }
        } else if ("2".equals(pChoice)) {
            boolean ok = DataPersistenceManager.loadState(userService, bookService);
            if (ok) {
                System.out.println(green("✅ Platform state successfully restored from snapshot."));
            } else {
                System.out.println(yellow("No existing snapshot found on disk."));
            }
        } else if ("3".equals(pChoice)) {
            MockDataSeeder.seedDemoData(userService, bookService);
            System.out.println(green("✅ Default campus pilot demo data re-seeded!"));
        }
    }
}
