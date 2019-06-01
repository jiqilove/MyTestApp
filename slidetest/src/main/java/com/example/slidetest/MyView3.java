package com.example.slidetest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * 作者 ： cnb on 2019-05-29
 * 功能 ：
 * 修改 ：
 */
public class MyView3 extends View {
    Paint mPaint;
    Paint mPaint2;
    Paint mPaintText;
    private float touchX;
    private float touchY;
    private int mTouchSlop;
    private float mWrodTextHeight;
    private float mPadding;


    public MyView3(Context context) {
        this(context, null);
    }


    public MyView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        init();
    }

    Paint.FontMetrics fontMetrics;
     String showText = "继续向左";

    private void init() {
        showText = "继续向左";
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);

        mPaint2 = new Paint();
        mPaint2.setColor(Color.WHITE);

        mPaintText = new Paint();
        mPaintText.setColor(Color.YELLOW);
        mPaintText.setTextSize(50);
        fontMetrics = mPaintText.getFontMetrics();
        mWrodTextHeight = fontMetrics.bottom - fontMetrics.ascent;

    }

    int rectW = 0;
    int rectH = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        height = heightSpecSize;
        left = width = widthSpecSize;

        rectH = height / 4;
        rectW = rectH / 3;
        top = (height - rectH) / 2f;
        bottom = (height + rectH) / 2f;
//        right = ;

        if (rectH>showText.length() * mWrodTextHeight){
            mPadding = (rectH - showText.length() * mWrodTextHeight) / 2;
        }else {
            mPadding=0;
        }


    }

    int width = 0;
    int height = 0;
    private float bottom;
    float left = 0;
    float top = 0;
    float right = 0;

    private float leftText = 0;
    private float topText = 0f;


    float offset = 0;

    //左右

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(left + offset, top, (left + rectW) + offset, bottom, mPaint);

        for (int i = 0; i < showText.length(); i++) {
            leftText = left + (rectW - mWrodTextHeight) / 2;
            topText = (height - rectH + mPadding) / 2 + mWrodTextHeight / 2;
            canvas.drawText(showText, i, i + 1, leftText + offset, topText + mWrodTextHeight * (i + 1), mPaintText);
        }

    }

    private static final String TAG = "MyView3";

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchX = event.getX();
                touchY = event.getY();


                Log.e(TAG, "onTouchEvent:" + touchX);
                Log.e(TAG, "onTouchEvent:Y:" + touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                float nowX = event.getX();
//                float nowY = event.getY();
                float offsetX = nowX - touchX; //
                Log.e(TAG, "onTouchEvent:" + offsetX);


                if (offsetX > 0 && offsetX < mTouchSlop) {
                    showText = "继续向右";
                } else if (offsetX < 0 && Math.abs(offsetX) < mTouchSlop) {
                    showText = "继续向左";
                }


                if (Math.abs(offsetX) > mTouchSlop) {
                    if (offsetX > 0 && offsetX > mTouchSlop) {
                        //向右
                        left = -rectW;
                        offset = offsetX;

                        if (Math.abs(offset) >= rectW) {
                            offset = rectW;
                            showText = "松开刷新";
                        }
                        mPadding = (rectH - showText.length() * mWrodTextHeight) / 2;
                        this.postInvalidate();

                        Log.e(TAG, "onTouchEvent:" + offset);
                        break;
                    }
                    if (offsetX < 0 && Math.abs(offsetX) > mTouchSlop) {
                        left = width;
                        offset = offsetX;
                        if (Math.abs(offset) >= rectW) {
                            offset = -rectW;
                            showText = "松开刷新";
                        }
                        //前提是 rectH > 文字总高度
                        mPadding = (rectH - showText.length() * mWrodTextHeight) / 2;

                        this.postInvalidate();
                        Log.e(TAG, "onTouchEvent:" + offset + "-----：" + mPadding);
                        break;
                    }

                }

                break;
            case MotionEvent.ACTION_UP:
                float upX = event.getX();
                if (Math.abs(upX - touchX) > mTouchSlop) {
                    showText = "";
                    startCircleProgressAnim((int) offset, 0);
                    this.postInvalidate();
                    return true;
                }
                break;
        }

//        postInvalidate();
        return true;
    }

    private void startCircleProgressAnim(int startoffset, int endoffset) {
        ValueAnimator animator = ValueAnimator.ofInt(startoffset, endoffset);
        animator.setDuration(400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                Log.e(TAG, "onAnimationUpdate:" + value);
                offset = value;

                postInvalidate();

            }
        });
        animator.start();
    }
}
