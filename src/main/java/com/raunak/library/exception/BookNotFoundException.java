package com.raunak.library.exception;
// Thrown when a lookup, update, or delete operation targets an ISBN
// that does not exist in the repository.
public class BookNotFoundException extends Exception{
    public BookNotFoundException(String isbn){
        super("Book not found for ISBN: " + isbn);
    }
}
