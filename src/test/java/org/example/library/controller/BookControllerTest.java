package org.example.library.controller;

import org.example.library.model.Book;
import org.example.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {

    @MockitoBean
    private BookService bookService;
    private Long count;

    @LocalServerPort
    private int port;
    private RestClient restClient;

    @BeforeEach
    void beforeEach() {
        restClient = RestClient.create("http://localhost:" + port);
        count = 0L;
    }

    @Test
    void testGetByIdOk() {
        Book book = getBook();

        doReturn(book).when(bookService).getById(anyLong());

        ResponseEntity<Book> actual = restClient.get()
                .uri("/books/" + book.getId())
                .retrieve()
                .toEntity(Book.class);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        Book body = actual.getBody();
        assertNotNull(body);
        assertEquals(book.getId(), body.getId());
    }

    @Test
    void testOkGetAll() {
        Book book = getBook();
        Book book1 = getBook();

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book);

        doReturn(books).when(bookService).getAll();

        assertEquals(books.size(), restClient.get()
                .uri("/books")
                .retrieve()
                .toEntity(List.class)
                .getBody()
                .size());
    }

    @Test
    void testDeleteByIdAllOk() {
        doNothing().when(bookService).deleteById(anyLong());

        ResponseEntity<Void> bodilessEntity = restClient.delete()
                .uri("/books/" + 1L)
                .retrieve()
                .toBodilessEntity();

        assertEquals(HttpStatus.NO_CONTENT, bodilessEntity.getStatusCode());
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
