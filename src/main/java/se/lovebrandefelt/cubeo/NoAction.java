package se.lovebrandefelt.cubeo;

public class NoAction implements Action {
  private Pos pos;

  public NoAction(Pos pos) {
    this.pos = pos;
  }

  @Override
  public void perform(Board board) {
  }

  @Override
  public void undo(Board board) {
  }

  @Override
  public Pos getFrom() {
    return pos;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    NoAction noAction = (NoAction) o;

    return pos != null ? pos.equals(noAction.pos) : noAction.pos == null;
  }

  @Override
  public int hashCode() {
    return pos != null ? pos.hashCode() : 0;
  }
}
