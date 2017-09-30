package se.lovebrandefelt.cubeo;

public class RandomAI implements AI {

  @Override
  public void performAction(Game game) {
    game.performAction(
        game.getBoard().legalActions(game.getCurrentPlayer()).stream().findAny().get());
  }
}
