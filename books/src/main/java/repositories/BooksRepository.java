package repositories;

import models.Book;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BooksRepository implements CrudRepository<Book> {

    //language=SQL
    private static final String SQL_SAVE_BOOK = "INSERT INTO books (title, author_id, genre_id) VALUES (?, ?, ?)";
    //language=SQL
    private static final String SQL_FIND_BY_ID = "SELECT b.book_id, b.title, " +
            "concat_ws(' ', a.name, a.surname) AS author, g.title AS genre \n" +
            "FROM books b\n" +
            "LEFT JOIN authors a\n" +
            "ON a.author_id = b.author_id \n" +
            "LEFT JOIN genres g\n" +
            "ON g.genre_id = b.genre_id\n" +
            "WHERE book_id = ?";
    //language=SQL
    private static final String SQL_UPDATE_BOOK = "UPDATE books SET title = ?, author_id = ?, genre_id = ?  WHERE book_id = ?";
    //language=SQL
    private static final String SQL_DELETE_BOOK = "DELETE FROM books WHERE book_id = ?";
    //language=SQL
    private static final String SQL_SELECT_ALL_BOOKS = "SELECT  books.book_id, books.title, \n" +
            "(SELECT  concat_ws(' ', authors.name, authors.surname) FROM authors\n" +
            "WHERE authors.author_id = books.author_id) AS author,\n" +
            "(SELECT genres.title FROM genres\n" +
            "WHERE genres.genre_id = books.genre_id) AS genre \n" +
            "FROM books";
    //language=SQL
    private static final String SQL_GET_LAST_ID = "SELECT book_id FROM books ORDER BY book_id DESC";
    //language=SQL
    private static final String SQL_FIND_BY_AUTHOR_ID = "SELECT book_id, title FROM books WHERE author_id = ?";
    //language=SQL
    private static final String SQL_FIND_BY_GENRE_ID = "SELECT book_id, title FROM books WHERE genre_id = ?";
    //language=SQL
    private static final String SQL_FIND_COUNT_BOOKS_BY_AUTHOR_ID = "SELECT count(book_id) FROM books WHERE author_id = ?";
    //language=SQL
    private static final String SQL_FIND_COUNT_BOOKS_BY_GENRE_ID = "SELECT count(book_id) FROM books WHERE genre_id = ?";

    // преобразовать строку из бд в объект
    static final RowMapper<Book> bookRowMapper = row -> new Book(
            row.getInt("book_id"),
            row.getString("title"),
            row.getString("author"),
            row.getString("genre")
    );

    // преобразовать строку из бд в объект
    static final RowMapper<Book> booksByAuthorRowMapper = row -> new Book(
            row.getInt("book_id"),
            row.getString("title")
    );

    // у DataSource будет запрошено соединение
    private DataSource dataSource;

    public BooksRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // найти все книги
    @Override
    public List<Book> findAll() {
        // список для объектов из бд
        List<Book> books = new ArrayList<>();
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_BOOKS)) {
            // отправить запрос
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // вывод результата
                while (resultSet.next()) {
                    // преобразовать полученный результат в объект
                    Book book = bookRowMapper.mapRow(resultSet);
                    // добавить объект в список
                    books.add(book);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        // вернуть список полученных из бд объектов
        return books;
    }

    // добавить новую книгу
    @Override
    public void save(Book model) {
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_BOOK)) {
            // добавление параметров, которые будут переданы в бд и сохранены
            preparedStatement.setString(1, model.getTitle());
            preparedStatement.setInt(2, model.getAuthor_id());
            preparedStatement.setInt(3, model.getGenre_id());

            // в случае однократного добавления это число равно одному
            // отправить запрос в бд
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows != 1) {
                // некорректоное добавление
                throw new SQLException("Некорректное добавление данных");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        System.out.println("Добавлен " + model.getTitle());
    }

    // найти книгу по id
    @Override
    public Book findById(int id) {
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            // добавление параметра, по которому будет поиск
            preparedStatement.setInt(1, id);
            // отправить запрос
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // вывод результата
                if (resultSet.next()) {
                    // преобразовать полученный результат в объект
                    return bookRowMapper.mapRow(resultSet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // если книга не найдена
            return null;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // изменить книгу
    @Override
    public void update(Book model) {
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_BOOK)) {
            // добавление параметров, которые будут обновлены
            preparedStatement.setString(1, model.getTitle());
            preparedStatement.setInt(2, model.getAuthor_id());
            preparedStatement.setInt(3, model.getGenre_id());
            // параметр, по которому искать
            preparedStatement.setInt(4, model.getBook_id());
            // в случае однократного добавления это число равно одному
            // отправить запрос в бд
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows != 1) {
                // некорректоное добавление
                throw new SQLException("Некорректное добавление данных");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        System.out.println("Обновление прошло успешно");
    }

    // удалить книгу
    @Override
    public void remove(int id) {
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_BOOK)) {
            // параметр, по которому искать
            preparedStatement.setInt(1, id);
            // в случае однократного добавления это число равно одному
            // отправить запрос в бд
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows != 1) {
                // некорректоное добавление
                throw new SQLException("Некорректное удаление данных");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        System.out.println("Удаление прошло успешно");
    }

    // получить последний id из БД
    public int getLastId() {
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_LAST_ID)) {
            // отправить запрос
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // вывод результата
                if (resultSet.next()) {
                    return resultSet.getInt("book_id");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // найти книги по id автора
    public List<Book> findByAuthorId(int id) {
        // список для объектов из бд
        List<Book> books = new ArrayList<>();
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_AUTHOR_ID)) {
            // добавление параметра, по которому будет поиск
            preparedStatement.setInt(1, id);
            // отправить запрос
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // вывод результата
                while (resultSet.next()) {
                    // преобразовать полученный результат в объект
                    Book book = booksByAuthorRowMapper.mapRow(resultSet);
                    // добавить объект в список
                    books.add(book);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        // вернуть список полученных из бд объектов
        return books;
    }

    // найти книги по id жанра
    public List<Book> findByGenreId(int id) {
        // список для объектов из бд
        List<Book> books = new ArrayList<>();
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_BY_GENRE_ID)) {
            // добавление параметра, по которому будет поиск
            preparedStatement.setInt(1, id);
            // отправить запрос
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // вывод результата
                while (resultSet.next()) {
                    // преобразовать полученный результат в объект
                    Book book = booksByAuthorRowMapper.mapRow(resultSet);
                    // добавить объект в список
                    books.add(book);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        // вернуть список полученных из бд объектов
        return books;
    }

    // получить количество книг автора
    public int getCountBooksByAuthorId(int id) {
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_COUNT_BOOKS_BY_AUTHOR_ID)) {
            // добавление параметра, по которому будет поиск
            preparedStatement.setInt(1, id);
            // отправить запрос
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // вывод результата
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // получить количество книг по жанру
    public int getCountBooksByGenreId(int id) {
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_COUNT_BOOKS_BY_GENRE_ID)) {
            // добавление параметра, по которому будет поиск
            preparedStatement.setInt(1, id);
            // отправить запрос
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // вывод результата
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

}


