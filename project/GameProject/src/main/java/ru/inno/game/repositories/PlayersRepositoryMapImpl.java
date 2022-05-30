package ru.inno.game.repositories;

import ru.inno.game.models.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayersRepositoryMapImpl implements PlayersRepository {
    // массив игроков по имени
    private Map<String, Player> players;

    //конструктор для создания Map
    public PlayersRepositoryMapImpl() {
        players = new HashMap<>();
    }

    // получение игрока
    @Override
    public Player findByNickname(String nickname) {
        // получить игрока по nickname из Map
        return players.get(nickname);
    }

    // сохранение игрока
    @Override
    public void save(Player player) {
        // положить игрока по имени в Map
        players.put(player.getName(), player);
    }

    // обновление игрока
    @Override
    public void update(Player player) {
        // если игрок присутствует
        if (players.containsKey(player.getName())) {
            // сделать замену
            players.put(player.getName(), player);
            // если игрока нет в системе
        } else {
            System.err.println("Нельзя обновить несуществующего игрока");
        }
    }
}
