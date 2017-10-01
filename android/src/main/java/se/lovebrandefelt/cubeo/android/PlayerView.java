package se.lovebrandefelt.cubeo.android;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import se.lovebrandefelt.cubeo.Color;

public class PlayerView extends View {

  private Color player;

  public PlayerView(Context context) {
    super(context);
  }

  public PlayerView(Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  protected void onDraw(Canvas canvas) {
  }
}
