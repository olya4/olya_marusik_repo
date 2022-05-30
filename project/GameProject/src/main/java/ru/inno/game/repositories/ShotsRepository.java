package ru.inno.game.repositories;

import ru.inno.game.models.Shot;

public interface ShotsRepository {
    // сохранить выстрел
    void save (Shot shot);
}
