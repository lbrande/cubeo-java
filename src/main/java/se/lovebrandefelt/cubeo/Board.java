package se.lovebrandefelt.cubeo;

import static se.lovebrandefelt.cubeo.Color.BLACK;
import static se.lovebrandefelt.cubeo.Color.RED;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board {
  private Map<Pos, Die> dice;
  private Pos topLeft;
  private Pos bottomRight;

  public Board() {
    dice = new HashMap<>();
    add(RED, new Pos(0, 0));
    add(BLACK, new Pos(0, -1));
  }

  public void add(Color color, Pos pos) {
    Die die = new Die(color, pos);
    if (dice.isEmpty()) {
      topLeft = pos;
      bottomRight = pos;
    } else {
      topLeft = new Pos(Math.min(pos.getX(), topLeft.getX()), Math.min(pos.getY(), topLeft.getY()));
      bottomRight =
          new Pos(
              Math.max(pos.getX(), bottomRight.getX()), Math.max(pos.getY(), bottomRight.getY()));
    }
    dice.put(pos, die);
  }

  public void merge(Pos from, Pos to) {
    Die resultDie = dice.get(to);
    resultDie.setDots(resultDie.getDots() + dice.get(from).getDots());
    dice.remove(from);
  }

  public void move(Pos from, Pos to) {
    dice.put(to, dice.get(from));
    dice.remove(from);
  }

  public Set<Pos> adjacentPositions(Pos pos) {
    return Stream.of(
            pos.offsetBy(new Pos(-1, 0)),
            pos.offsetBy(new Pos(1, 0)),
            pos.offsetBy(new Pos(0, -1)),
            pos.offsetBy(new Pos(0, 1)))
        .collect(Collectors.toSet());
  }

  public boolean legalToMove(Pos pos) {
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

  public Set<Pos> legalAddPositions(Color color) {
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
        .collect(Collectors.toSet());
  }

  public Map<Pos, Set<Pos>> legalMerges(Color color) {
    Map<Pos, Set<Pos>> legalMerges = new HashMap<>();
    dice.values()
        .stream()
        .filter(die -> die.getColor() == color && legalToMove(die.getPos()))
        .forEach(
            die ->
                legalMerges.put(
                    die.getPos(),
                    adjacentPositions(die.getPos())
                        .stream()
                        .filter(pos -> dice.containsKey(pos) && dice.get(pos).getColor() == color)
                        .collect(Collectors.toSet())));
    return legalMerges;
  }

  public Map<Pos, Set<Pos>> legalMoves(Color color) {
    Map<Pos, Set<Pos>> legalMoves = new HashMap<>();
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
              legalMoves.put(die.getPos(), legalDestinations);
            });
    return legalMoves;
  }

  public Set<Pos> legalDestinations(Pos pos, Set<Pos> otherPositions) {
    Set<Pos> legalDestinations = new HashSet<>();
    Pos northWest = pos.offsetBy(new Pos(-1, -1));
    boolean northWestExists = otherPositions.contains(northWest);
    Pos north = pos.offsetBy(new Pos(0, -1));
    boolean northExists = otherPositions.contains(north);
    Pos northEast = pos.offsetBy(new Pos(1, -1));
    boolean northEastExists = otherPositions.contains(northEast);
    Pos west = pos.offsetBy(new Pos(-1, 0));
    boolean westExists = otherPositions.contains(west);
    Pos east = pos.offsetBy(new Pos(1, 0));
    boolean eastExists = otherPositions.contains(east);
    Pos southWest = pos.offsetBy(new Pos(-1, 1));
    boolean southWestExists = otherPositions.contains(southWest);
    Pos south = pos.offsetBy(new Pos(0, 1));
    boolean southExists = otherPositions.contains(south);
    Pos southEast = pos.offsetBy(new Pos(1, 1));
    boolean southEastExists = otherPositions.contains(southEast);
    if (!northWestExists && (northExists != westExists)) {
      legalDestinations.add(northWest);
    }
    if (!northEastExists && (northExists != eastExists)) {
      legalDestinations.add(northEast);
    }
    if (!southWestExists && (southExists != westExists)) {
      legalDestinations.add(southWest);
    }
    if (!southEastExists && (southExists != eastExists)) {
      legalDestinations.add(southEast);
    }
    if (!northExists && ((northWestExists && westExists) || (northEastExists && eastExists))) {
      legalDestinations.add(north);
    }
    if (!southExists && ((southWestExists && westExists) || (southEastExists && southEastExists))) {
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
