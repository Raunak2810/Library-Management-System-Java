public class Book {
    private String isbn;
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
    @Override
    public String toString(){
        return "ISBN: " + isbn +
                ", Title: " + title +
                ", Author: " + author +
                ", Year: " + publicationYear;
    }
}