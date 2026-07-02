package com.raunak.library.repository;
import com.raunak.library.model.Book;
import java.util.Collection;
import java.util.Optional;
// Repository pattern: abstracts how Book records are persisted so the
// service layer never depends on the storage mechanism directly.
// Swapping CSV storage for a database later only requires a new
// implementation of this interface -- no changes to LibraryService.
public interface BookRepository{
    void save(Book book);
    Optional<Book> findByIsbn(String isbn);
    Collection<Book> findAll();
    boolean existsByIsbn(String isbn);
    void deleteByIsbn(String isbn);
    // Persists any in-memory changes to durable storage (CSV file, etc.)
    void flush();
}
