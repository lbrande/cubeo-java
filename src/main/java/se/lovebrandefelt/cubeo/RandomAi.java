package se.lovebrandefelt.cubeo;

public class RandomAi implements Ai {
  @Override
  public void performAction(Game game) {
    game.legalActions().stream().findAny().ifPresent(game::performAction);
  }
}
