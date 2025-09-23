package com.example.graphqlserver.controller;

import com.example.graphqlserver.dto.input.AddAuthorInput;
import com.example.graphqlserver.dto.input.AddBookInput;
import com.example.graphqlserver.dto.output.AddAuthorPayload;
import com.example.graphqlserver.dto.output.AddBookPayload;
import com.example.graphqlserver.dto.output.UpdateAuthorFirstNamePayload;
import com.example.graphqlserver.model.Author;
import com.example.graphqlserver.model.Book;
import com.example.graphqlserver.repository.AuthorRepository;
import com.example.graphqlserver.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MutationController {

    private final AuthorRepository authorRepo;
    private final BookRepository bookRepo;

    public MutationController(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepo = authorRepository;
        this.bookRepo = bookRepository;
    }

    @MutationMapping
    public AddAuthorPayload addAuthor(@Argument AddAuthorInput input) {
        Author a = new Author(null, input.firstName(), input.lastName(), new ArrayList<>());
        return new AddAuthorPayload(authorRepo.save(a));
    }

    @MutationMapping
    public AddBookPayload addBook(@Argument AddBookInput input) {
        Author author = authorRepo.findById(input.authorId()).orElse(null);
        if (author == null) return new AddBookPayload(null);
        Book b = new Book(input.isbn(), input.title(), author);
        return new AddBookPayload(bookRepo.save(b));
    }

    // Task 3 (Activity 2): update first name, return oldFirstName
    @MutationMapping
    public UpdateAuthorFirstNamePayload updateAuthorFirstName(@Argument Integer id, @Argument String newFirstName) {
        Author a = authorRepo.findById(id).orElse(null);
        if (a == null) return new UpdateAuthorFirstNamePayload(null);
        String old = a.getFirstName();
        a.setFirstName(newFirstName);
        authorRepo.save(a);
        return new UpdateAuthorFirstNamePayload(old);
    }

    // Task 4 (Activity 2): delete book by ISBN, return ISBN or null
    @MutationMapping
    public String deleteBook(@Argument String isbn) {
        if (!bookRepo.existsById(Integer.parseInt(isbn))) return null;
        bookRepo.deleteById(Integer.parseInt(isbn));
        return isbn;
    }
}
