package org.example.library.service;

import org.example.library.exception.NotFoundException;
import org.example.library.model.Book;
import org.example.library.repository.BookRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BookServiceTest {

    @Autowired
    private BookService bookService;
    @Autowired
    private BookRepository bookRepository;

    private Long count;

    private final static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.8-alpine3.18");

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @BeforeEach
    void BeforeEach() {
        bookRepository.deleteAll();
        count = 0L;
    }

    @Test
    void testGetByIdOk() {
        Book book = getBook();

        bookRepository.save(book);

        Book actual = bookService.getById(book.getId());

        assertEquals(book.getId(), actual.getId());
        assertEquals(book.getName(), actual.getName());
        assertEquals(book.getDate(), actual.getDate());
        assertEquals(book.getGenre(), actual.getGenre());
    }

    @Test
    void testGetByIdExpectedException() {
        assertThrows(NotFoundException.class, () -> bookService.getById(1L));
    }

    @Test
    void testGetAllOk() {
        Book book = getBook();

        Book book1 = getBook();

        bookRepository.save(book);
        bookRepository.save(book1);

        List<Book> bookList = bookService.getAll();

        assertEquals(bookList.size(), 2);
    }

    @Test
    void testDeleteByIdAllOk() {
        Book book = getBook();

        Book actual = bookRepository.save(book);

        bookService.deleteById(actual.getId());

        assertTrue(bookRepository.findById(actual.getId()).isEmpty());
    }

    private Book getBook() {
        count++;
        Book book = new Book();
        book.setId(count);
        book.setName("King" + count);
        book.setDate("04.07.1985");
        book.setGenre("asd" + count);

        return book;
    }
}
