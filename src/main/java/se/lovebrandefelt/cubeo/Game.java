package se.lovebrandefelt.cubeo;

import static se.lovebrandefelt.cubeo.Color.RED;
import static se.lovebrandefelt.cubeo.GameResult.BLACK_WON_BY_7_PLUS_MERGE;
import static se.lovebrandefelt.cubeo.GameResult.BLACK_WON_BY_STALEMATE;
import static se.lovebrandefelt.cubeo.GameResult.RED_WON_BY_7_PLUS_MERGE;
import static se.lovebrandefelt.cubeo.GameResult.RED_WON_BY_STALEMATE;

import java.util.Set;
import java.util.stream.Collectors;

public class Game {
  private Board board;
  private Color currentPlayer;
  private GameResult result;
  private Set<Action> continueMergeActions;

  public Game() {
    board = new Board();
    currentPlayer = RED;
  }

  public Set<Action> legalActions() {
    if (continueMergeAction()) {
      return continueMergeActions;
    }
    return board.legalActions(currentPlayer);
  }

  public boolean performAction(Action action) {
    if ((continueMergeAction() && continueMergeActions.contains(action))
        || (!continueMergeAction()
        && board.legalActions(currentPlayer).contains(action)
        && result == null)) {
      board.performAction(action);
      if (action instanceof MergeAction
          && board
          .legalActions(currentPlayer)
          .stream()
          .anyMatch(
              moveAction ->
                  moveAction instanceof MoveAction
                      && moveAction.getFrom().equals(((MergeAction) action).getTo()))) {
        continueMergeActions =
            board
                .legalActions(currentPlayer)
                .stream()
                .filter(
                    moveAction ->
                        moveAction instanceof MoveAction
                            && moveAction.getFrom().equals(((MergeAction) action).getTo()))
                .collect(Collectors.toSet());
        continueMergeActions.add(new NoAction(((MergeAction) action).getTo()));
      } else {
        continueMergeActions = null;
        nextTurn();
      }
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

  public boolean continueMergeAction() {
    return continueMergeActions != null;
  }
}
