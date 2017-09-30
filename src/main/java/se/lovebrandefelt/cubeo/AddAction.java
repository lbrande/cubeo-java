package se.lovebrandefelt.cubeo;

public class AddAction implements Action {
  private Color color;
  private Pos pos;

  public AddAction(Color color, Pos pos) {
    this.color = color;
    this.pos = pos;
  }

  @Override
  public void perform(Board board) {
    Die die = new Die(color, pos);
    board.getDice().put(pos, die);
  }

  @Override
  public void undo(Board board) {
    board.getDice().remove(pos);
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

    AddAction addAction = (AddAction) o;

    return color == addAction.color
        && (pos != null ? pos.equals(addAction.pos) : addAction.pos == null);
  }

  @Override
  public int hashCode() {
    int result = color != null ? color.hashCode() : 0;
    result = 31 * result + (pos != null ? pos.hashCode() : 0);
    return result;
  }
}
