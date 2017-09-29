package se.lovebrandefelt.cubeo;

import static se.lovebrandefelt.cubeo.Color.RED;
import static se.lovebrandefelt.cubeo.GameResult.BLACK_WON_BY_7_PLUS_MERGE;
import static se.lovebrandefelt.cubeo.GameResult.BLACK_WON_BY_STALEMATE;
import static se.lovebrandefelt.cubeo.GameResult.RED_WON_BY_7_PLUS_MERGE;
import static se.lovebrandefelt.cubeo.GameResult.RED_WON_BY_STALEMATE;

import java.util.Map;
import java.util.Set;

public class Game {
  private Board board;
  private Color currentPlayer;
  private GameResult result;

  public Game() {
    board = new Board();
    currentPlayer = RED;
  }

  public void add(Pos pos) {
    if (board.legalAddPositions(currentPlayer).contains(pos)) {
      board.add(currentPlayer, pos);
      nextTurn();
    }
  }

  public void merge(Pos from, Pos to) {
    Map<Pos, Set<Pos>> legalMerges = board.legalMerges(currentPlayer);
    if (legalMerges.containsKey(from) && legalMerges.get(from).contains(to)) {
      board.merge(from, to);
      nextTurn();
    }
  }

  public void move(Pos from, Pos to) {
    Map<Pos, Set<Pos>> legalMoves = board.legalMoves(currentPlayer);
    if (legalMoves.containsKey(from) && legalMoves.get(from).contains(to)) {
      board.move(from, to);
      nextTurn();
    }
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
    if (board.legalAddPositions(currentPlayer).isEmpty() && board.legalFroms(currentPlayer).isEmpty()) {
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
