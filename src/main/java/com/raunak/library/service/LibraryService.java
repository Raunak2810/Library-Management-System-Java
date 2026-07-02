package com.raunak.library.service;
import com.raunak.library.exception.BookNotFoundException;
import com.raunak.library.exception.DuplicateIsbnException;
import com.raunak.library.exception.InvalidBookDataException;
import com.raunak.library.model.Book;
import com.raunak.library.repository.BookRepository;
import com.raunak.library.util.AppLogger;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
//  Business/service layer. This is the single integration point that wires
//  together the Repository (persistence), Factory (object creation + validation), and Strategy (search algorithm) patterns behind one clean
//  API that the CLI layer calls into. Keeping this layer independent of
//  System.in/System.out is what makes it unit-testable with JUnit.

public class LibraryService{
    private static final Logger log = AppLogger.getLogger();
    private final BookRepository repository;
    private SearchStrategy searchStrategy;
    public LibraryService(BookRepository repository){
        this.repository = repository;
        this.searchStrategy = new TitleSearchStrategy(); // default strategy
    }
    //  Allows the caller to swap search behaviour at runtime (Strategy pattern)
    public void setSearchStrategy(SearchStrategy strategy){
        this.searchStrategy = strategy;
    }
    public Book addBook(String isbn, String title, String author, int year)
            throws InvalidBookDataException, DuplicateIsbnException{
        Book book = BookFactory.createBook(isbn, title, author, year);
        if (repository.existsByIsbn(book.getIsbn())){
            log.warning("Attempted to add duplicate ISBN: " + book.getIsbn());
            throw new DuplicateIsbnException(book.getIsbn());
        }
        repository.save(book);
        log.info("Added book: " + book.getIsbn());
        return book;
    }
    public Collection<Book> listAllBooks(){
        return repository.findAll();
    }
    public Book findByIsbn(String isbn) throws BookNotFoundException{
        return repository.findByIsbn(isbn).orElseThrow(()->{
            log.warning("Lookup failed, ISBN not found: " + isbn);
            return new BookNotFoundException(isbn);
        });
    }
    public List<Book> search(String query){
        return searchStrategy.search(repository.findAll(), query);
    }
    public Book updateBook(String isbn, String title, String author, int year)
            throws BookNotFoundException, InvalidBookDataException{
        Book existing = findByIsbn(isbn);
        Book validated = BookFactory.createBook(isbn, title, author, year);
        existing.setTitle(validated.getTitle());
        existing.setAuthor(validated.getAuthor());
        existing.setPublicationYear(validated.getPublicationYear());
        repository.save(existing);
        log.info("Updated book: " + isbn);
        return existing;
    }
    public void deleteBook(String isbn) throws BookNotFoundException{
        if (!repository.existsByIsbn(isbn)){
            throw new BookNotFoundException(isbn);
        }
        repository.deleteByIsbn(isbn);
        log.info("Deleted book: " + isbn);
    }
    public int count(){
        return repository.findAll().size();
    }
}
