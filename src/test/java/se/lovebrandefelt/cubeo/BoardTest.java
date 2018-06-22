package se.lovebrandefelt.cubeo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.lovebrandefelt.cubeo.Color.RED;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class BoardTest {

  @Test
  void legalToMoveTest() {
    Board board = new Board();
    board.performAction(new AddAction(RED, new Pos(0, -2)));
    assertFalse(board.legalToMove(new Pos(0, -1)));
    assertTrue(board.legalToMove(new Pos(0, -2)));
  }

  @Test
  void legalAddPositionsTest() {
    Board board = new Board();
    Set<AddAction> legalAddActions = board.legalAddActions(RED);
    assertEquals(
        Stream.of(
            new AddAction(RED, new Pos(-1, 0)),
            new AddAction(RED, new Pos(1, 0)),
            new AddAction(RED, new Pos(0, 1)))
            .collect(Collectors.toSet()),
        legalAddActions);
  }

  @Test
  void legalMergesTest() {
    Board board = new Board();
    board.performAction(new AddAction(RED, new Pos(0, 1)));
    Set<MergeAction> legalMerges = board.legalMergeActions(RED);
    assertEquals(1, legalMerges.size());
    assertTrue(legalMerges.stream().anyMatch(action -> action.getFrom().equals(new Pos(0, 1))));
    assertEquals(
        Stream.of(new MergeAction(new Pos(0, 1), new Pos(0, 0))).collect(Collectors.toSet()),
        legalMerges
            .stream()
            .filter(action -> action.getFrom().equals(new Pos(0, 1)))
            .collect(Collectors.toSet()));
  }

  @Test
  void legalMovesTest() {
    Board board = new Board();
    Set<MoveAction> legalMoves = board.legalMoveActions(RED);
    assertEquals(2, legalMoves.size());
    assertTrue(legalMoves.stream().anyMatch(action -> action.getFrom().equals(new Pos(0, 0))));
    assertEquals(
        Stream.of(
            new MoveAction(new Pos(0, 0), new Pos(-1, -1)),
            new MoveAction(new Pos(0, 0), new Pos(1, -1)))
            .collect(Collectors.toSet()),
        legalMoves
            .stream()
            .filter(action -> action.getFrom().equals(new Pos(0, 0)))
            .collect(Collectors.toSet()));
  }

  @Test
  void legalMovesWith2DotsTest() {
    Board board = new Board();
    board.performAction(new AddAction(RED, new Pos(0, 1)));
    board.getDice().get(new Pos(0, 1)).setDots(2);
    Set<MoveAction> legalMoves = board.legalMoveActions(RED);
    assertEquals(2, legalMoves.size());
    assertTrue(legalMoves.stream().anyMatch(action -> action.getFrom().equals(new Pos(0, 1))));
    assertEquals(
        Stream.of(
            new MoveAction(new Pos(0, 1), new Pos(-1, -1)),
            new MoveAction(new Pos(0, 1), new Pos(1, -1)))
            .collect(Collectors.toSet()),
        legalMoves
            .stream()
            .filter(action -> action.getFrom().equals(new Pos(0, 1)))
            .collect(Collectors.toSet()));
  }
}
