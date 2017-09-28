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
        legalAddPositions,
        Stream.of(new Pos(-1, 0), new Pos(1, 0), new Pos(0, 1)).collect(Collectors.toSet()));
  }

  @Test
  void legalMergesTest() {
    Board board = new Board();
    board.add(RED, new Pos(0, 1));
    Map<Pos, Set<Pos>> legalMerges = board.legalMerges(RED);
    assertEquals(1, legalMerges.size());
    assertEquals(1, legalMerges.get(new Pos(0, 1)).size());
    assertTrue(legalMerges.get(new Pos(0, 1)).contains(new Pos(0, 0)));
  }
}
