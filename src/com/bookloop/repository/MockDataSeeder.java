package com.bookloop.repository;

import com.bookloop.model.*;
import com.bookloop.service.IBookService;
import com.bookloop.service.IUserService;

import java.util.List;

/**
 * Seeds rich, realistic campus demo data reflecting BookLoop AI pilot launch metrics.
 */
public class MockDataSeeder {

    public static void seedDemoData(IUserService userService, IBookService bookService) {
        // 1. Seed Student Profiles
        Student aarav = new Student("U-101", "Aarav Sharma", "aarav.s@campus.edu", "CS-2022-041", "Computer Science", 5, 2025);
        aarav.addKarma(120);
        aarav.addToWishlist("Database System Concepts");
        aarav.addToWishlist("DBMS");

        Student priya = new Student("U-102", "Priya Patel", "priya.p@campus.edu", "CS-2022-089", "Computer Science", 5, 2025);
        priya.addKarma(180);
        priya.addToWishlist("Head First Java");
        priya.addToWishlist("Java Programming");

        Student marcus = new Student("U-103", "Marcus Chen", "marcus.c@campus.edu", "EC-2023-012", "Electrical Engineering", 4, 2026);
        marcus.addKarma(240);
        marcus.addToWishlist("VLSI Design");

        Student sophia = new Student("U-104", "Sophia Rodriguez", "sophia.r@campus.edu", "CS-2023-115", "Computer Science", 3, 2026);
        sophia.addKarma(310);
        sophia.addToWishlist("Clean Architecture");

        Student liam = new Student("U-105", "Liam O'Connor", "liam.oc@campus.edu", "MA-2024-007", "Mathematics", 2, 2027);
        liam.addKarma(95);
        liam.addToWishlist("Linear Algebra Done Right");

        Student elena = new Student("U-106", "Elena Rostova", "elena.r@campus.edu", "CS-2021-033", "Computer Science", 6, 2024);
        elena.addKarma(160);
        elena.addToWishlist("Computer Networking");

        Student david = new Student("U-107", "David Kim", "david.k@campus.edu", "CS-2022-055", "Computer Science", 4, 2025);
        david.addKarma(130);
        david.addToWishlist("Operating Systems");

        for (Student s : List.of(aarav, priya, marcus, sophia, liam, elena, david)) {
            userService.registerUser(s);
        }

        // Set default active session to Aarav
        userService.setActiveUser(aarav);

        // 2. Seed Textbooks
        // Loop Pair 1: Aarav (has Java, wants DBMS) <--> Priya (has DBMS, wants Java)
        Book b1 = new Book("BK-101", "Head First Java", "Kathy Sierra & Bert Bates", "3rd Edition",
                "Computer Science", Condition.LIKE_NEW, ListingMode.EXCHANGE, 0.0, 55.0,
                aarav, "Computer Science", "Database System Concepts");

        Book b2 = new Book("BK-102", "Database System Concepts", "Silberschatz, Korth & Sudarshan", "7th Edition",
                "Computer Science", Condition.GOOD, ListingMode.EXCHANGE, 0.0, 110.0,
                priya, "Computer Science", "Head First Java");

        // Loop Pair 2: Elena (has OS, wants Networking) <--> David (has Networking, wants OS)
        Book b3 = new Book("BK-103", "Operating System Concepts (Dinosaur)", "Silberschatz & Galvin", "10th Edition",
                "Computer Science", Condition.GOOD, ListingMode.EXCHANGE, 0.0, 125.0,
                elena, "Computer Science", "Computer Networking");

        Book b4 = new Book("BK-104", "Computer Networking: A Top-Down Approach", "Kurose & Ross", "8th Edition",
                "Computer Science", Condition.LIKE_NEW, ListingMode.EXCHANGE, 0.0, 135.0,
                david, "Computer Science", "Operating Systems");

        // Affordable SELL Listings
        Book b5 = new Book("BK-105", "Microelectronic Circuits", "Adel S. Sedra & Kenneth C. Smith", "8th Edition",
                "Electrical Engineering", Condition.GOOD, ListingMode.SELL, 28.00, 145.00,
                marcus, "", "");

        Book b6 = new Book("BK-106", "Calculus: Early Transcendentals", "James Stewart", "9th Edition",
                "Mathematics", Condition.LIKE_NEW, ListingMode.SELL, 22.00, 130.00,
                liam, "", "");

        Book b7 = new Book("BK-107", "Artificial Intelligence: A Modern Approach", "Stuart Russell & Peter Norvig", "4th Edition",
                "Computer Science", Condition.GOOD, ListingMode.SELL, 32.00, 120.00,
                sophia, "", "");

        Book b8 = new Book("BK-108", "Discrete Mathematics and Its Applications", "Kenneth Rosen", "8th Edition",
                "Mathematics", Condition.FAIR, ListingMode.SELL, 18.00, 115.00,
                liam, "", "");

        // 100% Free Campus Eco-Donations (High Karma)
        Book b9 = new Book("BK-109", "Introduction to Algorithms (CLRS)", "Cormen, Leiserson, Rivest & Stein", "4th Edition",
                "Computer Science", Condition.LIKE_NEW, ListingMode.DONATE, 0.0, 95.00,
                sophia, "", "");

        Book b10 = new Book("BK-110", "Clean Code: A Handbook of Agile Software Craftsmanship", "Robert C. Martin", "1st Edition",
                "Computer Science", Condition.NEW, ListingMode.DONATE, 0.0, 48.00,
                aarav, "", "");

        Book b11 = new Book("BK-111", "Principles of Physics", "Halliday, Resnick & Walker", "11th Edition",
                "Physics", Condition.GOOD, ListingMode.DONATE, 0.0, 140.00,
                marcus, "", "");

        for (Book b : List.of(b1, b2, b3, b4, b5, b6, b7, b8, b9, b10, b11)) {
            bookService.addBook(b);
        }

        // 3. Seed Past Pilot Transactions to showcase historical campus sustainability impact
        Transaction tx1 = new Transaction("TX-HIST-01", "BK-091", "Python Crash Course",
                aarav.getId(), aarav.getName(), priya.getId(), priya.getName(),
                ListingMode.DONATE, 0.0, 50, 40.0, "", "", "Campus Eco-Donation Drive");

        Transaction tx2 = new Transaction("TX-HIST-02", "BK-092", "Linear Algebra and Its Applications",
                marcus.getId(), marcus.getName(), liam.getId(), liam.getName(),
                ListingMode.SELL, 25.0, 10, 85.0, "", "", "P2P Peer Purchase");

        Transaction tx3 = new Transaction("TX-HIST-03", "BK-093", "Introduction to Automata Theory",
                sophia.getId(), sophia.getName(), elena.getId(), elena.getName(),
                ListingMode.EXCHANGE, 0.0, 60, 110.0, "BK-094", "Compilers: Principles & Techniques", "Zero-cash Barter Exchange");

        bookService.recordTransaction(tx1);
        bookService.recordTransaction(tx2);
        bookService.recordTransaction(tx3);
    }
}
