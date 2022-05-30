package ru.inno.game.client.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.inno.game.client.controllers.MainController;

public class Main extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String fxmlFileName = "/fxml/Main.fxml";
        // запуск fxmlFileName
        FXMLLoader loader = new FXMLLoader();
        // загрузить fxmlFileName
        Parent root = loader.load(getClass().getResourceAsStream(fxmlFileName));

        primaryStage.setTitle("Game Client");
        // положить сцену, загруженную из файла Main.fxml
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        // нельзя менять размер окна
        primaryStage.setResizable(false);

        // получить controller
        MainController controller = loader.getController();
        // при нажатии на кнопку, у controller получить перехватчика
        // взять обработчика нажатий из контроллера и добавить его в сцену
        scene.setOnKeyPressed(controller.getKeyEventEventHandler());

        // показать
        primaryStage.show();
    }
}
