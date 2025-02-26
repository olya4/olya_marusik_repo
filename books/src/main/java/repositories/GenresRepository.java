package repositories;

import models.Genre;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenresRepository implements CrudRepository<Genre> {

    //language=SQL
    private static final String SQL_SAVE_GENRE = "INSERT INTO genres (title) VALUES (?)";
    //language=SQL
    private static final String SQL_FIND_BY_ID = "SELECT * FROM genres WHERE genre_id = ?";
    //language=SQL
    private static final String SQL_UPDATE_GENRE = "UPDATE genres SET title = ? WHERE genre_id = ?";
    //language=SQL
    private static final String SQL_DELETE_GENRE = "DELETE FROM genres WHERE genre_id = ?";
    //language=SQL
    private static final String SQL_SELECT_ALL_GENRES = "SELECT * FROM genres ORDER BY genre_id ASC";
    //language=SQL
    private static final String SQL_GET_LAST_ID = "SELECT genre_id FROM genres ORDER BY genre_id DESC";
    //language=SQL
    private static final String SQL_GET_ALL_TITLES = "SELECT title FROM genres ORDER BY title ASC";

    // преобразовать строку из бд в объект
    static final RowMapper<Genre> genreRowMapper = row -> new Genre(
            row.getInt("genre_id"),
            row.getString("title")
    );

    // у DataSource будет запрошено соединение
    private DataSource dataSource;

    public GenresRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // найти все жанры
    @Override
    public List<Genre> findAll() {
        // список для объектов из бд
        List<Genre> genres = new ArrayList<>();
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_ALL_GENRES)) {
            // отправить запрос
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // вывод результата
                while (resultSet.next()) {
                    // преобразовать полученный результат в объект
                    Genre genre = genreRowMapper.mapRow(resultSet);
                    // добавить объект в список
                    genres.add(genre);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        // вернуть список полученных из бд объектов
        return genres;
    }

    // добавить новый жанр
    @Override
    public void save(Genre model) {
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SAVE_GENRE)) {
            // добавление параметров, которые будут переданы в бд и сохранены
            preparedStatement.setString(1, model.getTitle());

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

    // найти жанр по id
    @Override
    public Genre findById(int id) {
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
                    return genreRowMapper.mapRow(resultSet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // если жанр не найден
            return null;
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    // изменить жанр
    @Override
    public void update(Genre model) {
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_UPDATE_GENRE)) {
            // добавление параметров, которые будут обновлены
            preparedStatement.setString(1, model.getTitle());
            // параметр, по которому искать
            preparedStatement.setInt(2, model.getGenre_id());
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

    // удалить жанр
    @Override
    public void remove(int id) {
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_DELETE_GENRE)) {
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
                    return resultSet.getInt("genre_id");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public List<String> getAllTitles() {
        // список для объектов из бд
        List<String> titles = new ArrayList<>();
        // получить соединение с бд
        try (Connection connection = dataSource.getConnection();
             // сформировать запрос в бд
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_GET_ALL_TITLES)) {
            // отправить запрос
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                // вывод результата
                while (resultSet.next()) {
                    // добавить полученный результат в список
                    titles.add(resultSet.getString("title"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
        // вернуть список полученных из бд объектов
        return titles;
    }

}

