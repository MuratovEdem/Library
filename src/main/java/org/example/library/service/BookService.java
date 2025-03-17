package org.example.library.service;

import org.example.library.exception.NotFoundException;
import org.example.library.model.Book;
import org.example.library.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Book getById(Long id) {
        Optional<Book> bookOpt = bookRepository.findById(id);
        return bookOpt.orElseThrow(NotFoundException::new);
    }

    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    public Book create(Book book) {
        return bookRepository.save(book);
    }

    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}

