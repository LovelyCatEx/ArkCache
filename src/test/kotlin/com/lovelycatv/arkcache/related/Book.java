package com.lovelycatv.arkcache.related;

import java.util.Arrays;

public class Book {
  private Long id;
  private String bookName;
  private String description;
  private String[] author;
  private String publishedTime;
  private float rates;

  public Book(Long id, String bookName, String description, String[] author, String publishedTime, float rates) {
    this.id = id;
    this.bookName = bookName;
    this.description = description;;
    this.author = author;
    this.publishedTime = publishedTime;
    this.rates = rates;
  }

  public float getRates() {
    return rates;
  }

  public Long getId() {
    return id;
  }

  public void setAuthor(String[] author) {
    this.author = author;
  }

  public void setBookName(String bookName) {
    this.bookName = bookName;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPublishedTime(String publishedTime) {
    this.publishedTime = publishedTime;
  }

  public void setRates(float rates) {
    this.rates = rates;
  }

  public String getBookName() {
    return bookName;
  }

  public String getDescription() {
    return description;
  }

  public String[] getAuthor() {
    return author;
  }

  public String getPublishedTime() {
    return publishedTime;
  }

  @Override
  public String toString() {
    final String[] tmp_author = {""};
    Arrays.stream(author).forEach(s -> tmp_author[0] += s + ",");
    tmp_author[0] = tmp_author[0].substring(0, tmp_author[0].length() - 1);
    return String.format("id:%s, title: %s: desc: %s, author: %s, published: %s, rates: %s", id, bookName, description, tmp_author[0], publishedTime, rates);
  }
}
