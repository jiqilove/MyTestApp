package com.example.mytestmodule.mytest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mytestmodule.R;

import java.util.List;

public class MyRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //item类型
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_BOTTOM = 2;

    private List<String> mDatas = null;
    private LayoutInflater mInflater = null;

    public MyRecycleViewAdapter(Context context, List<String> mDatas) {
        this.mDatas = mDatas;
        this.mInflater = LayoutInflater.from(context);
    }

    //list 内容长度
    public int getContentItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public boolean isHeaderView(int position) {
        return 0 == position;
    }


    public boolean isFooterView(int position) {
        return position == (getContentItemCount() + 1);
    }


    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return ITEM_TYPE_HEADER;
        } else if (position == (getContentItemCount() + 1)) {
            return ITEM_TYPE_BOTTOM;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View view = null;
        if (viewType == ITEM_TYPE_HEADER) {
            view = mInflater.inflate(R.layout.my_recyclerview_header, viewGroup, false);
            viewHolder = new HeaderViewHolder(view);

        } else if (viewType == ITEM_TYPE_BOTTOM) {
            view = mInflater.inflate(R.layout.test_recycle_footer_item, viewGroup, false);
            viewHolder = new FooterViewHolder(view);

        } else {
            view = mInflater.inflate(R.layout.test_recycle_item, viewGroup, false);
            viewHolder = new MyViewHolder(view);
        }


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


//
//          if (viewHolder instanceof HeaderViewHolder) {
//
//        } else if (viewHolder instanceof MyAdapter.FooterHolder) {
//
//
//        }
        if (viewHolder instanceof MyViewHolder){

            ((MyViewHolder) viewHolder).textView.setText("ddd");
        }
    }

    @Override
    public int getItemCount() {
        return getContentItemCount() + 2;
    }


    public void addData(int position) {
        mDatas.add(position, "Insert" + position);
        notifyItemInserted(position);
        if (position != getItemCount()) {
            notifyItemRangeChanged(position, getItemCount());
        }
    }

    public void removeDate(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
        if (position != getItemCount()) {
            notifyItemRangeChanged(position, getItemCount());
        }

    }


    //头
    class HeaderViewHolder extends RecyclerView.ViewHolder {

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    //尾
    class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    //内容
    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.txt_item);
        }
    }

}
