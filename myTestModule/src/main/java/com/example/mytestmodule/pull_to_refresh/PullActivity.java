package com.example.mytestmodule.pull_to_refresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.mytestmodule.R;
import com.example.mytestmodule.mybase.recyclerView.RecyclerViewHelper;
import com.example.mytestmodule.mybase.recyclerView.BaseViewHolder;
import com.example.mytestmodule.pull_to_refresh.bean.UserBean;

import java.util.ArrayList;
import java.util.List;


//使用一个拖拽的方式来解决数据刷新问题的方法
public class PullActivity extends AppCompatActivity {
    private List<UserBean> datas;
    private RecyclerView recyclerView;
    private Context mContext = PullActivity.this;
    private SwipeRefreshLView refreshLayout;
    RecyclerViewHelper recyclerViewHelper;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        refreshLayout = findViewById(R.id.swipeRefreshLayout);



         recyclerViewHelper = new RecyclerViewHelper<UserBean>(PullActivity.this, recyclerView, R.layout.item_user) {

             @Override
             public List<UserBean> setDatas() {
                 datas = new ArrayList<>();
                 for (int i = 0; i < 15; i++) {
                     datas.add(new UserBean("cnb", "" + i));
                 }
                 return datas;
             }

             @Override
            public void onBindView(BaseViewHolder myViewHolder, UserBean data, final int i) {

                myViewHolder.setText(R.id.tv_age, data.getAge());
                myViewHolder.setText(R.id.tv_name, data.getName() + "66");
                myViewHolder.setOnClickListener(R.id.ll_all, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("cnb", "===" + i);
                    }
                });

            }
        };


        recyclerViewHelper.setLinearLayoutManager(false, false, true);
//        recyclerViewHelper.setGridViewLayoutManager(3, true);
        recyclerViewHelper.addItemDecoration();



        refreshLayout.setColorSchemeColors(R.color.yellow_FFEB3B, R.color.colorAccent, R.color.colorPrimaryDark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<UserBean>  dd = new ArrayList<>();
                        for (int i = 0; i < 5; i++) {
                            dd.add(new UserBean("xmz", "" + i));
                        }
                        recyclerViewHelper.setLoadMoreData(dd);
                        Log.e("cnb", "run: 刷新");
                        refreshLayout.setRefreshing(false);
                    }
                }, 1200);
            }
        });

        refreshLayout.setOnLoadListener(new SwipeRefreshLView.OnLoadListener() {
            @Override
            public void onLoad() {
//                refreshLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
                        Log.e("cnb", "onLoad:6666ddddd " );
//                        refreshLayout.setLoading(false);
//                    }
//                },1000);

            }
        });

    }


}

