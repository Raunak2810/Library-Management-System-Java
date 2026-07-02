package com.raunak.library.repository;
import com.raunak.library.model.Book;
import com.raunak.library.util.AppLogger;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Logger;
// CSV-file backed implementation of BookRepository. Loads all records into an in-memory LinkedHashMap on startup for fast lookups, and flushes the full map back to disk after every mutating operation so the file never gets out of sync with the running application.
public class CsvBookRepository implements BookRepository{
    private static final Logger log = AppLogger.getLogger();
    private static final String HEADER = "isbn,title,author,publicationYear";
    private final Path csvPath;
    private final Map<String, Book> books = new LinkedHashMap<>();
    public CsvBookRepository(String csvFilePath){
        this.csvPath = Path.of(csvFilePath);
        load();
    }
    private void load(){
        if (!Files.exists(csvPath)){
            log.info("No existing data file at " + csvPath + " - starting with an empty library.");
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)){
            String line = reader.readLine(); // skip header
            int lineNum = 1;
            while ((line = reader.readLine()) != null){
                lineNum++;
                if (line.isBlank()) continue;
                try{
                    String[] fields = parseCsvLine(line);
                    Book book = new Book(fields[0], fields[1], fields[2], Integer.parseInt(fields[3]));
                    books.put(book.getIsbn(), book);
                } catch (Exception parseEx){
                    log.warning("Skipping malformed CSV row " + lineNum + ": " + line);
                }
            }
            log.info("Loaded " + books.size() + " book(s) from " + csvPath);
        } catch (IOException e){
            log.severe("Failed to read data file " + csvPath + ": " + e.getMessage());
        }
    }
    @Override
    public void flush(){
        try{
            Path parent = csvPath.toAbsolutePath().getParent();
            if (parent != null) Files.createDirectories(parent);
            try (BufferedWriter writer = Files.newBufferedWriter(csvPath, StandardCharsets.UTF_8)){
                writer.write(HEADER);
                writer.newLine();
                for (Book b : books.values()){
                    writer.write(b.toCsvRow());
                    writer.newLine();
                }
            }
            log.fine("Persisted " + books.size() + " book(s) to " + csvPath);
        } catch (IOException e) {
            log.severe("Failed to write data file " + csvPath + ": " + e.getMessage());
        }
    }
    @Override
    public void save(Book book){
        books.put(book.getIsbn(), book);
        flush();
    }
    @Override
    public Optional<Book> findByIsbn(String isbn){
        return Optional.ofNullable(books.get(isbn));
    }
    @Override
    public Collection<Book> findAll(){
        return Collections.unmodifiableCollection(books.values());
    }
    @Override
    public boolean existsByIsbn(String isbn){
        return books.containsKey(isbn);
    }
    @Override
    public void deleteByIsbn(String isbn){
        books.remove(isbn);
        flush();
    }
    // Minimal CSV line parser that understands double-quoted, comma-escaped fields.
    private String[] parseCsvLine(String line){
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"' && i + 1 < line.length() && line.charAt(i + 1) == '"'){
                    current.append('"');
                    i++;
                } else if (c == '"'){
                    inQuotes = false;
                } else {
                    current.append(c);
                }
            } else {
                if (c == '"'){
                    inQuotes = true;
                } else if (c == ','){
                    fields.add(current.toString());
                    current.setLength(0);
                } else{
                    current.append(c);
                }
            }
        }
        fields.add(current.toString());
        return fields.toArray(new String[0]);
    }
}
