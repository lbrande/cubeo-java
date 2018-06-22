package se.lovebrandefelt.cubeo;

public class MergeAi implements Ai {
  @Override
  public void performAction(Game game) {
    game.legalActions()
        .stream()
        .filter(action -> action instanceof MergeAction)
        .findAny()
        .ifPresentOrElse(
            action -> {
              game.performAction(action);
              if (game.legalActions().size() <= 1) {
                game.undoAction();
                game.legalActions()
                    .stream()
                    .filter(action1 -> action1 instanceof AddAction)
                    .findAny()
                    .ifPresentOrElse(
                        game::performAction,
                        () ->
                            game.legalActions()
                                .stream()
                                .filter(action1 -> action1 instanceof MoveAction)
                                .findAny()
                                .ifPresent(game::performAction));
              }
            },
            () ->
                game.legalActions()
                    .stream()
                    .filter(action1 -> action1 instanceof AddAction)
                    .findAny()
                    .ifPresentOrElse(
                        game::performAction,
                        () ->
                            game.legalActions()
                                .stream()
                                .filter(action1 -> action1 instanceof MoveAction)
                                .findAny()
                                .ifPresent(game::performAction)));
  }
}
