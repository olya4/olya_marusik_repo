package ru.inno.game.services;

import ru.inno.game.dto.StatisticDto;

public interface GameService {
    // Long - идентификатор игры

    /**
     * метод для начала игры
     * Если игрок с таким никнеймом уже есть - работать с ним
     * Если нет - создать нового
     *
     * @param firstIp              = IP-адрес, с которого зашел первый игрок
     * @param secondIp             = IP-адрес, с которого зашел второй игрок
     * @param firstPlayerNickname  - имя первого игрока
     * @param secondPlayerNickname - имя второго игрока
     * @return идентификатор игры Long
     */
    Long startGame(String firstIp, String secondIp, String firstPlayerNickname, String secondPlayerNickname);

    // был произведен выстрел (только попавшие выстрелы)
    // в ходе какой игры, кто стрелял, в кого попали

    /**
     * фиксирует попавший выстрел игроков
     *
     * @param gameId          - идентификатор игры
     * @param shooterNickname - имя первого игрока
     * @param targetNickname  - имя второго игрока
     */
    void shot(Long gameId, String shooterNickname, String targetNickname);

    // закончить игру
    StatisticDto finishGame(Long gameId);
}
