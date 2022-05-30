package ru.inno.game.repositories;

import ru.inno.game.models.Game;
import ru.inno.game.models.Player;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;

public class GamesRepositoryJdbcImpl implements GamesRepository {
    //language=SQL
    private static final String SQL_FIND_BY_ID = "select * from game where id = ?";
    //language=SQL
    private static final String SQL_INSERT_GAME = "insert into game (datatime, player_first, player_second, " +
            "player_first_shot_count,player_second_shot_count,seconds_game_time_amount) " +
            "values (?,?,?,?,?,?)";
    //language=SQL
    private static final String SQL_UPDATE_GAME = "update game set player_first = ?, player_first_shot_count = ?, player_second = ?, player_second_shot_count = ?, seconds_game_time_amount = ?  where id = ?";

    // анонимный класс, реализованный через lambda выражение
    // строку из БД преобразовать в игру
    static private final RowMapper<Game> gameRowMapper = row -> Game.builder()
            .id(row.getLong("id"))
            .dateTime(LocalDateTime.parse(row.getString("dataTime")))
            .playerFirst(Player.builder()
                    .id(row.getLong("player_first"))
                    .build())
            .playerFirstShotCount(row.getInt("player_first_shot_count"))
            .playerSecond(Player.builder()
                    .id(row.getLong("player_second"))
                    .build())
            .playerSecondShotCount(row.getInt("player_second_shot_count"))
            .build();

    // класс GamesRepositoryJdbcImpl зависит от источника данных (CustomDataSource)
    private DataSource dataSource;

    public GamesRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Game game) {
        try (Connection connection = dataSource.getConnection();
             // вернет созданный id(ключи, которые были сгенерированы БД стали доступны во время выполнения программы)
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_GAME, Statement.RETURN_GENERATED_KEYS)) {
            // добавление параметров
            statement.setString(1, game.getDateTime().toString());
            statement.setLong(2, game.getPlayerFirst().getId());
            statement.setLong(3, game.getPlayerSecond().getId());
            statement.setInt(4, game.getPlayerFirstShotCount());
            statement.setInt(5, game.getPlayerSecondShotCount());
            statement.setLong(6, game.getSecondsGameTimeAmount());

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
                    game.setId(id);
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
    public Game findById(Long gameId) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_ID)) {
            // добавление параметра
            statement.setLong(1, gameId);

            try (ResultSet rows = statement.executeQuery()) {
                if (rows.next()) {
                    return gameRowMapper.mapRow(rows);
                }
            }
            // если ничего не найдено
            return null;

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void update(Game game) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_UPDATE_GAME)) {
            // добавление параметров
            statement.setLong(1, game.getPlayerFirst().getId());
            statement.setInt(2, game.getPlayerFirstShotCount());
            statement.setLong(3, game.getPlayerSecond().getId());
            statement.setInt(4, game.getPlayerSecondShotCount());
            statement.setLong(5, game.getSecondsGameTimeAmount());
            statement.setLong(6, game.getId());

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
