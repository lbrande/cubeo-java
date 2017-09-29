package se.lovebrandefelt.cubeo;

import static se.lovebrandefelt.cubeo.Color.RED;

import java.util.Map;
import java.util.Set;

public class Game {
  private Board board;
  private Color currentPlayer;

  public Game() {
    board = new Board();
    currentPlayer = RED;
  }

  public void add(Pos pos) {
    if (board.legalAddPositions(currentPlayer).contains(pos)) {
      board.add(currentPlayer, pos);
      currentPlayer = currentPlayer.next();
    }
  }

  public void merge(Pos from, Pos to) {
    Map<Pos, Set<Pos>> legalMerges = board.legalMerges(currentPlayer);
    if (legalMerges.containsKey(from) && legalMerges.get(from).contains(to)) {
      board.merge(from, to);
      currentPlayer = currentPlayer.next();
    }
  }

  public void move(Pos from, Pos to) {
    Map<Pos, Set<Pos>> legalMoves = board.legalMoves(currentPlayer);
    if (legalMoves.containsKey(from) && legalMoves.get(from).contains(to)) {
      board.move(from, to);
      currentPlayer = currentPlayer.next();
    }
  }

  public Board getBoard() {
    return board;
  }

  public Color getCurrentPlayer() {
    return currentPlayer;
  }
}
