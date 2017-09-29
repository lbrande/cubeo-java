package se.lovebrandefelt.cubeo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.lovebrandefelt.cubeo.Color.RED;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class BoardTest {

  @Test
  void legalToMoveTest() {
    Board board = new Board();
    board.add(RED, new Pos(0, -2));
    assertFalse(board.legalToMove(new Pos(0, -1)));
    assertTrue(board.legalToMove(new Pos(0, -2)));
  }

  @Test
  void legalAddPositionsTest() {
    Board board = new Board();
    Set<Pos> legalAddPositions = board.legalAddPositions(RED);
    assertEquals(
        Stream.of(new Pos(-1, 0), new Pos(1, 0), new Pos(0, 1)).collect(Collectors.toSet()),
        legalAddPositions);
  }

  @Test
  void legalMergesTest() {
    Board board = new Board();
    board.add(RED, new Pos(0, 1));
    Map<Pos, Set<Pos>> legalMerges = board.legalMerges(RED);
    assertEquals(1, legalMerges.size());
    assertTrue(legalMerges.containsKey(new Pos(0, 1)));
    assertEquals(
        Stream.of(new Pos(0, 0)).collect(Collectors.toSet()),
        legalMerges.get(new Pos(0, 1)));
  }

  @Test
  void legalMovesTest() {
    Board board = new Board();
    Map<Pos, Set<Pos>> legalMoves = board.legalMoves(RED);
    assertEquals(1, legalMoves.size());
    assertTrue(legalMoves.containsKey(new Pos(0, 0)));
    assertEquals(
        Stream.of(new Pos(-1, -1), new Pos(1, -1)).collect(Collectors.toSet()),
        legalMoves.get(new Pos(0, 0)));
  }

  @Test
  void legalMovesWith2DotsTest() {
    Board board = new Board();
    board.add(RED, new Pos(0, 1));
    board.getDice().get(new Pos(0, 1)).setDots(2);
    Map<Pos, Set<Pos>> legalMoves = board.legalMoves(RED);
    assertEquals(1, legalMoves.size());
    assertTrue(legalMoves.containsKey(new Pos(0, 1)));
    assertEquals(
        Stream.of(new Pos(-1, -1), new Pos(1, -1)).collect(Collectors.toSet()),
        legalMoves.get(new Pos(0, 1)));
  }
}
