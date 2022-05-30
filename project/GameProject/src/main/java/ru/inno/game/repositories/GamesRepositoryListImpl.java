package ru.inno.game.repositories;

import ru.inno.game.models.Game;

import java.util.ArrayList;
import java.util.List;

public class GamesRepositoryListImpl implements GamesRepository {

    private List<Game> games;

    // конструктор для создания List
    public GamesRepositoryListImpl() {
        games = new ArrayList<>();
    }

    // сохранение игры
    @Override
    public void save(Game game) {
        // идентификатор игры - это ее порядковый номер в списке
        game.setId((long) games.size());
        // добавить игру в List
        games.add(game);
    }

    // найти игру по Id
    @Override
    public Game findById(Long gameId) {
        // преобразование gameId из long в int
        return games.get(gameId.intValue());
    }

    // обновить игру
    @Override
    public void update(Game game) {
        games.set(game.getId().intValue(), game);
    }
}
