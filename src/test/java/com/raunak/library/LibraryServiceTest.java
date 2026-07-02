package com.raunak.library;
import com.raunak.library.exception.BookNotFoundException;
import com.raunak.library.exception.DuplicateIsbnException;
import com.raunak.library.exception.InvalidBookDataException;
import com.raunak.library.repository.BookRepository;
import com.raunak.library.repository.CsvBookRepository;
import com.raunak.library.service.AuthorSearchStrategy;
import com.raunak.library.service.LibraryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;


//  Integration-level tests: exercises LibraryService against a real
//  CsvBookRepository backed by a temp file, verifying the Week 5
//  integration actually works end to end.

class LibraryServiceTest{
    private LibraryService service;
    private Path tempCsv;
    @BeforeEach
    void setUp(@TempDir Path tempDir){
        tempCsv = tempDir.resolve("test-books.csv");
        BookRepository repo = new CsvBookRepository(tempCsv.toString());
        service = new LibraryService(repo);
    }
    @Test
    void addBook_thenFindByIsbn_returnsSameBook() throws Exception{
        service.addBook("111", "Clean Code", "Robert Martin", 2008);
        var book = service.findByIsbn("111");
        assertEquals("Clean Code", book.getTitle());
    }
    @Test
    void addBook_withDuplicateIsbn_throws()throws Exception{
        service.addBook("111", "Clean Code", "Robert Martin", 2008);
        assertThrows(DuplicateIsbnException.class,
                () -> service.addBook("111", "Other", "Someone", 2020));
    }
    @Test
    void addBook_withInvalidYear_throws(){
        assertThrows(InvalidBookDataException.class,
                () -> service.addBook("222", "Bad Year Book", "Author", 5000));
    }
    @Test
    void findByIsbn_missing_throwsBookNotFound(){
        assertThrows(BookNotFoundException.class, () -> service.findByIsbn("does-not-exist"));
    }
    @Test
    void updateBook_changesFields()throws Exception{
        service.addBook("333", "Old Title", "Old Author", 2000);
        service.updateBook("333", "New Title", "New Author", 2021);
        var updated = service.findByIsbn("333");
        assertEquals("New Title", updated.getTitle());
        assertEquals(2021, updated.getPublicationYear());
    }

    @Test
    void deleteBook_removesRecord() throws Exception{
        service.addBook("444", "Temp Book", "Author", 2010);
        service.deleteBook("444");
        assertThrows(BookNotFoundException.class, () -> service.findByIsbn("444"));
    }
    @Test
    void searchByAuthorStrategy_findsMatch()throws Exception{
        service.addBook("555", "Some Title", "Jane Austen", 1813);
        service.setSearchStrategy(new AuthorSearchStrategy());
        var results = service.search("austen");
        assertEquals(1, results.size());
    }
    @Test
    void persistence_survivesRepositoryReload()throws Exception{
        service.addBook("666", "Persisted Book", "Author", 2015);
        // simulate app restart by creating a fresh repository pointed at the same file
        BookRepository reloaded = new CsvBookRepository(tempCsv.toString());
        LibraryService reloadedService = new LibraryService(reloaded);
        assertEquals(1, reloadedService.count());
        assertEquals("Persisted Book", reloadedService.findByIsbn("666").getTitle());
    }
    @AfterEach
    void tearDown(){
        // TempDir is cleaned up automatically by JUnit
    }
}
