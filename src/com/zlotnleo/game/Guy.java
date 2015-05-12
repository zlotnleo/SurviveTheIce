package com.zlotnleo.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Guy
{
    private Vector2 pos;
    private Vector2 velocity;
    private float spdMagnitude = 100f;
    boolean walking;

    private Vector2 destination;

    private Bitmap[] coldLeft;
    private Bitmap[] hotLeft;
    private Bitmap[] coldRight;
    private Bitmap[] hotRight;
    int frame;

    private int bitmapWidth;
    private int bitmapHeight;

    private double timeKeeper;
    private final double FRAME_CHNG_THRESHHOLD = .1f;
    private float temperature;
    private final float TEMPERATURE_THRESHOLD=21f;
    private final float TEMP_UP_BASE = .05f;
    private final float TEMP_DOWN_BASE = .005f;

    private Paint paint;
    private float centerx, centery;
    private boolean bitmapInited;
    private boolean centercoordInited;

    public int woodCarried;
    public final int MAX_WOOD_CAPACITY = 3;
    private float screenWidth, screenHeight;

    public void initSpeed(int min)
    {
        spdMagnitude=min;
    }

    enum Direction
    {
        RIGHT, LEFT
    }
    Direction dir;

    public Guy()
    {
        pos = new Vector2(30, 30);
        velocity = new Vector2();
        temperature = 36.6f;
        dir = Direction.RIGHT;
        frame = 0;
        timeKeeper = 0;
        paint = new Paint();
        bitmapInited = false;
        centercoordInited = false;
        destination = new Vector2(pos.x, pos.y);
        walking = false;
        woodCarried = 0;

        coldLeft = new Bitmap[2];
        coldRight = new Bitmap[2];
        hotLeft = new Bitmap[2];
        hotRight = new Bitmap[2];

    }

    public void loadSprites(Bitmap[] coldLeft, Bitmap[] hotLeft, Bitmap[] coldRight, Bitmap[] hotRight, float width, float height)
    {
        bitmapHeight=coldLeft[0].getHeight();
        bitmapWidth=coldLeft[0].getWidth();

        float k = Math.min(width/bitmapWidth, height/bitmapHeight)/6;

        this.coldLeft[0] = Bitmap.createScaledBitmap(coldLeft[0], (int)(bitmapWidth*k), (int)(bitmapHeight*k), true);
        this.coldLeft[1] = Bitmap.createScaledBitmap(coldLeft[1], (int)(bitmapWidth*k), (int)(bitmapHeight*k), true);
        this.hotLeft[0] = Bitmap.createScaledBitmap(hotLeft[0], (int)(bitmapWidth*k), (int)(bitmapHeight*k), true);
        this.hotLeft[1] = Bitmap.createScaledBitmap(hotLeft[1], (int)(bitmapWidth*k), (int)(bitmapHeight*k), true);
        this.coldRight[0] = Bitmap.createScaledBitmap(coldRight[0], (int)(bitmapWidth*k), (int)(bitmapHeight*k), true);
        this.coldRight[1] = Bitmap.createScaledBitmap(coldRight[1], (int)(bitmapWidth*k), (int)(bitmapHeight*k), true);
        this.hotRight[0] = Bitmap.createScaledBitmap(hotRight[0], (int)(bitmapWidth*k), (int)(bitmapHeight*k), true);
        this.hotRight[1] = Bitmap.createScaledBitmap(hotRight[1], (int)(bitmapWidth*k), (int)(bitmapHeight*k), true);

        bitmapHeight=this.coldLeft[0].getHeight();
        bitmapWidth=this.coldLeft[0].getWidth();
        bitmapInited = true;
    }

    public void initCenterPos(float centerx, float centery)
    {
        this.centerx = centerx;
        this.centery = centery;
        screenWidth = centerx*2;
        screenHeight = centery*2;
        centercoordInited = true;
    }

    public void setDestination(float x, float y)
    {
        destination.x = x - centerx;
        destination.y = y - centery;
    }

    public void tick(double dt, Fire fire)
    {
        if(walking)
        {
            timeKeeper += dt;
            if (timeKeeper >= FRAME_CHNG_THRESHHOLD)
            {
                timeKeeper -= FRAME_CHNG_THRESHHOLD;
                switchFrame();
            }
        }
        if(pos.distanceTo(destination) >= 15)
        {
            velocity = Vector2.VminusV(destination, pos);
            velocity.setLength((float)(spdMagnitude*dt));
            pos.plusV(velocity);

          //  Log.d("DEBUG", "velocity = ("+velocity.x+";"+velocity.y+")");

            if(velocity.x >= 0.3)
                dir = Direction.RIGHT;
            else if(velocity.x <= -0.3)
                dir = Direction.LEFT;

            walking = true;
        }
        else
            walking = false;

        float dst = Vector2.distanceBetween(fire.getBasePos(), getLegPos());
        float radius =  Math.min(screenHeight, screenWidth)/12;
        if(dst <= radius)
            temperature += (radius-dst)*TEMP_UP_BASE*dt;
        else
            temperature += (radius-dst)*TEMP_DOWN_BASE*dt;
    }

    public boolean feelsGood()
    {
        return (temperature >= 13) && (temperature <= 48);
    }

    private void switchFrame()
    {
        frame=(frame+1)%2;
    }

    public boolean isHot()
    {
        return temperature>=TEMPERATURE_THRESHOLD;
    }

    public float getTemp(){ return temperature;}

    private Bitmap getBitmap()
    {
        Bitmap bit=null;
        switch (dir)
        {
            case RIGHT:
                if(isHot())
                    bit=hotRight[frame];
                else
                    bit=coldRight[frame];
            break;
            case LEFT:
                if(isHot())
                    bit=hotLeft[frame];
                else
                    bit=coldLeft[frame];
        }
        return bit;
    }

    public Vector2 getLegPos() {return new Vector2(pos.x + bitmapWidth/2, pos.y+bitmapHeight/2);}

    public void draw(Canvas cnvs)
    {
            if(bitmapInited && centercoordInited)
            {
                cnvs.drawBitmap(getBitmap(),centerx+pos.x-bitmapWidth/2,centery+pos.y-bitmapHeight/2,paint);
            }
    }
}
