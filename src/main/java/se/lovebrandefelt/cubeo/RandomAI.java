package se.lovebrandefelt.cubeo;

public class RandomAI implements AI {

  @Override
  public void performAction(Game game) {
    game.performAction(
        game.legalActions().stream().findAny().get());
    if (game.continueMergeAction()) {
      game.performAction(
          game.legalActions().stream().findAny().get());
    }
  }
}
