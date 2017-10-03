package se.lovebrandefelt.cubeo;

public class Pos {
  private final int x;
  private final int y;

  public Pos(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  Pos offsetBy(Pos offset) {
    return new Pos(x + offset.x, y + offset.y);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Pos pos = (Pos) o;

    return x == pos.x && y == pos.y;
  }

  @Override
  public int hashCode() {
    int result = x;
    result = 31 * result + y;
    return result;
  }

  @Override
  public String toString() {
    return "Pos{" + "x=" + x + ", y=" + y + '}';
  }
}
