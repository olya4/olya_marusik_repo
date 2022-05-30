package ru.inno.game.dto;

// информация об игре
public class StatisticDto {
    // id игры
    private long id;
    // имя первого игрока
    private String firstPlayer;
    // количество попаданий первого игрока
    private Integer countHitsFirstPlayer;
    // всего очков первого игрока
    private Integer totalPointsFirstPlayer;
    // имя второго игрока
    private String secondPlayer;
    // количество попаданий второго игрока
    private Integer countHitsSecondPlayer;
    // всего очков второго игрока
    private Integer totalPointsSecondPlayer;
    // имя победителя
    private String nameChampion;
    // длительность игры
    private Long gameTimeAmount;


    public StatisticDto(long id, String firstPlayer, Integer countHitsFirstPlayer, Integer totalPointsFirstPlayer, String secondPlayer, Integer countHitsSecondPlayer, Integer totalPointsSecondPlayer, String nameChampion, Long gameTimeAmount) {
        this.id = id;
        this.firstPlayer = firstPlayer;
        this.countHitsFirstPlayer = countHitsFirstPlayer;
        this.totalPointsFirstPlayer = totalPointsFirstPlayer;
        this.secondPlayer = secondPlayer;
        this.countHitsSecondPlayer = countHitsSecondPlayer;
        this.totalPointsSecondPlayer = totalPointsSecondPlayer;
        this.nameChampion = nameChampion;
        this.gameTimeAmount = gameTimeAmount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Игра с ID = ").append(id).append("\n");
        sb.append("Игрок 1:").append(firstPlayer).append(", попаданий - ").append(countHitsFirstPlayer)
                .append(", всего очков - ").append(totalPointsFirstPlayer).append("\n");
        sb.append("Игрок 2:").append(secondPlayer).append(", попаданий - ").append(countHitsSecondPlayer)
                .append(", всего очков - ").append(totalPointsSecondPlayer).append("\n");
        sb.append("Победа: ").append(nameChampion).append("\n");
        sb.append("Игра длилась: ").append(gameTimeAmount).append(" секунд").append("\n");

        return sb.toString();
    }
}

