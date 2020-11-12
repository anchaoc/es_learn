package com.example.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.annotation.Documented;
import java.util.Date;
import java.util.Objects;

/**
 * @author anchao
 * @date 2020/11/12 9:36
 **/
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Book {

    private String id;
    private String name;
    private Double price;
    private String author;
    private String des;
    private String content;
    private Date pubDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) &&
                Objects.equals(name, book.name) &&
                Objects.equals(price, book.price) &&
                Objects.equals(author, book.author) &&
                Objects.equals(des, book.des) &&
                Objects.equals(content, book.content) &&
                Objects.equals(pubDate, book.pubDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, author, des, content, pubDate);
    }
}
