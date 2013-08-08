package com.github.windbender.dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.windbender.core.Book;

/**
 * A very noddy implementation of the {@see BookRepository} interface.
 */
public class DummyBookRepository implements BookRepository {
  private final Map<Integer, Book> books = new ConcurrentHashMap<Integer, Book>();

  @Override
  public Book findById(Integer id) {
    return this.books.get(id);
  }

  @Override
  public List<Book> findAll() {
    List<Book> books = new ArrayList<Book>(this.books.values());
    Collections.sort(books, new Comparator<Book>() {
      @Override
      public int compare(Book o1, Book o2) {
        return o1.getId() - o2.getId();
      }
    });
    return books;
  }

  @Override
  public Book save(Book book) {
    if (book.getId() == null) {
      book.setId(nextId());
    }
    this.books.put(book.getId(), book);
    return book;
  }

  @Override
  public void delete(Integer id) {
    this.books.remove(id);
  }

  private Integer nextId() {
    if (this.books.isEmpty()) {
      return 1;
    } else {
      return Collections.max(this.books.keySet()) + 1;
    }
  }
}
