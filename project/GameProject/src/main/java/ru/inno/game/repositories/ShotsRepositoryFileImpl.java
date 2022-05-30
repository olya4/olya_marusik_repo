package ru.inno.game.repositories;

import ru.inno.game.models.Shot;

import java.io.*;

// реализация на основе файла
public class ShotsRepositoryFileImpl implements ShotsRepository {
    private String dbfileName;
    // для счета id у выстрелов
    private String sequenceFileName;

    public ShotsRepositoryFileImpl(String dbfileName, String sequenceFileName) {
        this.dbfileName = dbfileName;
        this.sequenceFileName = sequenceFileName;

    }

    // сохранить выстрел в файл
    @Override
    public void save(Shot shot) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dbfileName, true));
            // сгенерировать новый id выстрела
            shot.setId(generateId());
            // записать информацию в файл
            // прописываются все поля класса Shot через #
            writer.write(shot.getId() + "#" + shot.getDateTime().toString() + "#" +
                    shot.getGame().getId() + "#" + shot.getShooter().getName() + "#" +
                    shot.getTarget().getName() + "\n");
            writer.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    // вложенный класс для счета id у выстрелов
    private Long generateId() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(sequenceFileName));
            // счиать последний сгенерированный id, как строку из файла
            String lastGeneratedIdAsString = reader.readLine();
            // преобразовать строку из файла в long
            long id = Long.parseLong(lastGeneratedIdAsString);
            reader.close();

            BufferedWriter writer = new BufferedWriter(new FileWriter(sequenceFileName));
            // записать в файл значение id на один больше
            writer.write(String.valueOf(id + 1));
            writer.close();

            return id;

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
