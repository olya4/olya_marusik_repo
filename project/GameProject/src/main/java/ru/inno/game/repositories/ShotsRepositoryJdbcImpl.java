package ru.inno.game.repositories;

import ru.inno.game.models.Game;
import ru.inno.game.models.Player;
import ru.inno.game.models.Shot;

import javax.sql.DataSource;
import javax.swing.plaf.synth.Region;
import java.sql.*;
import java.time.LocalDateTime;

public class ShotsRepositoryJdbcImpl implements ShotsRepository {

    //language=SQL
    private static final String SQL_INSERT_SHOT = "insert into shot (datatime, game, " +
            "player_shooter, player_target) values (?,?,?,?)";

    // класс ShotsRepositoryJdbcImpl зависит от источника данных (CustomDataSource)
    private DataSource dataSource;

    public ShotsRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void save(Shot shot) {
        try (Connection connection = dataSource.getConnection();
             // вернет созданный id(ключи, которые были сгенерированы БД стали доступны во время выполнения программы)
             PreparedStatement statement = connection.prepareStatement(SQL_INSERT_SHOT, Statement.RETURN_GENERATED_KEYS)) {
            // добавление параметров
            statement.setString(1, shot.getDateTime().toString());
            statement.setLong(2, shot.getGame().getId());
            statement.setLong(3, shot.getShooter().getId());
            statement.setLong(4, shot.getTarget().getId());

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
                    shot.setId(id);
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
}
