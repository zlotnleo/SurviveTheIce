package com.zlotnleo.game;

import android.content.Context;
import android.graphics.*;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class MyDraw extends View
{
    private long score;

    enum Screen
    {
        GAME, GAMEOVER
    }

    Paint paint;

    boolean touchProcessed;
    float touchXcoord, touchYcoord;

    long prevTime;
    double deltaTime;

    long startTime;

    Guy guy;
    Fire fire;

    Tree[] trees;
    final int MAX_TREE_COUNT = 4;

    GuyIndicator guyIndicator;
    FuelIndicator fuelIndicator;

    Bitmap background, gameover;
    Rect background_src, background_dst, gameover_src;

    Random random;

    Screen screen;

    Context cntxt;

    public MyDraw(Context context)
    {
        super(context);
        Init(context);
    }

    public void Init(Context context)
    {
        cntxt = context;
        screen = Screen.GAME;
        MyTimer timer = new MyTimer(10000, 17);
        timer.start();
        paint = new Paint();
        touchProcessed = true;
        touchXcoord = 0;
        touchYcoord = 0;

        background = BitmapFactory.decodeResource(context.getResources(), R.drawable.snowbackground);
        background_src = new Rect(0, 0, background.getWidth(), background.getHeight());
        gameover = BitmapFactory.decodeResource(context.getResources(), R.drawable.gameover);
        gameover_src = new Rect(0, 0, gameover.getWidth(), gameover.getHeight());

        guy = new Guy();
        fire = new Fire();
        trees = new Tree[MAX_TREE_COUNT];
        guyIndicator = new GuyIndicator(guy);
        fuelIndicator = new FuelIndicator(fire);

        random = new Random();

        paint.setStrokeWidth(5f);
        paint.setColor(Color.RED);

        prevTime = System.currentTimeMillis();
        startTime = prevTime;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        //super.onDraw(canvas)
        switch (screen)
        {
            case GAME:
                canvas.drawBitmap(background, background_src, background_dst, paint);

                for (int i = 0; i < MAX_TREE_COUNT; i++)
                    trees[i].draw(canvas);

                if (guy.getLegPos().y >= fire.getBaseY())
                {
                    fire.draw(canvas);
                    guy.draw(canvas);
                } else
                {
                    guy.draw(canvas);
                    fire.draw(canvas);
                }

                for (int i = 0; i < MAX_TREE_COUNT; i++)
                    trees[i].drawDustCloud(canvas);

                fuelIndicator.draw(canvas);
                guyIndicator.draw(canvas);

                break;
            case GAMEOVER:
                canvas.drawBitmap(gameover, gameover_src, background_dst, paint);
                paint.setTextSize(Math.min(getWidth(), getHeight())/10);
                String s = "Your score is "+score;
                paint.setColor(Color.rgb(0, 255, 0));
                Rect r = new Rect();
                paint.getTextBounds(s, 0, s.length(), r);
                canvas.drawText(s, getWidth()/2-r.width()/2, getHeight()-Math.min(getWidth(), getHeight())/5, paint);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(screen)
        {
            case GAME:
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_DOWN:
                        touchProcessed = false;
                        touchXcoord = event.getX();
                        touchYcoord = event.getY();
                        break;
                }
                break;
            case GAMEOVER:
                //Init(cntxt);
                break;
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        Bitmap[] hotLeft = new Bitmap[2];
        hotLeft[0] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.hotguywalk1left);
        hotLeft[1] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.hotguywalk2left);

        Bitmap[] coldLeft = new Bitmap[2];
        coldLeft[0] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.coldguywalk1left);
        coldLeft[1] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.coldguywalk2left);

        Bitmap[] hotRight = new Bitmap[2];
        hotRight[0] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.hotguywalk1right);
        hotRight[1] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.hotguywalk2right);

        Bitmap[] coldRight = new Bitmap[2];
        coldRight[0] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.coldguywalk1right);
        coldRight[1] = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.coldguywalk2right);

        Bitmap campfire = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.campfire);

        Bitmap woodlogs = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.woodlogs);
        Bitmap thermo = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.thermo);
        Bitmap flames = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.flames);
        Bitmap dustcloud = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.dustcloud);

        guy.loadSprites(coldLeft, hotLeft, coldRight, hotRight, w, h);
        fire.loadSprites(campfire, w, h);

        Bitmap tree = BitmapFactory.decodeResource(cntxt.getResources(), R.drawable.tree);
        Tree.loadSprites(tree, dustcloud, w, h);

        guyIndicator.InitBitmap(woodlogs, thermo, w, h);

        fuelIndicator.InitBitmap(flames, w, h);


        guy.initCenterPos(w / 2, h / 2);
        guy.initSpeed(Math.min(h,w)/3);
        fire.initCenterPos(w / 2, h / 2);
        Tree.initCenterPos(w / 2, h / 2);
        background_dst = new Rect(0, 0, w, h);
        for (int i = 0; i < MAX_TREE_COUNT; i++)
            trees[i] = new Tree(0, 0);
    }

    public boolean canGenTree(int index)
    {
        for (int i = 0; i < MAX_TREE_COUNT; i++)
            if (i != index && Vector2.distanceBetween(trees[i].getRootPos(), trees[index].getRootPos()) <= Math.min(getWidth(), getHeight())/12)
                return false;
        if (Vector2.distanceBetween(fire.getBasePos(), trees[index].getRootPos()) <= Math.min(getWidth(), getHeight())/4) return false;
        return true;
    }

    class MyTimer extends CountDownTimer
    {
        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyTimer(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished)
        {
            if (screen == Screen.GAME)
            {
                deltaTime = (double) (System.currentTimeMillis() - prevTime) / 1000;
                prevTime = System.currentTimeMillis();
                guy.tick(deltaTime, fire);
                fire.tick(deltaTime, guy);

                if (!touchProcessed)
                {
                    touchProcessed = true;
                    guy.setDestination(touchXcoord, touchYcoord);
                }

                for (int i = 0; i < MAX_TREE_COUNT; i++)   //TREE NULL CHECK SHOULD NOT BE EXISTENT!!
                {
                    if (trees[i] != null && trees[i].isShown()) trees[i].tick(deltaTime, guy);

                    if (trees[i] != null && !trees[i].isShown() && random.nextInt(500) < 10) //Generate new tree
                    {
                            trees[i].Init((float) random.nextInt(getWidth()) - getWidth() / 2, (float) random.nextInt(getHeight()) - getHeight() / 2);
                            if(canGenTree(i))
                                trees[i].show();
                    }
                }
                if (!guy.feelsGood() || !fire.FeelsGood())
                {
                    screen = Screen.GAMEOVER;
                    score = (System.currentTimeMillis() - startTime)/10000;
                }

            }
            else if (screen == Screen.GAMEOVER)
            {

            }
            invalidate();
        }

        @Override
        public void onFinish()
        {
            this.start();
        }
    }

}
