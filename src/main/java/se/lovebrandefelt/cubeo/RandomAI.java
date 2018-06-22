package se.lovebrandefelt.cubeo;

public class RandomAI implements AI {
  @Override
  public void performAction(Game game) {
    game.legalActions().stream().findAny().ifPresent(game::performAction);
  }
}
