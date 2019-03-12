package com.example.mytestmodule.pull_to_refresh;

import android.content.Context;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytestmodule.R;
import com.example.mytestmodule.mybase.recyclerView.BaseRecyclerView;
import com.example.mytestmodule.mybase.recyclerView.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;


//使用一个拖拽的方式来解决数据刷新问题的方法
public class PullActivity extends AppCompatActivity {
    private List<UserBean> datas;
    private RecyclerView recyclerView;
    private Context mContext = PullActivity.this;
    private UserAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        BaseRecyclerView baseRecyclerView= new BaseRecyclerView<UserBean>(PullActivity.this, recyclerView, R.layout.item_user) {
            @Override
            public List<UserBean> setDatas(List<UserBean> datas) {
                datas = new ArrayList<>();
                for (int i = 0; i < 15; i++) {
                    datas.add(new UserBean("cnb", "" + i));
                }
                return datas;
            }

            @Override
            public void onBindView(BaseViewHolder myViewHolder, UserBean data, final int i) {
                TextView tv_name = (TextView) myViewHolder.getView(R.id.tv_name);
                TextView tv_age = (TextView) myViewHolder.getView(R.id.tv_age);
                LinearLayout linearLayout =myViewHolder.getView(R.id.ll_all);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("cnb","==="+i);
                    }
                });

                tv_age.setText(data.getAge());
                tv_name.setText(data.getName());
            }
        };


//        baseRecyclerView.setLinearLayoutManager(false,false,true);
        baseRecyclerView.setGridViewLayoutManager(3,true);
        baseRecyclerView.addItemDecoration();



        initView();
    }


    private void initData() {

    }
    private void initView() {
//        initData();
//        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
//        mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(mLinearLayoutManager);

//        mAdapter = new UserAdapter(mContext, datas, R.layout.item_user);

//        mUserSimpleRecycleAdapter = new UserSimpleRecycleAdapter(mContext, datas);
//        recyclerView.setAdapter(mAdapter);

    }
}

