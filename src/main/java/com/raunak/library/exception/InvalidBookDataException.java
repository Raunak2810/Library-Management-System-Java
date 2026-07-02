package com.raunak.library.exception;
//Thrown when book field data fails validation (empty fields, bad year, etc.).
public class InvalidBookDataException extends Exception {
    public InvalidBookDataException(String message) {
        super(message);
    }
}
