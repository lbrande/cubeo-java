package se.lovebrandefelt.cubeo.gui;

import static se.lovebrandefelt.cubeo.Color.RED;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import se.lovebrandefelt.cubeo.Die;
import se.lovebrandefelt.cubeo.Game;

public class GameCanvas extends Canvas {
  private static final Color RED_COLOR = Color.RED;
  private static final Color BLACK_COLOR = Color.BLACK;
  private static final Color DOT_COLOR = Color.WHITE;
  private static final double squareSize = 75;

  private Game game;
  private double startX;
  private double startY;

  public void setGame(Game game) {
    this.game = game;
    setWidth(12 * squareSize);
    setHeight(12 * squareSize);
    updateCenter();
    draw();
  }

  public void draw() {
    if (game != null) {
      GraphicsContext graphicsContext = getGraphicsContext2D();
      for (Die die : game.getBoard().getDice().values()) {
        double squareX =
            startX + (die.getPos().getX() - game.getBoard().getTopLeft().getX()) * squareSize;
        double squareY =
            startY + (die.getPos().getY() - game.getBoard().getTopLeft().getY()) * squareSize;
        if (die.getColor() == RED) {
          graphicsContext.setFill(RED_COLOR);
        } else {
          graphicsContext.setFill(BLACK_COLOR);
        }
        graphicsContext.fillRoundRect(
            squareX, squareY, squareSize, squareSize, squareSize / 3, squareSize / 3);
        drawDots(graphicsContext, squareX, squareY, die.getDots());
      }
    }
  }

  public void drawDots(GraphicsContext graphicsContext, double squareX, double squareY, int dots) {
    switch (dots) {
      case 1:
        drawDot(graphicsContext, squareX, squareY, 2, 2);
        break;
      case 2:
        drawDot(graphicsContext, squareX, squareY, 1, 1);
        drawDot(graphicsContext, squareX, squareY, 3, 3);
        break;
      case 3:
        drawDot(graphicsContext, squareX, squareY, 1, 1);
        drawDot(graphicsContext, squareX, squareY, 2, 2);
        drawDot(graphicsContext, squareX, squareY, 3, 3);
        break;
      case 4:
        drawDot(graphicsContext, squareX, squareY, 1, 1);
        drawDot(graphicsContext, squareX, squareY, 1, 3);
        drawDot(graphicsContext, squareX, squareY, 3, 1);
        drawDot(graphicsContext, squareX, squareY, 3, 3);
        break;
      case 5:
        drawDot(graphicsContext, squareX, squareY, 1, 1);
        drawDot(graphicsContext, squareX, squareY, 1, 3);
        drawDot(graphicsContext, squareX, squareY, 2, 2);
        drawDot(graphicsContext, squareX, squareY, 3, 1);
        drawDot(graphicsContext, squareX, squareY, 3, 3);
        break;
      case 6:
        drawDot(graphicsContext, squareX, squareY, 1, 1);
        drawDot(graphicsContext, squareX, squareY, 1, 2);
        drawDot(graphicsContext, squareX, squareY, 1, 3);
        drawDot(graphicsContext, squareX, squareY, 3, 1);
        drawDot(graphicsContext, squareX, squareY, 3, 2);
        drawDot(graphicsContext, squareX, squareY, 3, 3);
        break;
      default:
    }
  }

  public void drawDot(
      GraphicsContext graphicsContext, double squareX, double squareY, int dotX, int dotY) {
    graphicsContext.setFill(DOT_COLOR);
    graphicsContext.fillOval(
        squareX + squareSize * dotX / 4 - squareSize / 15,
        squareY + squareSize * dotY / 4 - squareSize / 15,
        squareSize / 7.5,
        squareSize / 7.5);
  }

  public void updateCenter() {
    int width = game.getBoard().getBottomRight().getX() + 1 - game.getBoard().getTopLeft().getX();
    startX = (getWidth() - width * squareSize) / 2;
    int height = game.getBoard().getBottomRight().getY() + 1 - game.getBoard().getTopLeft().getY();
    startY = (getHeight() - height * squareSize) / 2;
  }
}
