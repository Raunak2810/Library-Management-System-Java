package com.raunak.library;
import com.raunak.library.exception.BookNotFoundException;
import com.raunak.library.exception.DuplicateIsbnException;
import com.raunak.library.exception.InvalidBookDataException;
import com.raunak.library.model.Book;
import com.raunak.library.repository.BookRepository;
import com.raunak.library.repository.CsvBookRepository;
import com.raunak.library.service.AuthorSearchStrategy;
import com.raunak.library.service.LibraryService;
import com.raunak.library.service.TitleSearchStrategy;
import com.raunak.library.util.AppLogger;
import java.util.Collection;
import java.util.Scanner;
import java.util.logging.Logger;

//  Menu-driven CLI entry point. This class only handles I/O; all business
//  logic lives in LibraryService, and all persistence lives behind
//  BookRepository. That separation is what let this Week 5 integration
//  plug the Week 2-4 pieces together without rewriting them.

public class LibraryApp{
    private static final Logger log = AppLogger.getLogger();
    private static final String DATA_FILE = "data/books.csv";
    private final Scanner sc = new Scanner(System.in);
    private final LibraryService library;
    public LibraryApp(LibraryService library){
        this.library = library;
    }
    public static void main(String[]args){
        log.info("Application starting...");
        BookRepository repository = new CsvBookRepository(DATA_FILE);
        LibraryService service = new LibraryService(repository);
        seedIfEmpty(service);
        new LibraryApp(service).run();
        log.info("Application shut down cleanly.");
    }
    //Pre-loads sample data only on first run, when the CSV file was empty
    private static void seedIfEmpty(LibraryService service){
        if (service.count() > 0) return;
        try{
            service.addBook("978-0-13-468599-1", "Effective Java", "Joshua Bloch", 2018);
            service.addBook("978-0-596-00712-6", "Head First Design Patterns", "Freeman & Robson", 2004);
            service.addBook("978-0-06-093546-9", "To Kill a Mockingbird", "Harper Lee", 1960);
        } catch (InvalidBookDataException | DuplicateIsbnException e){
            log.warning("Seed data failed to load: " + e.getMessage());
        }
    }
    public void run(){
        boolean running = true;
        while (running){
            printMenu();
            String choice = sc.nextLine().trim();
            try{
                switch (choice){
                    case "1": addBook(); break;
                    case "2": listAllBooks(); break;
                    case "3": findBook(); break;
                    case "4": searchByTitle(); break;
                    case "5": searchByAuthor(); break;
                    case "6": updateBook(); break;
                    case "7": deleteBook(); break;
                    case "0":
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice, try again.");
                }
            } catch (Exception e){
                // Defensive catch-all so a single bad interaction never crashes the CLI.
                log.severe("Unexpected error handling menu choice " + choice + ": " + e.getMessage());
                System.out.println("Something went wrong: " + e.getMessage());
            }
        }
        sc.close();
    }
    private void printMenu(){
        System.out.println("\n===== Library Management System =====");
        System.out.println("1. Add Book");
        System.out.println("2. List All Books");
        System.out.println("3. Find Book by ISBN");
        System.out.println("4. Search by Title");
        System.out.println("5. Search by Author");
        System.out.println("6. Update Book");
        System.out.println("7. Delete Book");
        System.out.println("0. Exit");
        System.out.print("Enter choice: ");
    }
    private void addBook(){
        System.out.print("Enter ISBN: ");
        String isbn = sc.nextLine();
        System.out.print("Enter Title: ");
        String title = sc.nextLine();
        System.out.print("Enter Author: ");
        String author = sc.nextLine();
        System.out.print("Enter Year: ");
        int year = readInt();
        try{
            library.addBook(isbn, title, author, year);
            System.out.println("Book added successfully.");
        } catch (InvalidBookDataException | DuplicateIsbnException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void listAllBooks(){
        Collection<Book> books = library.listAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }
        books.forEach(System.out::println);
        System.out.println("Total: " + books.size() + " book(s)");
    }
    private void findBook(){
        System.out.print("Enter ISBN: ");
        String isbn = sc.nextLine();
        try{
            System.out.println(library.findByIsbn(isbn));
        } catch (BookNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void searchByTitle(){
        System.out.print("Enter title keyword: ");
        String query = sc.nextLine();
        library.setSearchStrategy(new TitleSearchStrategy());
        printSearchResults(query);
    }
    private void searchByAuthor(){
        System.out.print("Enter author keyword: ");
        String query = sc.nextLine();
        library.setSearchStrategy(new AuthorSearchStrategy());
        printSearchResults(query);
    }
    private void printSearchResults(String query){
        var results = library.search(query);
        if (results.isEmpty()){
            System.out.println("No matches found.");
        } else{
            results.forEach(System.out::println);
        }
    }
    private void updateBook(){
        System.out.print("Enter ISBN of book to update: ");
        String isbn = sc.nextLine();
        System.out.print("New Title: ");
        String title = sc.nextLine();
        System.out.print("New Author: ");
        String author = sc.nextLine();
        System.out.print("New Year: ");
        int year = readInt();
        try{
            library.updateBook(isbn, title, author, year);
            System.out.println("Book updated successfully.");
        } catch(BookNotFoundException | InvalidBookDataException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    private void deleteBook(){
        System.out.print("Enter ISBN of book to delete: ");
        String isbn = sc.nextLine();
        System.out.print("Confirm delete? (yes/no): ");
        String confirm = sc.nextLine();
        if (!confirm.equalsIgnoreCase("yes")){
            System.out.println("Delete cancelled.");
            return;
        }
        try{
            library.deleteBook(isbn);
            System.out.println("Book deleted successfully.");
        } catch (BookNotFoundException e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    private int readInt(){
        while (true){
            String input = sc.nextLine();
            try{
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e){
                System.out.print("Please enter a valid number: ");
            }
        }
    }
}
