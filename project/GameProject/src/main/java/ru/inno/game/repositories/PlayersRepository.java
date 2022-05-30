package ru.inno.game.repositories;

import ru.inno.game.models.Player;

public interface PlayersRepository {
    // метод, позволяющий получить игрока по его имени
    Player findByNickname(String nickname);

    // сохранение игрока
    void save(Player player);

    // обновление игрока
    void update(Player player);
}
