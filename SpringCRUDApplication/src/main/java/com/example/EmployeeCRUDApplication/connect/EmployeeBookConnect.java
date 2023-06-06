package com.example.EmployeeCRUDApplication.connect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class EmployeeBookConnect {

    private static WebClient webClient;

    private static final String BOOK_MS_URL = "http://localhost:9090/";

    @Autowired
    public EmployeeBookConnect(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<DefineBook> fetchBookDetails(int bookId) {
        return webClient.get()
                .uri(BOOK_MS_URL + "getBookById/" + bookId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> Mono.error(new BookNotFoundException("Book not found with id " + bookId)))
                .bodyToMono(DefineBook.class);
    }

    public static class BookNotFoundException extends RuntimeException {
        public BookNotFoundException(String message) {
            super(message);
        }
    }
}
