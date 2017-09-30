package se.lovebrandefelt.cubeo;

public class MergeAI implements AI {
  @Override
  public void performAction(Game game, Color player) {
    if (game.getBoard().legalMergeActions(player).size() > 0) {
      game.performAction(
          game.getBoard().legalMergeActions(player).stream().findAny().get());
      if (game.getBoard().legalActions(player).size() <= 1) {
        game.undoAction();
      } else {
        return;
      }
    }

    if (game.getBoard().legalAddActions(player).size() > 0) {
      game.performAction(
          game.getBoard().legalAddActions(player).stream().findAny().get());
    } else {
      game.performAction(
          game.getBoard().legalMoveActions(player).stream().findAny().get());
    }
  }
}
