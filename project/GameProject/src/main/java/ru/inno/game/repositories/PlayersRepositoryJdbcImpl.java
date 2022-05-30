package ru.inno.game.repositories;

import ru.inno.game.models.Player;

import javax.sql.DataSource;
import java.sql.*;

public class PlayersRepositoryJdbcImpl implements PlayersRepository {

    //language=SQL
    private final static String SQL_FIND_PLAYER_BY_NICKNAME = "select * from player where name = ?";

    //language=SQL
    private final static String SQL_INSERT_PLAYER = "insert into player (name, ip) values (?,?)";

    //language=SQL
    private final static String SQL_UPDATE_PLAYER = "update player set ip = ?, points = ?, max_wins_count = ?, max_loses_count = ? where name = ?";

    // класс PlayersRepositoryJdbcImpl зависит от источника данных (CustomDataSource)
    private DataSource dataSource;

    public PlayersRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    // анонимный класс, реализованный через lambda выражение
    // строку из БД преобразовать в player
    private RowMapper<Player> playerRowMapper = row -> Player.builder()
            .id(row.getLong("id"))
            .ip(row.getString("ip"))
            .name(row.getString("name"))
            .points(row.getInt("points"))
            .maxWinsCount(row.getInt("max_wins_count"))
            .maxLosesCount(row.getInt("max_loses_count"))
            .build();

    @Override
    public Player findByNickname(String nickname) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_PLAYER_BY_NICKNAME)) {
            // добавление параметра
            statement.setString(1, nickname);

            try (ResultSet rows = statement.executeQuery()) {
                if (rows.next()) {
                    return playerRowMapper.mapRow(rows);
                }
            }
            // если ничего не найдено
            return null;

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void save(Player player) {
        try (Connection connection = dataSource.getConnection();
             // вернет созданный id(ключи, которые были сгенерированы БД стали доступны во время выполнения программы)
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_PLAYER, Statement.RETURN_GENERATED_KEYS)) {
            // добавление параметров
            statement.setString(1, player.getName());
            statement.setString(2, player.getIp());

            // affectedRows - сколько строк было изменено в БД
            // в случае однократного insert это число равно одному
            // отправить запрос в базу
            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                // некорректное добавление данных
                throw new SQLException("Can't insert");
            }

            // запрос на сгенерированные БД ключи
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                // если есть ключи - взять их
                if (generatedKeys.next()) {
                    // сгенерированный БД id
                    Long id = generatedKeys.getLong("id");
                    // положить id в сохраненного игрока
                    player.setId(id);
                } else {
                    // если ключи не вернулись
                    throw new SQLException("Can't retrieve id");
                }
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void update(Player player) {
        System.out.println("Обновление ip адреса для " + player.getName());
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_PLAYER)) {
            // добавление параметров
            statement.setString(1, player.getIp());
            statement.setInt(2, player.getPoints());
            statement.setInt(3, player.getMaxWinsCount());
            statement.setInt(4, player.getMaxLosesCount());
            statement.setString(5, player.getName());

            // affectedRows - сколько строк было изменено в БД
            // в случае однократного insert это число равно одному
            // отправить запрос в базу
            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                // некорректное добавление данных
                throw new SQLException("Can't update");
            }
        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
