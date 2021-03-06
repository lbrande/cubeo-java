package se.lovebrandefelt.cubeo;

import static se.lovebrandefelt.cubeo.Color.BLACK;
import static se.lovebrandefelt.cubeo.Color.RED;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {
  private Map<Pos, Die> dice;
  private Pos topLeft;
  private Pos bottomRight;
  private Stack<Action> history;

  Board() {
    dice = new HashMap<>();
    history = new Stack<>();
    performAction(new AddAction(RED, new Pos(0, 0)));
    performAction(new AddAction(BLACK, new Pos(0, -1)));
  }

  void performAction(Action action) {
    action.perform(this);
    history.add(action);
    updateBounds();
  }

  void undoAction() {
    history.pop().undo(this);
    updateBounds();
  }

  private Set<Pos> adjacentPositions(Pos pos) {
    return Stream.of(
            pos.offsetBy(new Pos(-1, 0)),
            pos.offsetBy(new Pos(1, 0)),
            pos.offsetBy(new Pos(0, -1)),
            pos.offsetBy(new Pos(0, 1)))
        .collect(Collectors.toSet());
  }

  boolean legalToMove(Pos pos) {
    Set<Pos> otherPositions =
        dice.keySet()
            .stream()
            .filter(otherPos -> !otherPos.equals(pos))
            .collect(Collectors.toSet());
    ArrayList<Pos> reachedPositions = new ArrayList<>();
    reachedPositions.add(otherPositions.stream().findAny().orElseThrow(IllegalStateException::new));
    for (int i = 0; i < reachedPositions.size(); i++) {
      reachedPositions.addAll(
          adjacentPositions(reachedPositions.get(i))
              .stream()
              .filter(
                  adjacentPos ->
                      otherPositions.contains(adjacentPos)
                          && !reachedPositions.contains(adjacentPos))
              .collect(Collectors.toList()));
    }
    return reachedPositions.size() == otherPositions.size();
  }

  Set<AddAction> legalAddActions(Color color) {
    if (dice.values().stream().filter(die -> die.getColor() == color).count() < 6) {
      return dice.values()
          .stream()
          .filter(die -> die.getColor() == color)
          .flatMap(die -> adjacentPositions(die.getPos()).stream())
          .distinct()
          .filter(
              pos ->
                  !dice.containsKey(pos)
                      && adjacentPositions(pos)
                          .stream()
                          .noneMatch(
                              adjacentPos ->
                                  dice.containsKey(adjacentPos)
                                      && dice.get(adjacentPos).getColor() != color))
          .map(pos -> new AddAction(color, pos))
          .collect(Collectors.toSet());
    }
    return new HashSet<>();
  }

  Set<MergeAction> legalMergeActions(Color color) {
    Set<MergeAction> legalMerges = new HashSet<>();
    dice.values()
        .stream()
        .filter(die -> die.getColor() == color && legalToMove(die.getPos()))
        .forEach(
            die -> {
              Set<Pos> legalDestinations =
                  adjacentPositions(die.getPos())
                      .stream()
                      .filter(pos -> dice.containsKey(pos) && dice.get(pos).getColor() == color)
                      .collect(Collectors.toSet());
              legalMerges.addAll(
                  legalDestinations
                      .stream()
                      .map(pos -> new MergeAction(die.getPos(), pos))
                      .collect(Collectors.toSet()));
            });
    return legalMerges;
  }

  Set<MoveAction> legalMoveActions(Color color) {
    Set<MoveAction> legalMoves = new HashSet<>();
    dice.values()
        .stream()
        .filter(die -> die.getColor() == color && legalToMove(die.getPos()))
        .forEach(
            die -> {
              Set<Pos> otherPositions =
                  dice.keySet()
                      .stream()
                      .filter(otherPos -> !otherPos.equals(die.getPos()))
                      .collect(Collectors.toSet());
              Set<Pos> legalDestinations = new HashSet<>();
              legalDestinations.add(die.getPos());
              for (int i = 0; i < die.getDots(); i++) {
                legalDestinations =
                    legalDestinations
                        .stream()
                        .flatMap(pos -> legalDestinations(pos, otherPositions).stream())
                        .collect(Collectors.toSet());
              }
              legalDestinations.remove(die.getPos());
              legalMoves.addAll(
                  legalDestinations
                      .stream()
                      .map(pos -> new MoveAction(die.getPos(), pos))
                      .collect(Collectors.toSet()));
            });
    return legalMoves;
  }

  Set<Action> legalActions(Color color) {
    Set<Action> legalActions = new HashSet<>();
    legalActions.addAll(legalAddActions(color));
    legalActions.addAll(legalMergeActions(color));
    legalActions.addAll(legalMoveActions(color));
    return legalActions;
  }

  private Set<Pos> legalDestinations(Pos pos, Set<Pos> otherPositions) {
    Set<Pos> legalDestinations = new HashSet<>();
    Pos northWest = pos.offsetBy(new Pos(-1, -1));
    boolean northWestExists = otherPositions.contains(northWest);
    Pos north = pos.offsetBy(new Pos(0, -1));
    boolean northExists = otherPositions.contains(north);
    Pos west = pos.offsetBy(new Pos(-1, 0));
    boolean westExists = otherPositions.contains(west);
    if (!northWestExists && (northExists != westExists)) {
      legalDestinations.add(northWest);
    }
    Pos east = pos.offsetBy(new Pos(1, 0));
    boolean eastExists = otherPositions.contains(east);
    Pos northEast = pos.offsetBy(new Pos(1, -1));
    boolean northEastExists = otherPositions.contains(northEast);
    if (!northEastExists && (northExists != eastExists)) {
      legalDestinations.add(northEast);
    }
    Pos south = pos.offsetBy(new Pos(0, 1));
    boolean southExists = otherPositions.contains(south);
    Pos southWest = pos.offsetBy(new Pos(-1, 1));
    boolean southWestExists = otherPositions.contains(southWest);
    Pos southEast = pos.offsetBy(new Pos(1, 1));
    boolean southEastExists = otherPositions.contains(southEast);
    if (!southWestExists && (southExists != westExists)) {
      legalDestinations.add(southWest);
    }
    if (!southEastExists && (southExists != eastExists)) {
      legalDestinations.add(southEast);
    }
    if (!northExists && ((northWestExists && westExists) || (northEastExists && eastExists))) {
      legalDestinations.add(north);
    }
    if (!southExists && ((southWestExists && westExists) || (southEastExists && eastExists))) {
      legalDestinations.add(south);
    }
    if (!westExists && ((northWestExists && northExists) || (southWestExists && southExists))) {
      legalDestinations.add(west);
    }
    if (!eastExists && ((northEastExists && northExists) || (southEastExists && southExists))) {
      legalDestinations.add(east);
    }
    return legalDestinations;
  }

  private void updateBounds() {
    topLeft =
        dice.keySet()
            .stream()
            .reduce(
                (pos1, pos2) ->
                    new Pos(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY())))
            .orElseThrow(IllegalStateException::new);
    bottomRight =
        dice.keySet()
            .stream()
            .reduce(
                (pos1, pos2) ->
                    new Pos(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY())))
            .orElseThrow(IllegalStateException::new);
  }

  public Map<Pos, Die> getDice() {
    return dice;
  }

  public Pos getTopLeft() {
    return topLeft;
  }

  public Pos getBottomRight() {
    return bottomRight;
  }
}
