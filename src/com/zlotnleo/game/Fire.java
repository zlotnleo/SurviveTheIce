package com.zlotnleo.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Fire
{
    Vector2 pos;
    boolean centercoordInited;

    Bitmap[] sprites;
    boolean bitMapInited;
    Paint paint;
    float bitmapWidth, bitmapHeight;

    int frame;
    private double timeKeeper;
    private final double FRAME_CHNG_THRESHHOLD = .1f;

    public float fuel;
    private final float FUEL_USAGE = .12f;

    Fire()
    {
        pos=new Vector2();
        centercoordInited=false;
        sprites=new Bitmap[5];
        bitMapInited = false;
        paint = new Paint();
        frame = 0;
        fuel = 5;
    }

    public void loadSprites(Bitmap spriteSheet, float width, float height)
    {
        bitmapWidth = spriteSheet.getWidth()/sprites.length;
        bitmapHeight = spriteSheet.getHeight();

        float k = Math.min(width/bitmapWidth, height/bitmapHeight)/6;

        for(int i=0;i<sprites.length;i++)
            sprites[i]=Bitmap.createScaledBitmap(Bitmap.createBitmap(spriteSheet, i*spriteSheet.getWidth()/sprites.length, 0, spriteSheet.getWidth()/sprites.length, spriteSheet.getHeight()), (int)(bitmapWidth*k), (int)(bitmapHeight*k), true);

        bitMapInited = true;
        bitmapWidth = sprites[0].getWidth();
        bitmapHeight = sprites[0].getHeight();
    }


    public void initCenterPos(float x, float y)
    {
        pos.x = x;
        pos.y = y;
        centercoordInited = true;
    }

    private void switchFrame()
    {
        frame = (frame+1)%sprites.length;
    }

    public void tick(double dt, Guy guy)
    {
        timeKeeper += dt;
        if (timeKeeper >= FRAME_CHNG_THRESHHOLD)
        {
            timeKeeper -= FRAME_CHNG_THRESHHOLD;
            switchFrame();
        }

        fuel-=FUEL_USAGE*dt;
        if(Vector2.distanceBetween(getBasePos(), guy.getLegPos()) <= Math.min(pos.x*2, pos.y*2)/15)
        {
            fuel+=guy.woodCarried;
            guy.woodCarried = 0;
        }
    }

    public boolean FeelsGood()
    {
        return fuel > 0;
    }

    public float getBaseY()
    {
        return bitmapHeight*1/8;
    }

    public Vector2 getBasePos() { return new Vector2(bitmapWidth/2, getBaseY()); }

    public Vector2 getPos() {return new Vector2(pos.x, pos.y);}

    public void draw(Canvas cnvs)
    {
        if(centercoordInited)
            cnvs.drawBitmap(sprites[frame], pos.x-bitmapWidth/2, pos.y-bitmapHeight*7/8, paint);
    }

}
