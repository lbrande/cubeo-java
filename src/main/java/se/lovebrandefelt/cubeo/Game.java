package se.lovebrandefelt.cubeo;

import static se.lovebrandefelt.cubeo.Color.RED;
import static se.lovebrandefelt.cubeo.GameResult.BLACK_WON_BY_7_PLUS_MERGE;
import static se.lovebrandefelt.cubeo.GameResult.BLACK_WON_BY_STALEMATE;
import static se.lovebrandefelt.cubeo.GameResult.RED_WON_BY_7_PLUS_MERGE;
import static se.lovebrandefelt.cubeo.GameResult.RED_WON_BY_STALEMATE;

import java.util.Set;

public class Game {
  private Board board;
  private Color currentPlayer;
  private GameResult result;

  public Game() {
    board = new Board();
    currentPlayer = RED;
  }

  public Set<Action> legalActions() {
    return board.legalActions(currentPlayer);
  }

  /**
   * Performs an action if possible.
   *
   * @param action action to perform
   * @return whether the action was performed
   */
  public boolean performAction(Action action) {
    if (board.legalActions(currentPlayer).contains(action) && result == null) {
      board.performAction(action);
      nextTurn();
      return true;
    }
    return false;
  }

  void undoAction() {
    board.undoAction();
    currentPlayer = currentPlayer.last();
  }

  private void nextTurn() {
    if (board.getDice().values().stream().anyMatch(die -> die.getDots() > 6)) {
      if (currentPlayer == RED) {
        result = RED_WON_BY_7_PLUS_MERGE;
      } else {
        result = BLACK_WON_BY_7_PLUS_MERGE;
      }
    }
    currentPlayer = currentPlayer.next();
    if (board.legalActions(currentPlayer).isEmpty()) {
      if (currentPlayer == RED) {
        result = BLACK_WON_BY_STALEMATE;
      } else {
        result = RED_WON_BY_STALEMATE;
      }
    }
  }

  public Board getBoard() {
    return board;
  }

  public Color getCurrentPlayer() {
    return currentPlayer;
  }

  public GameResult getResult() {
    return result;
  }
}
