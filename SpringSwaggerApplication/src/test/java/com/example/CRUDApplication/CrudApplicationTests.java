package com.example.CRUDApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.CRUDApplication.controller.BookController;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.CRUDApplication.model.Book;
import com.example.CRUDApplication.repo.BookRepo;

@SpringBootTest
public class CrudApplicationTests {

	@InjectMocks
	private BookController bookController;

	@Mock
	private BookRepo bookRepo;

	@Test
	public void testGetAllBooks() {
		List<Book> bookList = new ArrayList<>();
		bookList.add(new Book(1L, "Book1", "Author1"));
		bookList.add(new Book(2L, "Book2", "Author2"));
		when(bookRepo.findAll()).thenReturn(bookList);
		ResponseEntity<List<Book>> response = bookController.getAllBooks();
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(bookList, response.getBody());
	}

	@Test
	public void testGetBookById() {
		Book book = new Book(1L, "Book1", "Author1");
		when(bookRepo.findById(1L)).thenReturn(Optional.of(book));
		ResponseEntity<Book> response = bookController.getBookById(1L);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(book, response.getBody());
	}

	@Test
	public void testGetBookByIdNoContent() {
		when(bookRepo.findById(1L)).thenReturn(Optional.empty());
		ResponseEntity<Book> response = bookController.getBookById(1L);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	public void testAddBook() {
		Book book = new Book(1L, "Book1", "Author1");
		when(bookRepo.save(book)).thenReturn(book);
		ResponseEntity<Book> response = bookController.addBook(book);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(book, response.getBody());
	}

	@Test
	public void testUpdateBookById() {
		Book oldBook = new Book(1L, "Book1", "Author1");
		Book newBook = new Book(1L, "Book1_updated", "Author1_updated");
		when(bookRepo.findById(1L)).thenReturn(Optional.of(oldBook));
		when(bookRepo.save(oldBook)).thenReturn(newBook);
		ResponseEntity<Book> response = bookController.updateBookById(1L, newBook);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(newBook, response.getBody());
	}

	@Test
	public void testUpdateBookByIdNoContent() {
		Book newBook = new Book(1L, "Book1_updated", "Author1_updated");
		when(bookRepo.findById(1L)).thenReturn(Optional.empty());
		ResponseEntity<Book> response = bookController.updateBookById(1L, newBook);
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}

	@Test
	public void testDeleteBookById() {
		ResponseEntity<HttpStatus> response = bookController.deleteBookById(1L);
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}
}
