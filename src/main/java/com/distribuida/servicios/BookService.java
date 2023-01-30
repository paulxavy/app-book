package com.distribuida.servicios;

import com.distribuida.db.Book;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface BookService {
    Book findById(Integer id) throws ExecutionException, InterruptedException;

    List<Book> findAll() throws ExecutionException, InterruptedException;

    void delete(Integer id) throws ExecutionException, InterruptedException;

    void create(Book b) throws ExecutionException, InterruptedException;

    void update(Book obj) throws ExecutionException, InterruptedException;


}
