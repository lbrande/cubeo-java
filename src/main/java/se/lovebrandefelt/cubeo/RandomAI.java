package se.lovebrandefelt.cubeo;

import java.util.Random;

public class RandomAI implements AI {
  private final Random random;

  public RandomAI() {
    random = new Random();
  }

  @Override
  public void performTurn(Game game, Color player) {
    int legalAddPositions = game.getBoard().legalAddPositions(player).size();
    int legalFroms =
        game.getBoard()
            .legalFroms(player)
            .stream()
            .mapToInt(pos -> game.getBoard().legalTos(pos).size())
            .sum();
    int move = random.nextInt(legalAddPositions + legalFroms);
    if (move < legalAddPositions) {
      game.add(game.getBoard().legalAddPositions(player).stream().findAny().get());
    } else {
      Pos from = game.getBoard().legalFroms(player).stream().findAny().get();
      if (!game.merge(from, game.getBoard().legalTos(from).stream().findAny().get())) {
        game.move(from, game.getBoard().legalTos(from).stream().findAny().get());
      }
    }
  }
}
