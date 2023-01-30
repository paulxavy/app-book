package com.distribuida.servicios;

import com.distribuida.db.Book;
import io.helidon.dbclient.DbClient;
import io.helidon.dbclient.DbRow;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
public class BookServiceImpl implements BookService {
    @Inject
    DbClient dbClient;
    @Override
    public Book findById(Integer id) throws ExecutionException, InterruptedException {

        Book  book =
                dbClient.execute(dbExecute -> dbExecute.createNamedQuery("book").addParam("id",id)
                .execute())
                .first()
                .map( rs -> Book
                        .builder()
                        .id(rs.column("id").as(Integer.class))
                        .author_id(rs.column("author_id").as(Integer.class))
                        .isbn(rs.column("isbn").as(String.class))
                        .title(rs.column("title").as(String.class))
                        .price(rs.column("price").as(BigDecimal.class))
                        .build())
                        .get();
        return book;
    }

    @Override
    public List<Book> findAll() throws ExecutionException, InterruptedException {
        List<Book> books = new ArrayList<>();
        List<DbRow> persist ;
         persist = dbClient.execute(dbExecute -> dbExecute.createNamedQuery("all-book").execute()
                 ).collectList()
                 .get();
                    for(DbRow row:persist) {
                        Book book = new Book();
                        book.setId(row.column("id").as(Integer.class));
                        book.setAuthor_id(row.column("author_id").as(Integer.class));
                        book.setIsbn(row.column("isbn").as(String.class));
                        book.setTitle(row.column("title").as(String.class));
                        book.setPrice(row.column("price").as(BigDecimal.class));
                        books.add(book);
                    }
                    return books;
    }

    @Override
    public void delete(Integer id) throws ExecutionException, InterruptedException {
        dbClient.execute(dbExecute -> dbExecute.createDelete("DELETE FROM books WHERE id=:id").addParam("id",id).execute()).get();


    }

    @Override
    public void create(Book b) throws ExecutionException, InterruptedException {
        dbClient.execute(dbExecute -> dbExecute.createUpdate("INSERT INTO books(isbn,title,author_id,price) values (:isbn,:title,:author_id,:price)")
                .addParam("isbn",b.getIsbn())
                .addParam("title",b.getTitle())
                .addParam("author_id", b.getAuthor_id())
                .addParam("price",b.getPrice())
                .execute()).get();

    }

    @Override
    public void update(Book b) throws ExecutionException, InterruptedException {
        dbClient.execute(dbExecute -> dbExecute.createUpdate("UPDATE books set isbn=:isbn,title=:title,author_id=:author_id,price=:price WHERE id=:id")
                .addParam("isbn",b.getIsbn())
                .addParam("title",b.getTitle())
                .addParam("author_id", b.getAuthor_id())
                .addParam("price",b.getPrice())
                .addParam("id",b.getId())
                .execute()).get();

    }

}

