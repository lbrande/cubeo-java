package se.lovebrandefelt.cubeo;

public class Die {
  private int dots;
  private Color color;
  private Pos pos;

  Die(Color color, Pos pos) {
    this(1, color, pos);
  }

  private Die(int dots, Color color, Pos pos) {
    this.dots = dots;
    this.color = color;
    this.pos = pos;
  }

  public int getDots() {
    return dots;
  }

  void setDots(int dots) {
    this.dots = dots;
  }

  public Color getColor() {
    return color;
  }

  public Pos getPos() {
    return pos;
  }

  void setPos(Pos pos) {
    this.pos = pos;
  }
}
