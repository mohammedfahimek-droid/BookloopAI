# BookloopAI 📚🤖

> **A Java-based campus textbook reuse and AI barter-matching platform.**

BookloopAI enables students to easily exchange, buy, sell, and donate textbooks within campus communities. Powered by smart intent extraction and barter-matching algorithms, students can save money, reduce waste, and find required course materials efficiently.

---

## 🌟 Key Features

- **Smart Search & Intent Parsing Pipeline** (`SmartSearchPipeline`):
  - Natural language query understanding and keyword extraction.
  - Intent detection (budget/price caps, condition filters, listing mode).
  - Subject classification (Computer Science, Mathematics, Physics, Electrical Engineering, Business).
  - Ranked semantic search scoring with fuzzy matching.
- **AI Barter Matching** (`BarterMatch`):
  - Intelligent matching algorithms to connect students with reciprocal textbook wishlists.
  - Reduces friction in peer-to-peer exchanges.
- **Flexible Listing Modes**:
  - `SELL` / `BUY`
  - `BARTER` (direct textbook swap)
  - `DONATE` (free / karma-based campus sharing)
- **Karma & Reputation System**:
  - Incentivizes community textbook donations and fair sharing.
- **Domain-Driven Architecture**:
  - Clean separation into `model`, `service`, `ai`, and `exception` packages.

---

## 📁 Project Structure

```text
src/
└── com/
    └── bookloop/
        ├── ai/                 # AI & Smart Search
        │   ├── ScoredBookMatch.java
        │   ├── SearchQueryIntent.java
        │   └── SmartSearchPipeline.java
        ├── exception/          # Custom Domain Exceptions
        │   ├── BarterMismatchException.java
        │   ├── BookLoopException.java
        │   ├── BookNotFoundException.java
        │   ├── DuplicateListingException.java
        │   ├── InsufficientKarmaException.java
        │   ├── InvalidModeException.java
        │   └── UserNotFoundException.java
        ├── model/              # Domain Models
        │   ├── Book.java
        │   ├── BookStatus.java
        │   ├── Condition.java
        │   ├── ListingMode.java
        │   ├── Student.java
        │   ├── Transaction.java
        │   └── User.java
        └── service/            # Core Services & Matchers
            ├── BarterMatch.java
            └── IUserService.java
```

---

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK 17 or higher recommended)

### Compilation
Compile all Java source files from the project root:

```bash
javac -d bin $(find src -name "*.java")
```
*Or on Windows PowerShell:*
```powershell
Get-ChildItem -Recurse -Filter *.java src | ForEach-Object { $_.FullName } | Out-File -Encoding ascii sources.txt
javac -d bin @sources.txt
Remove-Item sources.txt
```

---

## 📄 License
This project is open source and available under the [MIT License](LICENSE).
