package se.lovebrandefelt.cubeo.gui;

import static javafx.scene.text.FontWeight.BOLD;
import static se.lovebrandefelt.cubeo.Color.RED;

import java.util.stream.Collectors;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import se.lovebrandefelt.cubeo.AI;
import se.lovebrandefelt.cubeo.AddAction;
import se.lovebrandefelt.cubeo.Die;
import se.lovebrandefelt.cubeo.Game;
import se.lovebrandefelt.cubeo.MergeAI;
import se.lovebrandefelt.cubeo.MergeAction;
import se.lovebrandefelt.cubeo.MoveAction;
import se.lovebrandefelt.cubeo.Pos;

public class GameCanvas extends Canvas {
  private static final Color BACKGROUND_COLOR = Color.DARKGREEN;
  private static final Color RED_COLOR = Color.RED;
  private static final Color BLACK_COLOR = Color.BLACK;
  private static final Color DOT_COLOR = Color.WHITE;
  private static final Color EMPTY_SELECTABLE_COLOR = Color.rgb(0, 0, 0, 0.35);
  private static final Color RED_SELECTABLE_COLOR = RED_COLOR.darker();
  private static final Color BLACK_SELECTABLE_COLOR = BLACK_COLOR.brighter();
  private static final Color SELECTABLE_BORDER_COLOR = Color.LIGHTYELLOW;
  private static final Color SELECTED_BORDER_COLOR = Color.YELLOW;
  private static final double squareSize = 75;
  private static final double fillSquareSize = squareSize - 4;
  private static final double dotSize = fillSquareSize / 5;
  private static final double fontSize = fillSquareSize * 2 / 3;

  private Game game;
  private AI ai;
  private Pos selectedFrom;
  private double startX;
  private double startY;
  private GraphicsContext graphicsContext;

  public GameCanvas() {
    super();
    ai = new MergeAI();
    graphicsContext = getGraphicsContext2D();
    graphicsContext.setFont(Font.font(null, BOLD, fontSize));
    graphicsContext.setTextAlign(TextAlignment.CENTER);
    graphicsContext.setTextBaseline(VPos.CENTER);
  }

  public void setGame(Game game) {
    this.game = game;
    setWidth(12 * squareSize);
    setHeight(12 * squareSize);
    updateCenter();
    draw();
    updateTitle();
  }

  public void draw() {
    graphicsContext.setFill(BACKGROUND_COLOR);
    graphicsContext.fillRect(0, 0, getWidth(), getHeight());
    if (game != null) {
      for (Die die : game.getBoard().getDice().values()) {
        double squareX = startX + die.getPos().getX() * squareSize;
        double squareY = startY + die.getPos().getY() * squareSize;
        if (die.getColor() == RED) {
          if ((selectedFrom == null
              && game.getBoard()
              .legalActions(game.getCurrentPlayer())
              .stream()
              .anyMatch(action -> action.getFrom().equals(die.getPos()))
              && game.getResult() == null)
              || (selectedFrom != null
              && game.getBoard()
              .legalToActions(game.getCurrentPlayer())
              .stream()
              .anyMatch(action -> action.getTo().equals(die.getPos())))) {
            fillDie(squareX, squareY, RED_SELECTABLE_COLOR);
            strokeDie(squareX, squareY, SELECTABLE_BORDER_COLOR);
          } else {
            fillDie(squareX, squareY, RED_COLOR);
          }
          drawDots(squareX, squareY, die.getDots());
        } else {
          if ((selectedFrom == null
              && game.getBoard()
              .legalActions(game.getCurrentPlayer())
              .stream()
              .anyMatch(action -> action.getFrom().equals(die.getPos()))
              && game.getResult() == null)
              || (selectedFrom != null
              && game.getBoard()
              .legalToActions(game.getCurrentPlayer())
              .stream()
              .anyMatch(action -> action.getTo().equals(die.getPos())))) {
            fillDie(squareX, squareY, BLACK_SELECTABLE_COLOR);
            strokeDie(squareX, squareY, SELECTABLE_BORDER_COLOR);
          } else {
            fillDie(squareX, squareY, BLACK_COLOR);
          }
          drawDots(squareX, squareY, die.getDots());
        }
      }
      if (selectedFrom != null) {
        for (MoveAction action :
            game.getBoard()
                .legalMoveActions(game.getCurrentPlayer())
                .stream()
                .filter(action -> action.getFrom().equals(selectedFrom))
                .collect(Collectors.toSet())) {
          double squareX = startX + action.getTo().getX() * squareSize;
          double squareY = startY + action.getTo().getY() * squareSize;
          fillDie(squareX, squareY, EMPTY_SELECTABLE_COLOR);
          strokeDie(squareX, squareY, SELECTABLE_BORDER_COLOR);
        }
      }
      if (selectedFrom == null && game.getResult() == null) {
        for (AddAction action : game.getBoard().legalAddActions(game.getCurrentPlayer())) {
          double squareX = startX + action.getFrom().getX() * squareSize;
          double squareY = startY + action.getFrom().getY() * squareSize;
          fillDie(squareX, squareY, EMPTY_SELECTABLE_COLOR);
          strokeDie(squareX, squareY, SELECTABLE_BORDER_COLOR);
        }
      }
      if (selectedFrom != null && game.getBoard().getDice().containsKey(selectedFrom)) {
        double squareX = startX + selectedFrom.getX() * squareSize;
        double squareY = startY + selectedFrom.getY() * squareSize;
        strokeDie(squareX, squareY, SELECTED_BORDER_COLOR);
      }
    }
  }

  public void fillDie(double squareX, double squareY, Color color) {
    graphicsContext.setFill(color);
    graphicsContext.fillRoundRect(
        squareX, squareY, fillSquareSize, fillSquareSize, fillSquareSize / 3, fillSquareSize / 3);
  }

  public void strokeDie(double squareX, double squareY, Color color) {
    graphicsContext.setStroke(color);
    graphicsContext.strokeRoundRect(
        squareX, squareY, fillSquareSize, fillSquareSize, fillSquareSize / 3, fillSquareSize / 3);
  }

  public void drawDots(double squareX, double squareY, int dots) {
    switch (dots) {
      case 1:
        drawDot(squareX, squareY, 2, 2);
        break;
      case 2:
        drawDot(squareX, squareY, 1, 1);
        drawDot(squareX, squareY, 3, 3);
        break;
      case 3:
        drawDot(squareX, squareY, 1, 1);
        drawDot(squareX, squareY, 2, 2);
        drawDot(squareX, squareY, 3, 3);
        break;
      case 4:
        drawDot(squareX, squareY, 1, 1);
        drawDot(squareX, squareY, 1, 3);
        drawDot(squareX, squareY, 3, 1);
        drawDot(squareX, squareY, 3, 3);
        break;
      case 5:
        drawDot(squareX, squareY, 1, 1);
        drawDot(squareX, squareY, 1, 3);
        drawDot(squareX, squareY, 2, 2);
        drawDot(squareX, squareY, 3, 1);
        drawDot(squareX, squareY, 3, 3);
        break;
      case 6:
        drawDot(squareX, squareY, 1, 1);
        drawDot(squareX, squareY, 1, 2);
        drawDot(squareX, squareY, 1, 3);
        drawDot(squareX, squareY, 3, 1);
        drawDot(squareX, squareY, 3, 2);
        drawDot(squareX, squareY, 3, 3);
        break;
      default:
        graphicsContext.setFill(DOT_COLOR);
        graphicsContext.fillText(
            String.valueOf(dots), squareX + fillSquareSize / 2, squareY + fillSquareSize / 2);
    }
  }

  public void drawDot(double squareX, double squareY, int dotX, int dotY) {
    graphicsContext.setFill(DOT_COLOR);
    graphicsContext.fillOval(
        squareX + fillSquareSize * dotX / 4 - dotSize / 2,
        squareY + fillSquareSize * dotY / 4 - dotSize / 2,
        dotSize,
        dotSize);
  }

  public void updateCenter() {
    int width = game.getBoard().getBottomRight().getX() + 1 - game.getBoard().getTopLeft().getX();
    startX =
        (getWidth() - width * squareSize + squareSize - fillSquareSize) / 2
            - game.getBoard().getTopLeft().getX() * squareSize;
    int height = game.getBoard().getBottomRight().getY() + 1 - game.getBoard().getTopLeft().getY();
    startY =
        (getHeight() - height * squareSize + squareSize - fillSquareSize) / 2
            - game.getBoard().getTopLeft().getY() * squareSize;
    draw();
  }

  public void updateTitle() {
    if (game.getResult() == null) {
      GUI.setTitle("Cubeo - " + game.getCurrentPlayer() + "'s turn");
    } else {
      switch (game.getResult()) {
        case RED_WON_BY_STALEMATE:
          GUI.setTitle("Cubeo - RED won by stalemating BLACK");
          break;
        case RED_WON_BY_7_PLUS_MERGE:
          GUI.setTitle("Cubeo - RED won by merging above 6");
          break;
        case BLACK_WON_BY_STALEMATE:
          GUI.setTitle("Cubeo - BLACK won by stalemating RED");
          break;
        case BLACK_WON_BY_7_PLUS_MERGE:
          GUI.setTitle("Cubeo - BLACK won by merging above 6");
          break;
        default:
      }
    }
  }

  public void onClick(MouseEvent mouseEvent) {
    if (game.getResult() == null) {
      Pos pos =
          new Pos(
              (int)
                  Math.floor(
                      (mouseEvent.getX() - startX + (squareSize - fillSquareSize) / 2)
                          / squareSize),
              (int)
                  Math.floor(
                      (mouseEvent.getY() - startY + (squareSize - fillSquareSize) / 2)
                          / squareSize));
      if (selectedFrom == null) {
        if (game.performAction(new AddAction(game.getCurrentPlayer(), pos))) {
          if (game.getResult() == null && game.getCurrentPlayer() != RED) {
            ai.performAction(game);
          }
        } else if (game.getBoard()
            .legalActions(game.getCurrentPlayer())
            .stream()
            .anyMatch(action -> action.getFrom().equals(pos))) {
          selectedFrom = pos;
        }
      } else {
        if (game.performAction(new MergeAction(selectedFrom, pos))
            || game.performAction(new MoveAction(selectedFrom, pos))) {
          if (game.getResult() == null && game.getCurrentPlayer() != RED) {
            ai.performAction(game);
          }
        }
        selectedFrom = null;
      }
    }
    if (game.getBoard()
        .legalAddActions(game.getCurrentPlayer())
        .stream()
        .anyMatch(action -> posOutSideCanvas(action.getFrom()))
        || game.getBoard()
        .legalMoveActions(game.getCurrentPlayer())
        .stream()
        .anyMatch(action -> posOutSideCanvas(action.getFrom()))) {
      updateCenter();
    }
    draw();
    updateTitle();
  }

  public boolean posOutSideCanvas(Pos pos) {
    double squareX = startX - (squareSize - fillSquareSize) / 2 + pos.getX() * squareSize;
    double squareY = startY - (squareSize - fillSquareSize) / 2 + pos.getY() * squareSize;
    return squareX < 0
        || squareY < 0
        || squareX + squareSize > getWidth()
        || squareY + squareSize > getHeight();
  }
}
