package ru.inno.game.repositories;

import ru.inno.game.models.Player;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PlayersRepositoryFileImpl implements PlayersRepository {
    private String dbfileName;
    // для счета id у игроков
    private String sequenceFileName;

    public PlayersRepositoryFileImpl(String dbfileName, String sequenceFileName) {
        this.dbfileName = dbfileName;
        this.sequenceFileName = sequenceFileName;

    }

    @Override
    public Player findByNickname(String nickname) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dbfileName));
            // вернет все строки из файла в виде строк объекта класса String
            Optional<Player> players = reader
                    .lines()
                    .map(line -> {
                        String parsedLine[] = line.split("#");
                        // строки собираются по конструктору Player
                        return new Player(Long.parseLong(parsedLine[0]),
                                parsedLine[1],
                                parsedLine[2],
                                Integer.parseInt(parsedLine[3]),
                                Integer.parseInt(parsedLine[4]),
                                Integer.parseInt(parsedLine[5]));
                    })
                    .filter(player -> player.getName().equals(nickname))
                    .findFirst();

            reader.close();

            return players.orElse(null);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    // сохранить игрока в файл
    @Override
    public void save(Player player) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dbfileName, true));
            // сгенерировать новый id игрока
            player.setId(generateId());
            // записать информацию в файл
            // прописываются все поля класса Player через #
            writer.write(player.getId() + "#" + player.getIp() + "#" + player.getName() + "#" +
                    player.getPoints() + "#" + player.getMaxWinsCount() + "#" +
                    player.getMaxLosesCount() + "\n");
            writer.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    // вложенный класс для счета id у игрока
    private Long generateId() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(sequenceFileName));
            // считать последний сгенерированный id, как строку из файла
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

    @Override
    public void update(Player player) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dbfileName));
            // вернет все строки из файла в виде строк объекта класса String
            List<Player> players = reader
                    .lines()
                    .map(line -> {
                        String parsedLine[] = line.split("#");
                        // строки собираются по конструктору Player
                        return new Player(Long.parseLong(parsedLine[0]),
                                parsedLine[1],
                                parsedLine[2],
                                Integer.parseInt(parsedLine[3]),
                                Integer.parseInt(parsedLine[4]),
                                Integer.parseInt(parsedLine[5]));
                    })
                    .collect(Collectors.toList());
            reader.close();

            for (int i = 0; i < players.size(); i++) {
                // перебор всех игроков
                Player x = players.get(i);
                // если игрок присутствует
                if (x.getId().equals(player.getId())) {
                    // скопировать все поля из player
                    x.setIp(player.getIp());
                    x.setName(player.getName());
                    x.setPoints(player.getPoints());
                    x.setMaxWinsCount(player.getMaxWinsCount());
                    x.setMaxLosesCount(player.getMaxLosesCount());
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(dbfileName));
            // записать данные в файл
            for (int i = 0; i < players.size(); i++) {
                // перебор всех игроков
                Player x = players.get(i);
                writer.write(x.getId() + "#" + x.getIp() + "#" + x.getName() + "#" +
                        x.getPoints() + "#" + x.getMaxWinsCount() + "#" +
                        x.getMaxLosesCount() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}


