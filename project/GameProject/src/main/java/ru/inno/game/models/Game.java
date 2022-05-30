package ru.inno.game.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

// Игра
public class Game {
    // идетификатор игры - обязательное поле, ни с чем не совпадает (уникальное)
    private Long id;
    // дата игры
    private LocalDateTime dateTime;
    // первый игрок
    private Player playerFirst;
    // второй игрок
    private Player playerSecond;
    // количество выстрелов первого игрока
    private int playerFirstShotCount;
    // количество высрелов второго игрока
    private int playerSecondShotCount;
    // длительность игры (сколько секунд)
    private Long secondsGameTimeAmount;

}
