package se.lovebrandefelt.cubeo;

public class RandomAI implements AI {

  @Override
  public void performAction(Game game, Color player) {
    game.performAction(
        game.getBoard().legalActions(player).stream().findAny().get());
  }
}
