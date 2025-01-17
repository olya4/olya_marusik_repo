package models;

import java.util.Objects;

public class Book {
    private Integer book_id;
    private String title;
    private String author;
    private String genre;
    private Integer author_id;
    private Integer genre_id;

    public Book(String title, Integer author_id, Integer genre_id) {
        this.title = title;
        this.author_id = author_id;
        this.genre_id = genre_id;
    }

    public Book(Integer book_id, String title, String author, String genre) {
        this.book_id = book_id;
        this.title = title;
        this.author = author;
        this.genre = genre;
    }

    public Book(Integer book_id, String title, Integer author_id, Integer genre_id) {
        this.book_id = book_id;
        this.title = title;
        this.author_id = author_id;
        this.genre_id = genre_id;
    }

    public Book(Integer book_id, String title) {
        this.book_id = book_id;
        this.title = title;
    }


    public Integer getBook_id() {
        return book_id;
    }

    public void setBook_id(Integer book_id) {
        this.book_id = book_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Integer getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(Integer author_id) {
        this.author_id = author_id;
    }

    public Integer getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(Integer genre_id) {
        this.genre_id = genre_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(book_id, book.book_id) && Objects.equals(title, book.title) && Objects.equals(author, book.author) && Objects.equals(genre, book.genre) && Objects.equals(author_id, book.author_id) && Objects.equals(genre_id, book.genre_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book_id, title, author, genre, author_id, genre_id);
    }

    @Override
    public String toString() {
        return "Book{" +
                +book_id +
                ", название ='" + title + '\'' +
                ", автор ='" + author + '\'' +
                ", жанр ='" + genre + '\'' +
                '}';
    }

}
