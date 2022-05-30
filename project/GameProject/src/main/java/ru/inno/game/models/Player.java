package ru.inno.game.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

// Пользователь (игрок)
public class Player {
    // идетификатор игры - обязательное поле, ни с чем не совпадает (уникальное)
    private Long id;
    // IP-адрес, с которого игрок заходил в последний раз
    private String ip;
    // имя игрока
    private String name;
    // оберточный тип, т.к. значение может быть null(ни разу не играл)
    // максимальное количество очков
    private int points;
    // максимальное количество побед
    private int maxWinsCount;
    // максимальное количество поражений
    private int maxLosesCount;

}
