package ru.inno.game.app;

import ru.inno.game.dto.StatisticDto;
import ru.inno.game.repositories.*;
import ru.inno.game.services.GameService;
import ru.inno.game.services.GameServiceImpl;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // PlayersRepository playersRepository = new PlayersRepositoryMapImpl();
        PlayersRepository playersRepository = new PlayersRepositoryFileImpl("players_db.txt", "players_sequence.txt");
        GamesRepository gamesRepository = new GamesRepositoryListImpl();
        // ShotsRepository shotsRepository = new ShotsRepositoryListImpl();
        ShotsRepository shotsRepository = new ShotsRepositoryFileImpl("shots_db.txt", "shots_sequence.txt");
        GameService gameService = new GameServiceImpl(playersRepository, gamesRepository, shotsRepository);

        Scanner scanner = new Scanner(System.in);

        String first = scanner.nextLine();
        String second = scanner.nextLine();
        Random random = new Random(); // случайно считать выстрелы

        // позволяет сыграть три игры
        for (int j = 0; j < 3; j++) {
            Long gameId = gameService.startGame("127.0.0.1", "127.0.0.2", first, second);
            String shooter = first;
            String target = second;

            // реализация игры
            int i = 0;
            // можно сделать до 10 выстрелов
            while (i < 10) {
                System.out.println(shooter + " делайте выстрел в " + target);
                scanner.nextLine();
                // стоимость одного выстрела до двух очков
                int success = random.nextInt(2);

                if (success == 0) {
                    System.out.println("Успешно");
                    // первый игрок попал во второго
                    gameService.shot(gameId, shooter, target);
                } else {
                    System.out.println("Промах");
                }
                String temp = shooter;
                shooter = target;
                target = temp;
                i++;
            }

            StatisticDto statistic = gameService.finishGame(gameId);
            System.out.println(statistic);
        }
    }
}
