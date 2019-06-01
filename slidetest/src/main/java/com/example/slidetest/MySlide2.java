package com.example.slidetest;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 作者 ： cnb on 2019-05-29
 * 功能 ：
 * 修改 ：
 */
public class MySlide2 extends View {
    Paint mPaint;

    public MySlide2(Context context) {
        this(context, null);
    }

    public MySlide2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        mPaint = new Paint();
        mPaint.setTextSize(300);
        mPaint.setColor(Color.BLACK);
    }

    //            1)  EXACTLY：当宽高值设置为具体值时使用，如100DIP、match_parent等，此时取出的size是精确的尺寸；
//            2)  AT_MOST：当宽高值设置为wrap_content时使用，此时取出的size是控件最大可获得的空间；
//            3)  UNSPECIFIED：当没有指定宽高值时使用（很少见）。
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);




    }

    int offset = 0;
    boolean isFirst =true;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(500 + offset, 500 + offset, 100+offset*3, mPaint);
        if (isFirst){
             startCircleProgressAnim();
            isFirst=false;
        }

    }

    private static final String TAG = "MySlide2";

    private void startCircleProgressAnim() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                Log.e(TAG, "onAnimationUpdate:" +value );
                offset = value;
                invalidate();
                if (offset==100){
                    startCircleProgressAnim2();
                }
            }
        });
        animator.start();
    }

    private void startCircleProgressAnim2() {
        ValueAnimator animator = ValueAnimator.ofInt(100, 0);
        animator.setDuration(3000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                Log.e(TAG, "onAnimationUpdate:" +value );
                offset = value;
                invalidate();
                if (offset==0){
                    startCircleProgressAnim();
                }
            }
        });
        animator.start();
    }
}
