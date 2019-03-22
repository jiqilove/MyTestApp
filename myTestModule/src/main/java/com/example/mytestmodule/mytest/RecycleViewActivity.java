package com.example.mytestmodule.mytest;

import android.content.Intent;
import android.os.Handler;
import android.speech.RecognitionService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.mytestmodule.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecycleViewActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    MyRecycleViewAdapter myAdapter;
    private static final int TOTAL_COUNTER = 16;
    private int mCurrentCounter = 0;
    List<String> strings = new ArrayList<>();


    protected int mState = MyAdapter.All_Gone;

//    protected void setState(int mState) {
//        this.mState = mState;
//
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                changeAdaperState();
//            }
//        });
//    }

//    //改变底部bottom的样式
//    protected void changeAdaperState() {
//        if (myAdapter != null && myAdapter.footerHolder != null) {
//            Log.e("cc", "setState: "+mState );
//            myAdapter.footerHolder.setData(mState);
//            myAdapter.notifyDataSetChanged();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_view);
        ButterKnife.bind(this);

        for (int i = 0; i < 4; i++) {
            strings.add("你好" + i);
        }
        myAdapter = new MyRecycleViewAdapter(RecycleViewActivity.this,strings);
//        myAdapter.addData(0);
//        myAdapter.addData(strings.size());
        recyclerView.setAdapter(myAdapter);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(RecycleViewActivity.this);

        mLinearLayoutManager.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManager);
//        recyclerView.setHasFixedSize(true);

//        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
//            @Override
//            public void onLoadNextPage(View view) {
//                super.onLoadNextPage(view);
//
//                Log.e("cnb", "onLoadNextPage: ");
//
//
//                if (strings.size() < TOTAL_COUNTER) {
//                    Log.e("sss", "onLoadNextPage:更多加载");
//
////                    strings.add("新的");
//
//
//                    setState(MyAdapter.Loading);
////                    myAdapter.notifyDataSetChanged();
//
//
//                    recyclerView.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.e("cnb", "run: ");
//                            List<String> a = new ArrayList<>();
//                            int newCount = 2;
//                            if (strings.size() + newCount > TOTAL_COUNTER) {
//                                newCount = TOTAL_COUNTER - strings.size();
//                            }
//                            for (int i = 0; i < newCount; i++) {
//                                a.add("新的" + i);
//                            }
//                            strings.addAll(strings.size(), a);
//                            setState(MyAdapter.All_Gone);
//                            myAdapter.notifyDataSetChanged();
//                        }
//                    }, 1000);
//
//
////        MyAdapter.status=MyAdapter.Loading;
//
//
//                } else {
//
////                    Log.e("sss", "onLoadNextPage: 到底部");
////                    recyclerView.post(new Runnable() {
////                        @Override
////                        public void run() {
////                            Log.e("cnb", "run----: ");
//                          setState(MyAdapter.Loading_end);
////                            myAdapter.notifyDataSetChanged();
////                        }
////                    });
//
//
//                    //错误
//                }
//
//
//            }
//        });


    }
}
