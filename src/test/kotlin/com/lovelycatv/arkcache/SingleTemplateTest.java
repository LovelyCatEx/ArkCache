package com.lovelycatv.arkcache;

import com.lovelycatv.arkcache.related.BookDAO;
import com.lovelycatv.arkcache.related.BookService;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SingleTemplateTest {

  private static final BookService bookService = new BookService();
  private static final BookDAO bookDAO = new BookDAO();

  @BeforeAll
  public static void before() {
    System.out.println(">>--------------- Test is starting ---------------<<");
  }

  @AfterAll
  public static void after() {
    System.out.println(">>--------------- Test is ended ---------------<<");
  }

  @Order(1)
  @Test
  public void insertCache() {
    System.out.println("===== Inserting cache");
    bookService.bookMultiCacheTemplate().setExactlyOne(2, bookDAO.byId(1L));
  }

  @Order(2)
  @Test
  public void readCache() {
    System.out.println("===== Reading cache");
    System.out.println(bookService.bookMultiCacheTemplate().getOne(2, 1L));
  }

  @Order(3)
  @Test
  public void removeCache() {
    System.out.println("===== Removing cache");
    bookService.bookMultiCacheTemplate().removeCache(2, 1L);
  }

  @Order(4)
  @Test
  public void readCacheNotExist() {
    System.out.println("===== Reading missed cache");
    System.out.println(bookService.bookMultiCacheTemplate().getExactlyOne(2, 1L));
  }
}
