package com.example.slidetest;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 作者 ： cnb on 2019-05-27
 * 功能 ：
 * 修改 ：
 */
public class MySlide extends LinearLayout {
    private int downX, moveX, moved;
    private Scroller scroller = new Scroller(getContext());
    private boolean haveShowRight = false;
    public static MySlide swipeMenu;

    public MySlide(Context context) {
        this(context,null);
    }

    public MySlide(Context context,  AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {

    }




    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (swipeMenu != null&&swipeMenu==this) {
            swipeMenu.closeMenus();
            swipeMenu = null;
        }
    }

    //缓慢滚动到指定位置
    private void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        //1000ms内滑动destX，效果就是慢慢滑动
        scroller.startScroll(scrollX, 0, delta, 0, 300);
        invalidate();
    }

    public void closeMenus() {
        smoothScrollTo(0, 0);
        haveShowRight = false;
    }

    public static void closeMenu() {
        swipeMenu.closeMenus();
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scroller.isFinished()) {
            return false;
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getRawX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (swipeMenu != null && swipeMenu == this && haveShowRight) {
                    closeMenu();
                    return true;
                }
                moveX = (int) ev.getRawX();

                moved = moveX - downX;

                if (haveShowRight) {
                    moved -= getChildAt(1).getMeasuredWidth();
                }
                scrollTo(-moved, 0);
                if (getScrollX() <= 0) {
                    scrollTo(0, 0);
                } else if (getScrollX() >= getChildAt(1).getMeasuredWidth()) {
                    scrollTo(getChildAt(1).getMeasuredWidth(), 0);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (swipeMenu != null) {
                    closeMenu();
                }
                //这个判断是的当向左滑动 右边控件距离的 1/8 就把整一个控件出来
                if (getScrollX() >= getChildAt(1).getMeasuredWidth() / 10) {
                    haveShowRight = true;
                    swipeMenu = this;
                    smoothScrollTo(getChildAt(1).getMeasuredWidth(), 0);
                } else {
                    haveShowRight = false;
                    smoothScrollTo(0, 0);
                }

                break;

        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

//    @Override
//    public LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return new MarginLayoutParams(getContext(), attrs);
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        View child = getChildAt(0);
        int margin = ((MarginLayoutParams) child.getLayoutParams()).topMargin + ((MarginLayoutParams) child.getLayoutParams()).bottomMargin;
        setMeasuredDimension(width, getChildAt(0).getMeasuredHeight() + margin);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            if (i == 0) {
                child.layout(l, t, r, b);
            } else if (i == 1) {
                child.layout(r, t, r + child.getMeasuredWidth(), b);
            }
        }
    }
}
