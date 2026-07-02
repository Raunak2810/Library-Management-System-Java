package com.raunak.library.model;
import java.util.Objects;
// Book entity representing a single record in the library inventory.
// Immutable ISBN (primary key); other fields are mutable to support updates.
public class Book{
    private final String isbn;
    private String title;
    private String author;
    private int publicationYear;
    public Book(String isbn, String title, String author, int publicationYear){
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
    }
    public String getIsbn(){
        return isbn;
    }
    public String getTitle(){
        return title;
    }
    public String getAuthor(){
        return author;
    }
    public int getPublicationYear(){
        return publicationYear;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setAuthor(String author){
        this.author = author;
    }
    public void setPublicationYear(int year){
        this.publicationYear = year;
    }
// Converts this Book into a single CSV row. Commas inside fields are
// escaped by wrapping the field in double quotes.
    public String toCsvRow(){
        return String.join(",",
                escape(isbn), escape(title), escape(author), String.valueOf(publicationYear));
    }
    private String escape(String value){
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (!(o instanceof Book)) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }
    @Override
    public int hashCode(){
        return Objects.hash(isbn);
    }
    @Override
    public String toString(){
        return "ISBN: " + isbn +
                ", Title: " + title +
                ", Author: " + author +
                ", Year: " + publicationYear;
    }
}