package repositories;

import models.Author;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorsRepository implements CrudRepository<Author> {

    //language=SQL
    private static final String SQL_SAVE_AUTHOR = "INSERT INTO authors (surname, name) VALUES (?, ?)";
    //language=SQL
    private static final String SQL_FIND_BY_ID = "SELECT * FROM authors WHERE author_id = ?";
    //language=SQL
    private static final String SQL_UPDATE_AUTHOR = "UPDATE authors SET surname = ?, name = ? WHERE author_id = ?";
    //language=SQL
    private static final String SQL_DELETE_AUTHOR = "DELETE FROM authors WHERE author_id = ?";
    //language=SQL
    private static final String SQL_SELECT_ALL_AUTHORS = "SELECT * FROM authors ORDER BY author_id ASC";
    //language=SQL
    private static final String SQL_GET_LAST_ID = "SELECT author_id FROM authors ORDER BY author_id DESC";

    // преобразовать строку из бд в объект
    static final RowMapper<Author> authorRowMapper = row -> new Author(
            row.getInt("author_id"),
            row.getString("surname"),
            row.getString("name")
    );

    // у DataSource будет запрошено соединение
    private DataSource dataSource;

    public AuthorsRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // найти всех авторов
    @Override
    public List<Author> findAll() {
        // список для объектов из бд
        List<Author> authors = new ArrayList<>();
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_AUTHORS)) {
            // отправить запрос
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // вывод результата
                while (resultSet.next()) {
                    // преобразовать полученный результат в объект
                    Author author = authorRowMapper.mapRow(resultSet);
                    // добавить объект в список
                    authors.add(author);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        // вернуть список полученных из бд объектов
        return authors;
    }

    // добавить нового автора
    @Override
    public void save(Author model) {
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_AUTHOR)) {
            // добавление параметров, которые будут переданы в бд и сохранены
            preparedStatement.setString(1, model.getSurname());
            preparedStatement.setString(2, model.getName());

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
        System.out.println("Добавлен " + model.getSurname() + " " + model.getName());
    }

    // найти автора по id
    @Override
    public Author findById(int id) {
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
                    return authorRowMapper.mapRow(resultSet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // если автор не найден
            return null;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // изменить автора
    @Override
    public void update(Author model) {
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_AUTHOR)) {
            // добавление параметров, которые будут обновлены
            preparedStatement.setString(1, model.getSurname());
            preparedStatement.setString(2, model.getName());
            // параметр, по которому искать
            preparedStatement.setInt(3, model.getAuthor_id());
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

    // удалить автора
    @Override
    public void remove(int id) {
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_AUTHOR)) {
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
                    return resultSet.getInt("author_id");
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
