package se.lovebrandefelt.cubeo.gui;

import javafx.scene.input.MouseEvent;
import se.lovebrandefelt.cubeo.Game;

public class GuiController {
  public GameCanvas canvas;

  public void canvasClicked(MouseEvent mouseEvent) {
    canvas.onClick(mouseEvent);
  }

  public void newGame() {
    canvas.setGame(new Game());
  }

  public void centerBoard() {
    canvas.updateCenter();
  }
}
