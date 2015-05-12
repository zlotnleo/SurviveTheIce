package com.zlotnleo.game;

import android.graphics.*;

public class FuelIndicator
{
    Bitmap flames;
    private boolean bitmapInited;
    private Fire fire;
    private Paint paint;
    private final int textColor = Color.rgb(255, 255, 255);

    FuelIndicator(Fire fire)
    {
        bitmapInited = false;
        this.fire = fire;
        paint = new Paint();
    }

    public void InitBitmap(Bitmap wood, float width, float height)
    {
        float k = Math.min(width/wood.getWidth(), height/wood.getHeight())/20;
        flames = Bitmap.createScaledBitmap(wood, (int)(wood.getWidth()*k), (int)(wood.getHeight()*k), true);
        bitmapInited = true;
    }

    public void draw(Canvas cnvs)
    {
        if(bitmapInited)
        {
            paint.setColor(textColor);
            paint.setTextSize(flames.getHeight());
            Rect r = new Rect();
            paint.getTextBounds("0.00", 0, 4, r);
            cnvs.drawText("" + (int) (fire.fuel * 100) / 100f, cnvs.getWidth() - 10 - r.width(), 10 + r.height(), paint);
            cnvs.drawBitmap(flames, cnvs.getWidth() - 20 - r.width() - flames.getWidth(), 10, paint);
        }
    }
}
