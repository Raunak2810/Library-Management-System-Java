# Library Inventory Management System
### Week 2 Java Application – Core Functionality Implementation

---

## Overview

A command-line Java application for managing a library's book inventory.  
It supports full **CRUD** operations (Create, Read, Update, Delete) on `Book`
records stored in memory via a `LinkedHashMap`.

---

## Project Structure

```
library-app/
├── src/
│   ├── Book.java          # Book entity (ISBN, title, author, year)
│   ├── LibraryManager.java # CRUD operations + custom exceptions
│   └── LibraryApp.java    # Entry point, menu-driven CLI
└── README.md
```

---

## Prerequisites

| Requirement | Minimum version |
|---|---|
| Java Development Kit (JDK) | 17 or later (uses switch-expression syntax) |

Verify your installation:
```bash
java  -version   # runtime
javac -version   # compiler
```

---

## Compiling

From the **project root** directory (`library-app/`):

```bash
# 1. Create an output directory for .class files
mkdir -p out

# 2. Compile all three source files at once
javac -d out src/Book.java src/LibraryManager.java src/LibraryApp.java
```

You should see no output — a silent exit means success.

---

## Running

```bash
java -cp out LibraryApp
```

The application pre-loads **five classic novels** as sample data so you can
explore all features immediately without manually adding books first.

---

## Using the Application

```
╔══════════════════════════════════════════════╗
║    📚  Library Inventory Manager v1.0  📚    ║
╚══════════════════════════════════════════════╝

┌─────────────────────────────────┐
│            MAIN MENU            │
├─────────────────────────────────┤
│  1.  Add a new book             │
│  2.  List all books             │
│  3.  Find book by ISBN          │
│  4.  Search books by title      │
│  5.  Update a book              │
│  6.  Delete a book              │
│  0.  Exit                       │
└─────────────────────────────────┘
```

### Option 1 – Add a new book
Enter the ISBN, title, author, and publication year when prompted.  
Duplicate ISBNs are rejected with a clear error message.

### Option 2 – List all books
Displays every book in the inventory as a formatted table including
ISBN, title, author, and year, with a total count at the bottom.

### Option 3 – Find book by ISBN
Enter an exact ISBN; the matching book's details are displayed in a table.

### Option 4 – Search by title keyword
Type any word or phrase; all books whose titles contain that string
(case-insensitive) are listed.

### Option 5 – Update a book
Enter the ISBN of the book to update.  The current record is shown,
then you are prompted for each field.  **Press Enter to keep an existing
value** — only fields you type something into will be changed.

### Option 6 – Delete a book
Enter the ISBN; the record is shown and you must confirm with `yes`
before the deletion proceeds.

### Option 0 – Exit
Closes the application gracefully.

---

## Design Decisions

| Decision | Rationale |
|---|---|
| `LinkedHashMap<String, Book>` | O(1) ISBN lookups + preserves insertion order for consistent listing |
| Custom exceptions (`BookNotFoundException`, `DuplicateIsbnException`) | Clear separation of error types; callers can catch them independently |
| Null-safe update fields | Passing `null` / `-1` means "no change"; avoids accidental overwrites |
| Input validation in `LibraryManager` | Business rules live in the manager, not the UI layer |
| Sample data pre-load | Enables immediate demo without manual data entry |

---

## Example Session

```
  Enter your choice: 1

  ── Add New Book ──────────────────────────────
  ISBN          : 978-0-06-093546-9
  Title         : Harper Lee Collection
  Author        : Harper Lee
  Published year: 2015

  ✔  Book added successfully!

  Enter your choice: 4

  ── Search by Title ───────────────────────────
  Enter keyword: harper

  Found 1 match(es) for "harper":

+--------------------+--------------------------------+------------------------+------+
| ISBN               | Title                          | Author                 | Year |
+--------------------+--------------------------------+------------------------+------+
| 978-0-06-093546-9  | Harper Lee Collection          | Harper Lee             | 2015 |
+--------------------+--------------------------------+------------------------+------+
```

---

## Author
Raunak Mishra
