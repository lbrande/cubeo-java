package se.lovebrandefelt.cubeo;

public interface Action {
  void perform(Board board);

  void undo(Board board);

  Pos getFrom();
}
