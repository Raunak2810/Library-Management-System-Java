package com.raunak.library.exception;
// Thrown when attempting to add a book whose ISBN already exists
// in the repository.
public class DuplicateIsbnException extends Exception{
    public DuplicateIsbnException(String isbn){
        super("A book with ISBN " + isbn + " already exists");
    }
}
