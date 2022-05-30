package ru.inno.game.server;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.inno.game.dto.StatisticDto;
import ru.inno.game.repositories.*;
import ru.inno.game.services.GameService;
import ru.inno.game.services.GameServiceImpl;

import javax.sql.DataSource;
import java.util.Random;
import java.util.Scanner;

public class MainServer {
    public static void main(String[] args) {
        // Hikari Connection Pool - пул-соединение с БД
        // позволяет открыть сразу несколько соединений с БД
        // экономит время на отправку запросов
        HikariConfig configucation = new HikariConfig();
        // указание данных для подключения
        configucation.setJdbcUrl("jdbc:postgresql://localhost:5432/game_db");
        configucation.setDriverClassName("org.postgresql.Driver");
        configucation.setUsername("postgres");
        configucation.setPassword("Wert2345@");
        // максимальное количество подключений
        configucation.setMaximumPoolSize(20);
        // DataSource - источник данных
        DataSource dataSource = new HikariDataSource(configucation);
        // репозиторий, который использует этот источник данных
        GamesRepository gamesRepository = new GamesRepositoryJdbcImpl(dataSource);
        PlayersRepository playersRepository = new PlayersRepositoryJdbcImpl(dataSource);
        ShotsRepository shotsRepository = new ShotsRepositoryJdbcImpl(dataSource);
        // сервис, которые использует созданные выше репозитории
        GameService gameService = new GameServiceImpl(playersRepository, gamesRepository, shotsRepository);
        // передача сервиса объекту-серверу для игры (сервер может использовать сервис)
        GameServer gameServer = new GameServer(gameService);
        gameServer.start(7777);

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
