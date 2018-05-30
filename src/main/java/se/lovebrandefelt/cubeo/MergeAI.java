package se.lovebrandefelt.cubeo;

public class MergeAI implements AI {
  @Override
  public void performAction(Game game) {
    if (game.legalActions().stream().filter(action -> action instanceof MergeAction).count() > 0) {
      game.performAction(
          game.legalActions()
              .stream()
              .filter(action -> action instanceof MergeAction)
              .findAny()
              .get());
      if (game.legalActions().size() <= 1) {
        game.undoAction();
      } else {
        return;
      }
    }

    if (game.legalActions().stream().filter(action -> action instanceof AddAction).count() > 0) {
      game.performAction(
          game.legalActions()
              .stream()
              .filter(action -> action instanceof AddAction)
              .findAny()
              .get());
    } else {
      game.performAction(
          game.legalActions()
              .stream()
              .filter(action -> action instanceof MoveAction)
              .findAny()
              .get());
    }
  }
}
