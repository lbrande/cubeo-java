package se.lovebrandefelt.cubeo;

public class MergeAction implements ToAction {
  private Pos from;
  private Pos to;
  private Die resultDie;
  private Die removed;

  public MergeAction(Pos from, Pos to) {
    this.from = from;
    this.to = to;
  }

  @Override
  public void perform(Board board) {
    resultDie = board.getDice().get(to);
    removed = board.getDice().remove(from);
    resultDie.setDots(resultDie.getDots() + removed.getDots());
  }

  @Override
  public void undo(Board board) {
    board.getDice().put(from, removed);
    resultDie.setDots(resultDie.getDots() - removed.getDots());
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

    MergeAction that = (MergeAction) o;

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
