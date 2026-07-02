package com.raunak.library.service;
import com.raunak.library.exception.InvalidBookDataException;
import com.raunak.library.model.Book;
import java.time.Year;
/**
 * Factory pattern: centralizes Book creation and field validation so
 * "how do I safely build a Book" lives in one place instead of being
 * duplicated across the CLI and service layers.
 */
public final class BookFactory{
    private BookFactory(){
    }
    public static Book createBook(String isbn, String title, String author, int year)
            throws InvalidBookDataException{
        if (isbn == null || isbn.isBlank()){
            throw new InvalidBookDataException("ISBN cannot be empty");
        }
        if (title == null || title.isBlank()){
            throw new InvalidBookDataException("Title cannot be empty");
        }
        if (author == null || author.isBlank()){
            throw new InvalidBookDataException("Author cannot be empty");
        }
        if (year < 0 || year > Year.now().getValue() + 1){
            throw new InvalidBookDataException("Publication year is not valid: " + year);
        }
        return new Book(isbn.trim(), title.trim(), author.trim(), year);
    }
}
