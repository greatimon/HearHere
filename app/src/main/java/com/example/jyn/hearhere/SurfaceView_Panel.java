package com.example.jyn.hearhere;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JYN on 2017-08-17.
 */

public class SurfaceView_Panel extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    public static float surfaceWidth;       // screen width
    public static float surfaceHeight;      // screen height

    public List<SurfaceView_Heart> ballList = new ArrayList<SurfaceView_Heart>();
    public SurfaceView_Heart_DrawingThread thread;

    public SurfaceView_Panel(Context context) {
        super(context);
        getHolder().addCallback(this);       // set SurfaceHolder.Callback
        setOnTouchListener(this);            // set View.OnTouchListener
    }

    /** called immediately after the surface is first created */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("SurfaceView_panel", "Created");
        if(thread == null) {        // if the first execution
//            thread = new BallDrawingThread(this);
//            thread.start();
        } else {                    // if not the first execution
            // one time draw (no updatePosition here)
            synchronized(ballList) {
                Canvas canvas = holder.lockCanvas();
                for(SurfaceView_Heart ball : ballList)
                    ball.drawOnCanvas(canvas);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }

    /** called immediately after any structural changes (format or size) */
    @Override
    public void surfaceChanged(SurfaceHolder holder,
                               int format, int width, int height) {
        Log.d("SurfaceView_panel", "Changed");
        surfaceWidth = width;
        surfaceHeight = height;
    }

    /** called immediately before a surface is being destroyed */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("SurfaceView_panel", "Destroyed");
//        thread.stopSafely();
    }

    /** if user touches the screen,
     *  a ball will appear at the touched position */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_UP) {
//                synchronized(ballList) {
//                    ballList.add(new Ball(getResources(),
//                            (int)event.getX(), (int)event.getY()));
//                }
        }
        return true;
    }

    public void like_clicked_event(int x, int y) {
        a_room.animation_start_milTime = System.currentTimeMillis();
        synchronized(ballList) {
            ballList.add(new SurfaceView_Heart(getResources(), x, y, System.currentTimeMillis(), 1, 255));
        }
    }
}
