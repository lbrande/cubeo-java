package se.lovebrandefelt.cubeo.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.lovebrandefelt.cubeo.Game;

public class GUI extends Application {
  private static Stage stage;

  public static void main(String[] args) {
    launch(args);
  }

  public static void setTitle(String title) {
    stage.setTitle(title);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    stage = primaryStage;
    Parent root = FXMLLoader.load(getClass().getResource("/GUI.fxml"));
    Scene scene = new Scene(root);
    GameCanvas canvas = (GameCanvas) scene.lookup("#canvas");
    canvas.setGame(new Game());
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.sizeToScene();
  }
}
