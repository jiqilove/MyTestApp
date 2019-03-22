package com.example.mytestmodule.pull_to_refresh;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.mytestmodule.R;
import com.example.mytestmodule.mytest.EndlessRecyclerOnScrollListener;

public class SwipeRefreshLView extends SwipeRefreshLayout {

    private int slop;//最小移动距离
    private View mFooterView;
    private RecyclerView recyclerView;
    private OnLoadListener mOnLoadListener;

    public SwipeRefreshLView(@NonNull Context context) {

        super(context, null);
    }

    public SwipeRefreshLView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
//        mFooterView = LayoutInflater.from(context).inflate( R.layout.footer_view, null);
        mFooterView = View.inflate(context, R.layout.footer_view, null);
        slop = ViewConfiguration.get(context).getScaledTouchSlop();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (recyclerView == null) {
            // 判断容器有多少个孩子
            if (getChildCount() > 0) {
                // 判断第一个孩子是不是ListView（所以这个SwipeRefreshView容器只能包裹ListView）
                if (getChildAt(0) instanceof RecyclerView) {
                    // 创建ListView对象
                    recyclerView = (RecyclerView) getChildAt(0);

                    // 设置ListView的滑动监听
                    setListViewOnScroll();
                }
            }
        }

    }

    private void setListViewOnScroll() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if (canLoadMore()) {
                    // 加载数据
                    loadData();
                }
            }
        });
//        recyclerView.setOnScrollListener(new EndlessRecyclerOnScrollListener(){
//
//            @Override
//            public void onLoadNextPage(View view) {
//                super.onLoadNextPage(view);
//                loadData();
//            }
//        });
    }

    private float mDownY, mUpY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 移动的起点
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动过程中判断时候能下拉加载更多
                if (canLoadMore()) {
                    // 加载数据
                    loadData();
                }
                break;
            case MotionEvent.ACTION_UP:
                // 移动的终点
                mUpY = getY();
                break;
        }


        return super.dispatchTouchEvent(ev);
    }

    private void loadData() {

        if (mOnLoadListener != null) {
            setLoading(true);
            mOnLoadListener.onLoad();
        }


    }

    public void setLoading(boolean b) {
        this.isLoading = b;
        if (isLoading) {
            // 显示布局
            Log.e("cccc", "setLoading: ");
            ((SwipeRefreshLView) (recyclerView.getParent())).addView(mFooterView);
//            ((SwipeRefreshLView) (recyclerView.getParent())).addView(mFooterView,1);
//

//            addView(mFooterView,layoutParams);



        } else {
            // 隐藏布局
//            ((SwipeRefreshLView) (recyclerView.getParent())).removeView(mFooterView);
//            recyclerView.removeView(mFooterView);

            // 重置滑动的坐标
            mDownY = 0;
            mUpY = 0;
        }
//         重置滑动的坐标
        ((SwipeRefreshLView) recyclerView.getParent()).invalidate();


    }

    private boolean isLoading = false;

    private boolean canLoadMore() {
        //区分来：1、当前是否处于 正在加载状态，2、是否为最后一条item，3、是否达到最高的可上拉状态
        boolean status1 = false;
        boolean status2 = false;
        boolean status3 = false;
        //status1 是否为最后一条item，
        if (recyclerView != null && recyclerView.getAdapter() != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                if (lastVisibleItem == recyclerView.getAdapter().getItemCount() - 1) {
                    status1 = true;
                } else {
                    status1 = false;
                    Log.e("cnb", "canLoadMore: 3");
                }
            } else {
                Log.e("cnb", "canLoadMore:4 ");
            }
        }
        // status2  是否达到最高的可上拉状态
        status2 = ((mDownY - mUpY) >= slop);


        // status3  当前是否处于 正在加载状态
        status3 = !isLoading;

        return status1 && status2 && status3;
    }


    public interface OnLoadListener {
        void onLoad();
    }

    public void setOnLoadListener(OnLoadListener listener) {
        this.mOnLoadListener = listener;
    }

}
