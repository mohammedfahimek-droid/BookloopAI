# BookLoop AI — Campus Textbook Circular Economy & Barter Platform

> **A production-grade Java application empowering zero-waste campus textbook sharing, peer-to-peer sales, eco-donations, and automated reciprocal barter matching.**

---

## 🌟 Key Architectural Pillars

### 1. Flexible Ownership & Circulation Modes
- **SELL**: Peer-to-peer textbook sales at fair student prices with calculated savings versus original retail value.
- **DONATE**: Free circular redistribution that generates high **Eco-Karma points** and trust ratings for the donor.
- **EXCHANGE**: Zero-cash reciprocal textbook trades detected and matched by the platform.
- **REQUEST**: Student-specific wishlist postings that automatically notify users when matching material surfaces.

### 2. AI-Powered Smart Match Engine
- **Heuristic Intent Parsing**: Processes free-form natural language queries (e.g., *"affordable beginner book for learning Java"*, *"free algorithms textbook in clean condition"*, *"calculus under 25 dollars"*).
- **Multi-Factor Ranking Formula**:
  - Academic Subject alignment: `+30 pts`
  - Keyword & Title overlap (TF-IDF weighted): `up to +35 pts`
  - Listing Mode alignment: `+15 pts`
  - Budget & Price constraint satisfaction: `+15 pts`
  - Pedagogical level (Beginner vs. Advanced): `+10 pts`
  - Book condition & Student trust score bonus: `+5 pts`

### 3. Automated 2-Way Reciprocal Barter Loop Detection
- Detects mutual cycles:
  $$\text{Student } A \text{ has Book } X \text{ and needs } Y \quad \Longleftrightarrow \quad \text{Student } B \text{ has Book } Y \text{ and needs } X$$
- Evaluates title keywords, academic subjects, and personal student wishlists.
- Emits **Instant Zero-Cash Barter Alerts** and generates formal dual-sided transaction receipts awarding `+60 Eco-Karma` points to each participant.

---

## 🏗️ Clean Modular Package Structure

```
src/
└── com/
    └── bookloop/
        ├── Main.java                          # Bootstrap runner with CLI and --test mode
        ├── model/                             # Domain models & entities
        │   ├── User.java                      # Abstract base user (Encapsulation & Abstraction)
        │   ├── Student.java                   # Concrete student entity (Inheritance & Polymorphism)
        │   ├── Book.java                      # Core textbook listing entity (Comparable)
        │   ├── Condition.java                 # Enum: NEW, LIKE_NEW, GOOD, FAIR, POOR
        │   ├── ListingMode.java               # Enum: SELL, DONATE, EXCHANGE, REQUEST
        │   ├── BookStatus.java                # Enum: AVAILABLE, PENDING_MATCH, COMPLETED, ARCHIVED
        │   └── Transaction.java               # Audit record with formatted receipt generator
        ├── exception/                         # Custom domain exceptions
        │   ├── BookLoopException.java         # Base domain runtime exception
        │   ├── BookNotFoundException.java     # Missing book entity
        │   ├── InvalidModeException.java      # Mode mismatch or invalid state transition
        │   ├── UserNotFoundException.java     # Missing student/user entity
        │   ├── DuplicateListingException.java # Prevents duplicate active listings
        │   ├── InsufficientKarmaException.java# Eco-karma threshold violations
        │   └── BarterMismatchException.java   # Invalid or self-referential barter trades
        ├── ai/                                # Heuristic NLP & semantic search pipeline
        │   ├── SearchQueryIntent.java         # Parsed intent (Subject, Budget, Level, Mode, Urgency)
        │   ├── SmartSearchPipeline.java       # Heuristic NLP tokenizer & scoring engine
        │   └── ScoredBookMatch.java           # Ranked match with human-readable reasoning
        ├── service/                           # Business logic & Interface contracts
        │   ├── IUserService.java / UserService.java
        │   ├── IBookService.java / BookService.java
        │   ├── IBarterService.java / BarterService.java
        │   ├── IMatchingEngine.java / MatchingEngine.java
        │   └── BarterMatch.java               # Reciprocal 2-way match data structure
        ├── repository/                        # State persistence and initial demo seeding
        │   ├── DataPersistenceManager.java    # Java Object Serialization to .bookloop_data/
        │   └── MockDataSeeder.java            # Realistic campus pilot seed data
        └── ui/                                # User interface layer
            ├── ConsoleTheme.java              # ANSI terminal colors & box formatting
            └── BookLoopCLI.java               # Interactive menu-driven console CLI
```

---

## 🎓 Demonstrating Core OOP Principles

| Principle | Implementation in BookLoop AI |
|---|---|
| **Encapsulation** | Private fields with strict validation in `User`, `Student`, `Book`, `Transaction`, and `BarterMatch`. Immutable state where appropriate (`final` identifiers, unmodifiable collections). |
| **Inheritance** | `Student extends User` inheriting karma tracking, savings calculation, trust scoring, and identity attributes while introducing semester, graduation year, and academic wishlists. |
| **Polymorphism** | Abstract methods `getRoleName()`, `getFormattedProfile()`, and `calculateTrustScore()` in `User`, overridden with student-specific formulas in `Student`. Interfaces `IUserService`, `IBookService`, `IBarterService`, and `IMatchingEngine` decoupled from their implementations. |
| **Abstraction** | Interfaces define strict domain contracts (`IBookService`, `IBarterService`). The AI pipeline abstracts text tokenization and intent scoring behind a single `rankMatches()` call. |
| **Collections Framework** | `ConcurrentHashMap` for thread-safe catalogs, `CopyOnWriteArrayList` for audit histories, `HashSet` for search tag indexing, and Java Streams for filtering, sorting (`Comparable`), and leaderboard aggregation. |
| **Exception Handling** | Custom hierarchy rooted at `BookLoopException` providing contextual error messages for catalog lookups, invalid mode operations, and barter mismatches. |

---

## 🚀 How to Run

### Option 1: Using PowerShell
```powershell
.\run.ps1
```

### Option 2: Using Batch Script (CMD)
```cmd
run.bat
```

### Option 3: Manual Compilation & Execution
```bash
# Compile all source files into bin/
javac -encoding UTF-8 -d bin src/com/bookloop/**/*.java src/com/bookloop/*.java

# Run interactive CLI
java -Dfile.encoding=UTF-8 -cp bin com.bookloop.Main

# Run automated verification suite
java -Dfile.encoding=UTF-8 -cp bin com.bookloop.Main --test
```

---

## 🧪 Built-in Automated Verification Suite
Run `java -cp bin com.bookloop.Main --test` to verify:
1. Catalog inventory & mock data seeding.
2. AI heuristic intent parsing on queries like `"affordable beginner book for learning Java"`.
3. Automated detection of reciprocal barter cycles (e.g., Aarav Sharma $\longleftrightarrow$ Priya Patel).
4. Zero-cash barter execution, dual karma award (`+60 pts`), and receipt generation.
5. Campus sustainability leaderboard calculation.
6. Object serialization persistence to `.bookloop_data/`.
