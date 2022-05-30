package ru.inno.game.client.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import ru.inno.game.client.socket.SocketClient;
import ru.inno.game.client.utils.GameUtils;


import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private GameUtils gameUtils;

    private SocketClient socketClient;

    // синий шарик - стрелок
    @FXML
    private Circle player;

    // красный шарик - цель
    @FXML
    private Circle enemy;

    // отправить nickname игрока
    @FXML
    private Button buttonGo;

    // подключиться
    @FXML
    private Button buttonConnect;

    // имя игрока
    @FXML
    private TextField textPlayerName;

    // пулька
    @FXML
    private AnchorPane pane;

    // здоровье игрока
    @FXML
    private Label hpPlayer;

    // здоровье противника
    @FXML
    private Label hpEnemy;

    public EventHandler<KeyEvent> getKeyEventEventHandler() {
        return keyEventEventHandler;
    }

    public GameUtils getGameUtils() {
        return gameUtils;
    }

    // перехватчик событий. Будет перехватывать нажатие на кнопку
    private EventHandler<KeyEvent> keyEventEventHandler = event -> {
        // если нажатие справа
        if (event.getCode() == KeyCode.RIGHT) {
            gameUtils.goRight(player);
            socketClient.sendMessage("right");
        } else if (event.getCode() == KeyCode.LEFT) {
            gameUtils.goLeft(player);
            socketClient.sendMessage("left");
        } else if (event.getCode() == KeyCode.SPACE) { // пробел - стрелять
            // выстрел игрока
            Circle bullet = gameUtils.createBulletFor(player, false);
            // сообщение о выстреле
            socketClient.sendMessage("shot");
        }
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameUtils = new GameUtils();

        // когда нажимают на buttonConnect - происходит подключение к серверу
        buttonConnect.setOnAction(event -> {
            socketClient = new SocketClient(this, "localhost", 7777);
            // запустить метод run() в socketClient
            new Thread(socketClient).start();
            // когда клиент подключился к серверу, нельзя второй раз подключиться к серверу
            buttonConnect.setDisable(true);
            // когда клиент подключился к серверу, можно отправить имя игрока
            buttonGo.setDisable(false);
            textPlayerName.setDisable(false);
            gameUtils.setPane(pane);
            gameUtils.setClient(socketClient);
        });
        // когда нажимают на  buttonGo отправить имя игрока на сервер
        buttonGo.setOnAction(event -> {
            socketClient.sendMessage("name: " + textPlayerName.getText());
            // когда игрок ввел имя, нельзя второй раз отправить имя, поля будут неактивны
            buttonGo.setDisable(true);
            textPlayerName.setDisable(true);
            // чтобы после введения имени не зависал курсор
            buttonGo.getScene().getRoot().requestFocus();
        });
        gameUtils.setController(this);
    }

    public Circle getPlayer() {
        return player;
    }

    public Circle getEnemy() {
        return enemy;
    }

    public Label getHpPlayer() {
        return hpPlayer;
    }

    public Label getHpEnemy() {
        return hpEnemy;
    }
}
