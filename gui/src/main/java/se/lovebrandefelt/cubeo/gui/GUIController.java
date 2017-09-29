package se.lovebrandefelt.cubeo.gui;

import javafx.scene.input.MouseEvent;

public class GUIController {
  public GameCanvas canvas;

  public void canvasClicked(MouseEvent mouseEvent) {
    canvas.draw();
  }
}
