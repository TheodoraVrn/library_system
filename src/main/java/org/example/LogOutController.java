package org.example;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class LogOutController {
    public static void Logout(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(LogOutController.class.getResource("log-in.fxml"));
        Parent root = loader.load();
        LogInController logInController = loader.getController();
        logInController.setPrimaryStage(primaryStage);
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("");
        primaryStage.show();
    }
}
