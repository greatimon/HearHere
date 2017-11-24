package com.example.jyn.hearhere;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Random;

/**
 * Created by JYN on 2017-08-17.
 */

public class SurfaceView_Heart {

    private float xPos;         // position(x)
    private float yPos;         // position(y)
    private int xVel;           // velocity(x)
    private int yVel;           // velocity(y)
    private Bitmap bitmap;      // image of the ball
    private Paint paint;
    private long birth_time;
    private int life;
    private int alpha;


    static int time_interval = 200;
    static int default_alpha = 250;
    static int alpha_decrease_unit = 20;
    static int ball_life_increase_unit = 1;
    Random random;

    // 볼 리스트
    private static final int ran[]= {
            R.drawable.heart0, R.drawable.heart1, R.drawable.heart2, R.drawable.heart3, R.drawable.heart4,
            R.drawable.heart5, R.drawable.heart6, R.drawable.heart7, R.drawable.heart8, R.drawable.heart9,
            R.drawable.heart10, R.drawable.heart11, R.drawable.heart12, R.drawable.heart13, R.drawable.heart14,
            R.drawable.heart15, R.drawable.heart16, R.drawable.heart17, R.drawable.heart18, R.drawable.heart19,
            R.drawable.heart20, R.drawable.heart21, R.drawable.heart22, R.drawable.heart23
    };

    /** @param resource
     *  @param xStart    initial position(x) of the ball
     *  @param yStart    initial position(y) of the ball */
    public SurfaceView_Heart(Resources resource, int xStart, int yStart, long birth_time, int life, int alpha) {

        // 볼 생성 시간(long)
        this.birth_time = birth_time;
        Log.d("surfaceView_log", "birth_time: " + birth_time);

        // 볼 수명(int)
        this.life = life;

        // 볼 투명도(int)
        this.alpha = alpha;

        // read image file for the ball
        bitmap = BitmapFactory.decodeResource(resource, ran[(int)(Math.random() * 24)]);

        // calculate upper-left coordinate of the ball
        xPos = xStart - bitmap.getWidth()  / 2;
        yPos = yStart - bitmap.getHeight() / 2;

        // set velocity randomly
        random = new Random();
        while(xVel == 0 && yVel == 0) {
            xVel = random.nextInt(10) - 4;
            yVel = random.nextInt(10) - 4;
        }
        paint = new Paint();
    }

    /** render bitmap at current location */
    public void drawOnCanvas(Canvas canvas) {

        // 방법1. 비트맵의 알파값이 적용되지 않았을 때의 방법
//        canvas.drawBitmap(bitmap, xPos, yPos, null);

        // 방법2. 비트맵의 알파값을 단계별로 적용할 때의 방법
        paint.setAlpha(alpha);
        canvas.drawBitmap(bitmap, xPos, yPos, paint);
    }

    /** update position
     *  ( e.g. +1vel = + 50px/s )
     *  ( e.g. -3vel = -150px/s ) */
    public void updatePosition(long elapsedTime) {
//        xPos += xVel * (elapsedTime / 15f);
//        yPos += yVel * (elapsedTime / 15f);
//        checkBoundary();
        if(life < 6) {
            xPos -= random.nextInt(2);
        }
        if(life > 6) {
            xPos += random.nextInt(2);
        }

        yPos -= (23-(life-1)) - (life-random.nextInt(4));

//        checkBoundary();
    }

    /** collision detection to the screen's boundary */
    private void checkBoundary() {
        // check for left or right boundary
        if(xPos <= 0) {
            xVel *= -1;
            xPos = 0;
        } else if(xPos + bitmap.getWidth() >= SurfaceView_Panel.surfaceWidth) {
            xVel *= -1;
            xPos = SurfaceView_Panel.surfaceWidth - bitmap.getWidth();
        }

        // check for top or bottom boundary
        if(yPos <= 0) {
            yVel *= -1;
            yPos = 0;
        } else if(yPos + bitmap.getHeight() >= SurfaceView_Panel.surfaceHeight) {
            yVel *= -1;
            yPos = SurfaceView_Panel.surfaceHeight - bitmap.getHeight();
        }
    }

    public long getBirth_time() {
        return birth_time;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
    }

}
