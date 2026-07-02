package com.raunak.library.service;
import com.raunak.library.model.Book;
import java.util.Collection;
import java.util.List;

/**
 * Strategy pattern: decouples "how a search is performed" from the code
 * that requests a search. New search strategies (by author, by year
 * range, fuzzy match, etc.) can be added without modifying LibraryService.
 */
public interface SearchStrategy{
    List<Book> search(Collection<Book> books, String query);
}
