package com.lovelycatv.arkcache.related;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookDAO {
  public static List<Book> innerBooks = new ArrayList<>();
  static {
    innerBooks.add(new Book(1L,"Book1","Description of book1",new String[]{"Alan"},"2021-12-02",4.9f));
    innerBooks.add(new Book(2L,"Book2","Description of book2",new String[]{"Jim"},"2022-02-20",4.8f));
    innerBooks.add(new Book(3L,"Book3","Description of book3",new String[]{"Jason"},"2021-11-09",4.9f));
  }

  public Book byId(Long id) {
    for (Book book : innerBooks) {
      if (Objects.equals(book.getId(), id)) {
        return book;
      }
    }
    return null;
  }

  public List<Book> list() {
    return innerBooks;
  }
}
