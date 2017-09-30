package se.lovebrandefelt.cubeo;

public class MergeAI implements AI {
  @Override
  public void performAction(Game game) {
    if (game.getBoard().legalMergeActions(game.getCurrentPlayer()).size() > 0) {
      game.performAction(
          game.getBoard().legalMergeActions(game.getCurrentPlayer()).stream().findAny().get());
    } else if (game.getBoard().legalAddActions(game.getCurrentPlayer()).size() > 0) {
      game.performAction(
          game.getBoard().legalAddActions(game.getCurrentPlayer()).stream().findAny().get());
    } else {
      game.performAction(
          game.getBoard().legalMoveActions(game.getCurrentPlayer()).stream().findAny().get());
    }
  }
}
