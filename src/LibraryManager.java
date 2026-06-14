import java.util.LinkedHashMap;
import java.util.Map;
public class LibraryManager {
    private Map<String, Book> books = new LinkedHashMap<>();
    public void addBook(String isbn, String title, String author, int year) throws Exception {
        if(isbn.isEmpty() || title.isEmpty() || author.isEmpty()){
            throw new Exception("Details cannot be empty");
        }
        if(books.containsKey(isbn)){
            throw new Exception("Book already exists");
        }
        Book book = new Book(isbn, title, author, year);
        books.put(isbn, book);
    }
    public void listAllBooks(){
        if(books.isEmpty()){
            System.out.println("No books available");
            return;
        }
        for(Book book : books.values()){
            System.out.println(book);
        }
    }
    public void findByIsbn(String isbn) throws Exception{
        Book book = books.get(isbn);
        if(book == null){
            throw new Exception("Book not found");
        }
        System.out.println(book);
    }
    public void searchByTitle(String title){
        boolean found = false;
        for(Book book : books.values()){
            if(book.getTitle().toLowerCase().contains(title.toLowerCase())){
                System.out.println(book);
                found = true;
            }
        }
        if(!found){
            System.out.println("No book found");
        }
    }
    public void updateBook(String isbn, String title, String author, int year) throws Exception{
        Book book = books.get(isbn);
        if(book == null){
            throw new Exception("Book not found");
        }
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublicationYear(year);
        System.out.println("Book updated successfully");
    }
    public void deleteBook(String isbn) throws Exception{
        if(!books.containsKey(isbn)){
            throw new Exception("Book not found");
        }
        books.remove(isbn);
        System.out.println("Book deleted successfully");
    }
}