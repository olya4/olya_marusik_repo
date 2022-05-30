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

// Выстрел
public class Shot {
    // идетификатор игры - обязательное поле, ни с чем не совпадает (уникальное)
    private Long id;
    // время выстрела
    private LocalDateTime dateTime;
    // какая игра
    private Game game;
    // кто стрелял
    private Player shooter;
    // в кого попали
    private Player target;

}
