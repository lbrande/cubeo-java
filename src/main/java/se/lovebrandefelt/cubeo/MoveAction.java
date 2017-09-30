package se.lovebrandefelt.cubeo;

public class MoveAction implements ToAction {
  private Pos from;
  private Pos to;

  public MoveAction(Pos from, Pos to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public void perform(Board board) {
    board.getDice().put(to, board.getDice().remove(from));
    board.getDice().get(to).setPos(to);
  }

  @Override
  public void undo(Board board) {
    board.getDice().put(from, board.getDice().remove(to));
    board.getDice().get(from).setPos(from);
  }

  @Override
  public Pos getFrom() {
    return from;
  }

  @Override
  public Pos getTo() {
    return to;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    MoveAction that = (MoveAction) o;

    return (from != null ? from.equals(that.from) : that.from == null)
        && (to != null ? to.equals(that.to) : that.to == null);
  }

  @Override
  public int hashCode() {
    int result = from != null ? from.hashCode() : 0;
    result = 31 * result + (to != null ? to.hashCode() : 0);
    return result;
  }
}
