package ru.inno.game.services;

import ru.inno.game.dto.StatisticDto;
import ru.inno.game.models.Game;
import ru.inno.game.models.Player;
import ru.inno.game.models.Shot;
import ru.inno.game.repositories.GamesRepository;
import ru.inno.game.repositories.PlayersRepository;
import ru.inno.game.repositories.ShotsRepository;

import java.time.Duration;
import java.time.LocalDateTime;

// бизнес-логика
public class GameServiceImpl implements GameService {
    // добавление интерфейсов репозиториев
    private PlayersRepository playersRepository;
    private GamesRepository gamesRepository;
    private ShotsRepository shotsRepository;

    public GameServiceImpl(PlayersRepository playersRepository, GamesRepository gamesRepository, ShotsRepository shotsRepository) {
        this.playersRepository = playersRepository;
        this.gamesRepository = gamesRepository;
        this.shotsRepository = shotsRepository;
    }

    // логика работы с интерфейсом репозитория

    @Override
    public Long startGame(String firstIp, String secondIp, String firstPlayerNickname, String secondPlayerNickname) {
        System.out.println("ПОЛУЧИЛИ: " + firstIp + " " + secondIp + " " + firstPlayerNickname + " " + secondPlayerNickname);
        // необходимо найти первого игрока или создать его
        // необходимо найти второго игрока или создать его

        // найти игрока
        // если ли первый игрок под таким именем
        Player first = checkIfExists(firstIp, firstPlayerNickname);
        // если ли второй игрок под таким именем
        Player second = checkIfExists(secondIp, secondPlayerNickname);

        // создать игру, все значение = null, т.к. это начало игры, заполняются поля по конструктору из класса Game
        Game game = Game.builder()
                .dateTime(LocalDateTime.now())
                .playerFirst(first)
                .playerSecond(second)
                .playerFirstShotCount(0)
                .playerSecondShotCount(0)
                .secondsGameTimeAmount(0L)
                .build();

        // сохранить игру
        gamesRepository.save(game);
        // возвращает идентификатор игры Long
        return game.getId();

    }

    // получение информации об обоих игроках
    private Player checkIfExists(String ip, String nickname) {
        Player player = playersRepository.findByNickname(nickname);
        // если нет игрока под таким именем
        if (player == null) {
            // создать игрока, заполняются поля по конструктору из класса Player
            player = Player.builder()
                    .name(nickname)
                    .ip(ip)
                    .build();
            // сохраненить игрока
            playersRepository.save(player);
        } else {
            // если игрок уже был в системе - обновить данные(Ip-адрес)
            player.setIp(ip);
            playersRepository.update(player);
        }
        return player;
    }

    // метод считает выстрелы
    @Override
    public void shot(Long gameId, String shooterNickname, String targetNickname) {
        System.out.println(gameId + " " + shooterNickname + " " + targetNickname);
        // получаем кто стрелял из репозитория
        Player shooter = playersRepository.findByNickname(shooterNickname);
        // получаем в кого стреляли из репозитория
        Player target = playersRepository.findByNickname(targetNickname);
        // найти игру по id
        Game game = gamesRepository.findById(gameId);
        // создать выстрел, заполняются поля по конструктору из класса Shot
        Shot shot = Shot.builder()
                .dateTime(LocalDateTime.now())
                .game(game)
                .shooter(shooter)
                .target(target)
                .build();
        // при каждом попадании, количество очков увеличивается на единицу
        shooter.setPoints(shooter.getPoints() + 1);

        // если стрелявший первый игрок
        if (game.getPlayerFirst().getId().equals(shooter.getId())) {
            // увеличивается количество выстрелов первого игрока и сохраняется в игре
            game.setPlayerFirstShotCount(game.getPlayerFirstShotCount() + 1);
        }
        // если стрелявший второй игрок
        if (game.getPlayerSecond().getId().equals(shooter.getId())) {
            // увеличивается количество выстрелов второго игрока и сохраняется в игре
            game.setPlayerSecondShotCount(game.getPlayerSecondShotCount() + 1);
        }

        // обновить данные по стреляющему т.к. внесены изменения (+1 при каждом выстреле)
        playersRepository.update(shooter);

        // чтобы обновить игру, надо чтоб было время продолжительности игры
        Duration a = Duration.between(game.getDateTime(), LocalDateTime.now());
        // изменить значение время игры на объект класса Duration
        game.setSecondsGameTimeAmount(a.getSeconds());
        // обновить данные по игре
        gamesRepository.update(game);

        // сохранить выстрел
        shotsRepository.save(shot);
    }

    @Override
    public StatisticDto finishGame(Long gameId) {
        // найти игру из репозитория по id
        Game game = gamesRepository.findById(gameId);
        // создать объект класса Duration, затем у класса Duration вызвать метод between
        // метод between - вернет разницу между началом игры и текущим временем
        Duration a = Duration.between(game.getDateTime(), LocalDateTime.now());
        // изменить значение время игры на объект класса Duration
        game.setSecondsGameTimeAmount(a.getSeconds());

        // обновить игру
        gamesRepository.update(game);

        // имя победителя
        String nameChampion;
        // если у первого игрока больше очков, чем у второго
        if (game.getPlayerFirst().getPoints() > game.getPlayerSecond().getPoints()) {
            // имя победителя - первый игрок
            nameChampion = game.getPlayerFirst().getName();
            // к количеству побед первого игрока + 1
            game.getPlayerFirst().setMaxWinsCount(game.getPlayerFirst().getMaxWinsCount() + 1);
            // к количеству поражений второго игрока + 1
            game.getPlayerSecond().setMaxLosesCount(game.getPlayerSecond().getMaxLosesCount() + 1);
        } else {
            // имя победителя - второй игрок
            nameChampion = game.getPlayerSecond().getName();
            // к количеству побед второго игрока + 1
            game.getPlayerSecond().setMaxWinsCount(game.getPlayerSecond().getMaxWinsCount() + 1);
            // к количеству поражений первого игрока + 1
            game.getPlayerFirst().setMaxLosesCount(game.getPlayerFirst().getMaxLosesCount() + 1);
        }

        // сохранить изменения по первому игроку
        playersRepository.update(game.getPlayerFirst());
        // сохранить ихменения по второму игроку
        playersRepository.update(game.getPlayerSecond());

        // создать новый объект класса StatisticDto
        StatisticDto statistic = new StatisticDto(game.getId(), game.getPlayerFirst().getName(),
                game.getPlayerFirstShotCount(), game.getPlayerFirst().getPoints(),
                game.getPlayerSecond().getName(), game.getPlayerSecondShotCount(),
                game.getPlayerSecond().getPoints(),
                nameChampion, game.getSecondsGameTimeAmount());

        return statistic;
    }
}
