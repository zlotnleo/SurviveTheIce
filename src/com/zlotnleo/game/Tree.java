package com.zlotnleo.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Tree
{
    private Vector2 pos;
    private static Bitmap texture;
    private static float bitmapWidth, bitmapHeight;
    private static boolean bitmapInited;

    private static Bitmap dustcloud;
    private static float dustcloudWidth, dustcloudHeight;

    private Paint paint;

    private boolean shown;

    private static float centerx, centery;
    private static boolean centerposInited;
    private static float screenWidth, screenHeight;

    private float choppingProgress;
    private final float CHOPPING_THRESHOLD = 1.5f;

    public Tree(float x, float y)
    {
        Init(x, y);
    }

    public void Init(float x, float y)
    {
        pos = new Vector2(x, y);
        bitmapInited = false;
        centerposInited = false;
        paint = new Paint();
        shown = false;
    }

    public void show()
    {
        shown = true;
    }

    public static void loadSprites(Bitmap bitmap, Bitmap cloud, float width, float height)
    {
        screenWidth = width;
        screenHeight = height;

        bitmapHeight = bitmap.getHeight();
        bitmapWidth = bitmap.getWidth();
        dustcloudHeight = cloud.getHeight();
        dustcloudWidth = cloud.getWidth();

        float k = Math.min(width/bitmapWidth, height/bitmapHeight)/4;
        texture = Bitmap.createScaledBitmap(bitmap, (int)(bitmapWidth*k), (int)(bitmapHeight*k), true);

        k = Math.min(width/dustcloudWidth, height/dustcloudHeight)/4;
        dustcloud = Bitmap.createScaledBitmap(cloud, (int)(dustcloudWidth*k), (int)(dustcloudHeight*k), true);

        bitmapHeight = texture.getHeight();
        bitmapWidth = texture.getWidth();
        dustcloudHeight = dustcloud.getHeight();
        dustcloudWidth = dustcloud.getWidth();
        bitmapInited = true;
    }

    public static void initCenterPos(float centerx, float centery)
    {
        Tree.centerx = centerx;
        Tree.centery = centery;
        centerposInited = true;
    }

    public Vector2 getRootPos()
    {
        return new Vector2(pos.x + bitmapWidth/2, pos.y + bitmapHeight);
    }

    public void draw(Canvas cnvs)
    {
        cnvs.drawPoint(centerx + pos.x, centery + pos.y, paint);
        //Log.d("Tree draw", "bitMapInited:" + bitmapInited + "; centerposInited:" + centerposInited + "; show:" + shown);

        // AAARGH!!! NO WAY THIS IS GONNA STAY THIS WAY!!!
        bitmapInited = true;
        centerposInited = true;
        //don't worry, just a shitty piece of code. LOL

        if(bitmapInited && centerposInited && shown)
        {
            cnvs.drawBitmap(texture, pos.x+centerx-bitmapWidth/2, pos.y+centery-bitmapHeight/2, paint);
        }
    }

    public void drawDustCloud(Canvas cnvs)
    {
        if (choppingProgress != 0 && shown)
            cnvs.drawBitmap(dustcloud, pos.x + centerx - dustcloudWidth / 2, pos.y + centery - dustcloudHeight / 2, paint);
    }
    public void tick(double dt, Guy guy)
    {
        if(shown && Vector2.distanceBetween(getRootPos(), guy.getLegPos()) <= Math.min(screenHeight, screenWidth)/10)
        {
            //Log.d("Tree chopping", ""+choppingProgress+"/"+CHOPPING_THRESHOLD);
            choppingProgress += dt;
            if(choppingProgress >= CHOPPING_THRESHOLD)
            {
                if(guy.woodCarried < guy.MAX_WOOD_CAPACITY)
                    guy.woodCarried++;
                shown = false;
            }
            return;
        }
        choppingProgress = 0;
    }

    public boolean isShown()
    {
        return shown;
    }

}
