package ru.inno.game.repositories;

import ru.inno.game.models.Shot;

import java.util.ArrayList;
import java.util.List;

public class ShotsRepositoryListImpl implements ShotsRepository {

    // список выстрелов
    private List<Shot> shots;

    // конструктор для создания нового списка выстрелов
    public ShotsRepositoryListImpl() {
        this.shots = new ArrayList<>();
    }

    // сохранить выстрел
    @Override
    public void save(Shot shot) {
        // положить выстрел в List
        this.shots.add(shot);
    }
}
