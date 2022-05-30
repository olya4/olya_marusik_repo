package ru.inno.game.repositories;

import ru.inno.game.models.Game;

public interface GamesRepository {
    // сохраненить игру
    void save(Game game);

    // найти игру по id
    Game findById(Long gameId);

    // обновить игру
    void update (Game game);
}
