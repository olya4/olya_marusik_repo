package ru.inno.game.client.socket;

import javafx.application.Platform;
import ru.inno.game.client.controllers.MainController;
import ru.inno.game.client.utils.GameUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// поток для получения сообщений с сервера
public class SocketClient extends Thread {
    //канал подключения
    private Socket socket;
    // поток для отправления сообщений серверу
    private PrintWriter toServer;
    // поток для получения сообщений от сервера
    private BufferedReader fromServer;

    private MainController controller;

    private GameUtils gameUtils;

    public SocketClient(MainController controller, String host, int port) {
        try {
            // подключение к серверу
            socket = new Socket(host, port);
            // поток символов, который можно направлять серверу (запись)
            toServer = new PrintWriter(socket.getOutputStream(), true);
            // поток символов, который приходит с сервера (чтение)
            fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.controller = controller;
            this.gameUtils = controller.getGameUtils();

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    // ждет сообщения от сервера
    @Override
    public void run() {
        // бесконечный поток
        while (true) {
            // получение сообщение от сервера
            String messageFromServer;
            try {
                // чтение сообщения с сервера
                messageFromServer = fromServer.readLine();
                if (messageFromServer != null) {
                    switch (messageFromServer) {
                        case "left":
                            // передвинуть противника влево
                            gameUtils.goLeft(controller.getEnemy());
                            break;
                        case "right":
                            // передвинуть противника вправо
                            gameUtils.goRight(controller.getEnemy());
                            break;
                        case "shot":
                            // выстрел противника
                            // Platform.runLater - подружит потоки java и javaFx
                            // Java FX запускает в своем потоке
                            Platform.runLater(() -> gameUtils.createBulletFor(controller.getEnemy(), true));
                            break;
                    }
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    // отправить сообщение на сервер
    public void sendMessage(String message) {
        toServer.println(message);
    }

}
