package se.lovebrandefelt.cubeo;

import static se.lovebrandefelt.cubeo.Color.BLACK;
import static se.lovebrandefelt.cubeo.Color.RED;

import java.util.ArrayList;
import java.util.HashMap;
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
                      otherPositions.contains(adjacentPos) && !reachedPositions.contains(adjacentPos))
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
}
