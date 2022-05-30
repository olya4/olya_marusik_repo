package ru.inno.game.client.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import ru.inno.game.client.controllers.MainController;
import ru.inno.game.client.socket.SocketClient;

// отвечает за движение пользователя
public class GameUtils {

    // шаг движения (скорость перемешения шарика)
    private static final int PLAYER_STEP = 10;

    // урон от пульки
    private static final int DAMAGE = 5;

    private AnchorPane pane;
    private MainController controller;
    private SocketClient client;

    public void goRight(Circle player) {
        // то изменить значение по Х на 5 вправо
        // center - выбрана фигура КРУГ
        player.setCenterX(player.getCenterX() + PLAYER_STEP);
    }

    public void goLeft(Circle player) {
        // то изменить значение по Х на 5 влево
        // center - выбрана фигура КРУГ
        player.setCenterX(player.getCenterX() - PLAYER_STEP);
    }

    // player - кто стреляет,
    // isEnemy = false - стреляет игрок (нижний синий шарик),
    // isEnemy = true - стреляет противник (красный верхний шарик)
    // создание пульки
    public Circle createBulletFor(Circle player, boolean isEnemy) {
        // создать круг
        Circle bullet = new Circle();
        // указать радиус в пикселях
        bullet.setRadius(5);

        // расположение пульки на игровом поле
        pane.getChildren().add(bullet);
        bullet.setCenterX(player.getCenterX() + player.getLayoutX());
        bullet.setCenterY(player.getCenterY() + player.getLayoutY());

        // цвет пульки
        bullet.setFill(Color.ORANGE);

        int value;
        // если стреляет противник
        if (isEnemy) {
            // если выстрел вниз
            value = 1;
            // если стреляет игрок
        } else {
            // если выстрел вверх
            value = -1;
        }

        // в кого стреляют
        final Circle target;
        final Label targetHp;

        // в кого летит пулька
        if (!isEnemy) {
            // если стреляют в противника
            target = controller.getEnemy();
            targetHp = controller.getHpEnemy();
        } else {
            // если стреляют в игрока
            target = controller.getPlayer();
            targetHp = controller.getHpPlayer();
        }

        // чтобы пульки летели в противника
        // создание анимации
        // на создание каждой анимации уходит 0,005 секунды = задержка между анимациями
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.005), animation -> {
            // что происходит во время анимации
            // пулька движется вверх
            bullet.setCenterY(bullet.getCenterY() - 1);

            // если пульку еще видно и границы пульки пересекли границы в кого стреляют - произошло попадание
            if (bullet.isVisible() && isIntersects(bullet, target)) {
                // отнять здоровье в кого попали
                createDamage(targetHp);
                // если пулька пересекла границы, то она исчезает
                bullet.setVisible(false);
                // оправить сообщение на сервер
                // если попали в противника
                if (!isEnemy) {
                    client.sendMessage("DAMAGE");
                }
            }
        }));

        // анимация должна выполняться в цикле 500 раз
        timeline.setCycleCount(500);
        // запустить анимацию с пулькой
        timeline.play();

        return bullet;
    }

    private boolean isIntersects(Circle bullet, Circle player) {
        return bullet.getBoundsInParent().intersects(player.getBoundsInParent());
    }

    private void createDamage(Label hpLabel) {
        // старое значение, которое лежало в getHpEnemy()/getHpPlayer() преобразовать в int
        int hpPlayer = Integer.parseInt(hpLabel.getText());
        // отнять здоровье у getHpEnemy()/getHpPlayer() и преобразовать опять в текст
        hpLabel.setText(String.valueOf(hpPlayer - DAMAGE));
    }

    public void setClient(SocketClient client) {
        this.client = client;
    }

    public void setController(MainController controller) {
        this.controller = controller;
    }

    public void setPane(AnchorPane pane) {
        this.pane = pane;
    }
}
