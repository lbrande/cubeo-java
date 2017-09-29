package se.lovebrandefelt.cubeo;

public enum Color {
  RED,
  BLACK;

  public Color next() {
    return Color.values()[(ordinal() + 1) % values().length];
  }
}
