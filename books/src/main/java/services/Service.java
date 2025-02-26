package services;

import models.Author;
import models.Book;
import models.Genre;
import repositories.AuthorsRepository;
import repositories.BooksRepository;
import repositories.GenresRepository;

import java.util.List;

public class Service {

    private AuthorsRepository authorsRepository;
    private GenresRepository genresRepository;
    private BooksRepository booksRepository;

    public Service(AuthorsRepository authorsRepository, GenresRepository genresRepository, BooksRepository booksRepository) {
        this.authorsRepository = authorsRepository;
        this.genresRepository = genresRepository;
        this.booksRepository = booksRepository;
    }

    // найти всех авторов
    public List<Author> findAllAuthors() {
        return authorsRepository.findAll();
    }

    // добавить нового автора
    public void saveAuthor(String surname, String name) {
        authorsRepository.save(new Author(surname, name));
    }

    // найти автора по id
    public String findAuthorById(int id) {
        Author author = authorsRepository.findById(id);
        return author.getAuthor_id() + " " + author.getName() + " " + author.getSurname();
    }

    // изменить автора
    public void updateAuthor(int id, String surname, String name) {
        authorsRepository.update(new Author(id, surname, name));
    }

    // удалить автора
    public void removeAuthor(int id) {
        authorsRepository.remove(id);
    }

    // получить последний id из БД
    public int getLastIdAuthor() {
        return authorsRepository.getLastId();
    }

    // найти все жанры
    public List<Genre> findAllGenres() {
        return genresRepository.findAll();
    }

    // добавить новый жанр
    public void saveGenre(String title) {
        genresRepository.save(new Genre(title));
    }

    // найти жанр по id
    public String findGenreById(int id) {
        Genre genre = genresRepository.findById(id);
        return genre.getGenre_id() + " " + genre.getTitle();
    }

    // изменить жанр
    public void updateGenre(int id, String title) {
        genresRepository.update(new Genre(id, title));
    }

    // удалить жанр
    public void removeGenre(int id) {
        genresRepository.remove(id);
    }

    // получить последний id из БД
    public int getLastIdGenre() {
        return genresRepository.getLastId();
    }

    // найти все названия жанров
    public List<String> getAllTitlesGenres() {
        return genresRepository.getAllTitles();
    }

    // найти все книги
    public List<Book> findAllBooks() {
        return booksRepository.findAll();
    }

    // добавить новую книгу
    public void saveBook(String title, int author_id, int genre_id) {
        booksRepository.save(new Book(title, author_id, genre_id));
    }

    // найти книгу по id
    public String findBookById(int id) {
        Book book = booksRepository.findById(id);
        return book.getBook_id() + " " + book.getTitle() + " " + book.getAuthor() + " " + book.getGenre();
    }

    // изменить книгу: название, автора, жанр
    public void updateBook(int id, String title, int author_id, int genre_id) {
        booksRepository.update(new Book(id, title, author_id, genre_id));
    }

    // удалить книгу
    public void removeBook(int id) {
        booksRepository.remove(id);
    }

    // получить последний id из БД
    public int getLastIdBook() {
        return booksRepository.getLastId();
    }

    // найти книги по id автора
    public List<Book> findBooksByAuthorId(int id) {
        return booksRepository.findByAuthorId(id);
    }

    // найти книги по id жанра
    public List<Book> findBooksByGenreId(int id) {
        return booksRepository.findByGenreId(id);
    }

    // получить количество книг автора
    public int getCountBooksByAuthorId(int id) {
        return booksRepository.getCountBooksByAuthorId(id);
    }

    // получить количество книг по жанру
    public int getCountBooksByGenreId(int id) {
        return booksRepository.getCountBooksByGenreId(id);
    }

}
