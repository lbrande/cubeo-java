package se.lovebrandefelt.cubeo.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.lovebrandefelt.cubeo.Game;

public class GUI extends Application {
  private GameCanvas canvas;
  private Game game;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("/GUI.fxml"));
    Scene scene = new Scene(root);
    canvas = (GameCanvas) scene.lookup("#canvas");
    game = new Game();
    canvas.setGame(game);
    primaryStage.setScene(scene);
    primaryStage.show();
    primaryStage.sizeToScene();
  }
}
