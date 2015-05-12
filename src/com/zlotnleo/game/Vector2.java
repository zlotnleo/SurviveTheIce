package com.zlotnleo.game;

public class Vector2
{
    public float x, y;
    Vector2()
    {
        x = 0;
        y = 0;
    }
    Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
    public void plusV(Vector2 v2)
    {
        x+=v2.x;
        y+=v2.y;
    }

    public void minusV(Vector2 v2)
    {
        x-=v2.x;
        y-=v2.y;
    }

    public static Vector2 VplusV(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x + v2.x, v1.y + v2.y);
    }

    public static Vector2 VminusV(Vector2 v1, Vector2 v2)
    {
        return new Vector2(v1.x - v2.x, v1.y - v2.y);
    }

    public float getLength()
    {
        return (float)Math.sqrt(x*x+y*y);
    }

    public void normalize()
    {
        float len = this.getLength();
        if(len>0.000001)
        {
            x/=len;
            y/=len;
        }
    }

    public void setLength(float length)
    {
        if(this.getLength() > 0.000001)
        {
            this.normalize();
            x*=length;
            y*=length;
        }
    }

    public float distanceTo(Vector2 v2)
    {
        return Vector2.VminusV(this, v2).getLength();
    }

    public static float distanceBetween(Vector2 v1, Vector2 v2)
    {
        return v1.distanceTo(v2);
    }
}
