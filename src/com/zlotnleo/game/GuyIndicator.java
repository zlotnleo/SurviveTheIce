package com.zlotnleo.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class GuyIndicator
{
    Bitmap woodlogs, thermo;
    private boolean bitmapInited;
    private Guy guy;
    private Paint paint;
    private final int textColor = Color.rgb(255, 255, 255);

    GuyIndicator(Guy guy)
    {
        bitmapInited = false;
        this.guy = guy;
        paint = new Paint();
    }

    public void InitBitmap(Bitmap wood, Bitmap thermometer, float width, float height)
    {
        float k = Math.min(width/wood.getWidth(), height/wood.getHeight())/20;
        woodlogs = Bitmap.createScaledBitmap(wood, (int)(wood.getWidth()*k), (int)(wood.getHeight()*k), true);
        k = Math.min(width/thermometer.getWidth(), height/thermometer.getHeight())/20;
        thermo = Bitmap.createScaledBitmap(thermometer, (int)(thermometer.getWidth()*k), (int)(thermometer.getHeight()*k), true);
        bitmapInited = true;
    }

    public void draw(Canvas cnvs)
    {
        if(bitmapInited)
        {
            paint.setColor(textColor);

            cnvs.drawBitmap(woodlogs, 10, 10, paint);
            paint.setTextSize(woodlogs.getHeight());
            cnvs.drawText("" + guy.woodCarried + "/" + guy.MAX_WOOD_CAPACITY, 15 + woodlogs.getWidth(), woodlogs.getHeight(), paint);

            cnvs.drawBitmap(thermo, 10, 20 + woodlogs.getHeight(), paint);
            paint.setTextSize(thermo.getHeight());
            cnvs.drawText(""+(int)(guy.getTemp()*100)/100f, 15 + thermo.getWidth(), 20 + woodlogs.getHeight() + thermo.getHeight(), paint);
        }
    }
}
