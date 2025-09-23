package com.example.graphqlserver.controller;

import com.example.graphqlserver.dto.input.AddBookInput;
import com.example.graphqlserver.dto.output.AddBookPayload;
import com.example.graphqlserver.model.Author;
import com.example.graphqlserver.model.Book;
import com.example.graphqlserver.repository.AuthorRepository;
import com.example.graphqlserver.repository.BookRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class QueryController {

    private final BookRepository bookRepo;
    private final AuthorRepository authorRepo;


    public QueryController(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepo = bookRepository;
        this.authorRepo = authorRepository;
    }

    @QueryMapping
    public List<Author> authors() { return authorRepo.findAll(); }

    @QueryMapping
    public Author authorById(@Argument Integer id) { return authorRepo.findById(id).orElse(null); }

    @QueryMapping
    public List<Author> authorsByLastName(@Argument String lastName) { return authorRepo.findByLastName(lastName); }

    @QueryMapping
    public List<Book> books() { return bookRepo.findAll(); }

    @QueryMapping
    public Book bookByISBN(@Argument int isbn) { return bookRepo.findById(isbn).orElse(null); }

    // Task 1 - JPA edition
    @QueryMapping
    public List<Book> booksByAuthorId(@Argument Integer authorId) {
        return bookRepo.findByAuthor_Id(authorId);
    }

    // Task 5 - JPA edition
    @QueryMapping
    public List<String> bookTitlesByAuthorFirstName(@Argument String firstName) {
        return authorRepo.findByFirstName(firstName).stream()
                .flatMap(a -> a.getBooks().stream())
                .map(Book::getTitle)
                .toList();
    }


}
