package com.example.mytestmodule.pull_to_refresh;

import android.content.Context;
import android.widget.TextView;


import com.example.mytestmodule.R;
import com.example.mytestmodule.mybase.recyclerView.BaseRecycleViewAdapter;
import com.example.mytestmodule.mybase.recyclerView.BaseViewHolder;

import java.util.List;

public class UserAdapter extends BaseRecycleViewAdapter<UserBean> {


    public UserAdapter(Context context, List<UserBean> date, int layoutId) {
        super(context, date, layoutId);
    }

    @Override
    protected void bindData(BaseViewHolder holder, UserBean date, int position) {
////        LinearLayout ll_all = (LinearLayout) holder.getView(R.id.ll_all);
        TextView tv_name = (TextView) holder.getView(R.id.tv_name);
        TextView tv_age = (TextView) holder.getView(R.id.tv_age);

        tv_age.setText(date.getAge());
        tv_name.setText(date.getName());
//    }
    }




}
