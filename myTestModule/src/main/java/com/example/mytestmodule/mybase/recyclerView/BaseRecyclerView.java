package com.example.mytestmodule.mybase.recyclerView;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import com.example.mytestmodule.mybase.divider.MyDivider;

import java.util.List;

public abstract class BaseRecyclerView<T> {

    private Context context;
    private RecyclerView recyclerView;
    private BaseRecycleViewAdapter<T> adapter;
    private List<T> datas;


    public BaseRecyclerView(Context context, RecyclerView recyclerView, int layout) {
        this.context = context;
        this.recyclerView = recyclerView;

        adapter = new BaseRecycleViewAdapter<T>(context, setDatas(datas), layout) {
            @Override
            protected void bindData(BaseViewHolder baseViewHolder, T date, int position) {
                onBindView(baseViewHolder, date, position);
            }
        };
        recyclerView.setAdapter(adapter);

    }


    public abstract List<T> setDatas(List<T> datas) ;


    //======recyclerView的 配置方法==========

    /**
     *
     * @param isHorizontal true:横向布局  false：垂直布局
     * @param canScrollHorizontally 是否能左右滑动
     * @param canScrollVertically 是否能上下滑动
     */
    public void setLinearLayoutManager( boolean isHorizontal,final boolean canScrollHorizontally , final boolean canScrollVertically ) {
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(context){
            @Override
            public boolean canScrollHorizontally() {
                return canScrollHorizontally;
            }

            @Override
            public boolean canScrollVertically() {
                return canScrollVertically;
            }
        };

        if (isHorizontal){
            mLinearLayoutManager.setOrientation(LinearLayout.HORIZONTAL);
        }else {
            mLinearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        }
        recyclerView.setLayoutManager(mLinearLayoutManager);

    }

    public void setGridViewLayoutManager(int col, final boolean canScrollVertically) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, col) {
            @Override
            public boolean canScrollVertically() {
                return canScrollVertically;
            }
        };
        recyclerView.setLayoutManager(gridLayoutManager);
    }


    //添加分割线 默认方法 可以用过getRecyclerview。addItemDecoration 自定义
    public void addItemDecoration(){
        recyclerView.addItemDecoration(new MyDivider(context,recyclerView,true));
    }



    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public BaseRecycleViewAdapter<T> getAdapter() {
        return adapter;
    }

    public void setAdapter(BaseRecycleViewAdapter<T> adapter) {
        this.adapter = adapter;
    }

    //
    public abstract void onBindView(BaseViewHolder myViewHolder, T data, int i);
}
