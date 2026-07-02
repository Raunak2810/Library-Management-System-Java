package com.raunak.library.service;
import com.raunak.library.model.Book;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
//  Case-insensitive substring match against the book title
public class TitleSearchStrategy implements SearchStrategy{
    @Override
    public List<Book> search(Collection<Book> books, String query){
        String q = query.toLowerCase();
        return books.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }
}
