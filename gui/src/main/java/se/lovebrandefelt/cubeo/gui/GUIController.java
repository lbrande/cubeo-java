package se.lovebrandefelt.cubeo.gui;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import se.lovebrandefelt.cubeo.Game;

public class GUIController {
  public GameCanvas canvas;

  public void canvasClicked(MouseEvent mouseEvent) {
    canvas.onClick(mouseEvent);
  }

  public void newGame(ActionEvent actionEvent) {
    canvas.setGame(new Game());
  }

  public void centerBoard(ActionEvent actionEvent) {
    canvas.updateCenter();
  }
}
