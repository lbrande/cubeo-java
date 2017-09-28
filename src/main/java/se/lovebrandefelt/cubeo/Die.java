package se.lovebrandefelt.cubeo;

public class Die {
  private int dots;
  private Color color;
  private Pos pos;

  public Die(Color color, Pos pos) {
    this(1, color, pos);
  }

  public Die(int dots, Color color, Pos pos) {
    this.dots = dots;
    this.color = color;
    this.pos = pos;
  }

  public int getDots() {
    return dots;
  }

  public void setDots(int dots) {
    this.dots = dots;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public Pos getPos() {
    return pos;
  }

  public void setPos(Pos pos) {
    this.pos = pos;
  }
}
