package com.example.jyn.hearhere;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Iterator;

/**
 * Created by JYN on 2017-08-17.
 */

public class SurfaceView_Heart_DrawingThread extends Thread {

    private boolean shouldStop = false;
    private SurfaceView_Panel panel;
    private SurfaceHolder holder;
    private Paint textPaint;
    private long lastDrawnTime;
    private long startTime;

    public SurfaceView_Heart_DrawingThread(SurfaceView_Panel panel) {
        this.panel = panel;
        holder = panel.getHolder();
        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(100f);
    }

    public void stopSafely() {
        shouldStop = true;
    }

    @Override
    public void run() {
        lastDrawnTime = System.currentTimeMillis();
        startTime = System.currentTimeMillis();
        Log.d("surfaceView_log", "startTime:" + startTime);


        while(!shouldStop) {
            Canvas canvas = holder.lockCanvas();      // obtain canvas

            int alpha_decrease_count = 1;
            float textWidth;
            float textHeight;
            float text_xPos = 0;
            float text_yPos = 0;

            // null if user presses the home button
            if(canvas != null) {
                // re-draw background
//                canvas.drawColor(Color.BLACK);
                canvas.drawColor(0, PorterDuff.Mode.CLEAR);

                // draw text (horizontally centered)
                String text = "# of Balls = " + panel.ballList.size();
                textWidth = textPaint.measureText(text);
                textHeight = textPaint.getTextSize();
                text_xPos = (canvas.getWidth() - textWidth) / 2;
                text_yPos = textHeight;
//                canvas.drawText(text, text_xPos, text_yPos, textPaint);

                // calculate elapsed time
                long currTime = System.currentTimeMillis();
                long elapsedTime = currTime - lastDrawnTime;

                // re-draw all balls
                // synchronized to avoid java.util.ConcurrentModificationException
                synchronized(panel.ballList) {

                    Iterator<SurfaceView_Heart> iter = panel.ballList.iterator();
                    while (iter.hasNext()) {
                        SurfaceView_Heart ball = iter.next();

                        // 볼 위치 업데이트
                        ball.updatePosition(elapsedTime);

                        // 현재 밀리세컨타임과 볼생성시 밀리세컨타임과의 차이값이,
                        // 볼의 누적라이프 숫자의 1초를 곱한 숫자의 계산값보다 크면
                        // 즉, 볼의 생성으로부터 1초가 증가할때마다
                        // 볼의 누적라이프를 1씩 증가시키고, alpha값을 누적라이프의 비율만큼 정비례하게 감소시킨다
                        // 그리고, 볼의 누적라이프가 10이 되면, alpha 값을 없애고, panel.ballList 에서 remove 시킨다
                        if(currTime-ball.getBirth_time() > SurfaceView_Heart.time_interval*ball.getLife()) {
                            // 원래 alpha의 최대값은 255이나, 25*10을 맞추기 위해 250으로 수정 설정
                            int alpha = SurfaceView_Heart.default_alpha - (SurfaceView_Heart.alpha_decrease_unit * ball.getLife());

                            ball.setAlpha(alpha);
                            ball.setLife(ball.getLife() + SurfaceView_Heart.ball_life_increase_unit);

                            // alpha 값이 0이면 ballList에서 제거
                            if(alpha <= 0) {
                                iter.remove();
                            }
                            else {
                                ball.drawOnCanvas(canvas);
                            }
                        }
                        else {
                            ball.drawOnCanvas(canvas);
                        }
                    }
                }

                // apply the change
                holder.unlockCanvasAndPost(canvas);

                // refresh lastDrawnTime
                lastDrawnTime = currTime;
            }

            if(panel.ballList.size() == 0) {
                Log.d("surfaceView_log", "panel.ballList.size():" + panel.ballList.size());
                String text = "# of Balls = " + panel.ballList.size();
//                canvas.drawText(text, text_xPos, text_yPos, textPaint);
                stopSafely();
                a_room.surfaceview_handler.sendEmptyMessage(1);
            }
        }
    }

}
