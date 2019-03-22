package com.example.mytestmodule.mytest;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytestmodule.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "MyAdapter";
    public static final int All_Gone = 1;
    public static final int Loading = 2;
    public static final int Loading_end = 3;
    public static final int NetWorkError = 4;
    public static int status = All_Gone;


    private List<String> list;
    public FooterHolder footerHolder;
    public NormalHolder normalHolder;


    public MyAdapter(List<String> list) {
        this.list = list;


    }




    private final static int ITEM_HEADER = 0;
    private final static int ITEM_CONTENT = 1;
    private final static int ITEM_FOOT = 2;

    @Override
    public int getItemViewType(int position) {
        if (position == list.size()) {
            return ITEM_FOOT;
        } else {
            return ITEM_CONTENT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        normalHolder = new NormalHolder(inflater.inflate(R.layout.test_recycle_item, viewGroup, false));
        footerHolder = new FooterHolder(inflater.inflate(R.layout.test_recycle_footer_item, viewGroup, false));

        if (viewType == ITEM_FOOT) {
            return footerHolder;
        } else {
            return normalHolder;
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof NormalHolder) {
            ((NormalHolder) viewHolder).textView.setText(list.get(i));
        }


    }

    //public abstract void footViewHolderBind(RecyclerView.MyViewHolder viewHolder, int i);
    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    class NormalHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public NormalHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
        }
    }


    public class FooterHolder extends RecyclerView.ViewHolder {

        View tv_loading;
        View tv_loading_end;
        View tv_network_error;

        public FooterHolder(@NonNull View itemView) {
            super(itemView);
            tv_loading = itemView.findViewById(R.id.loading);
            tv_loading_end = itemView.findViewById(R.id.loading_end);
            tv_network_error = itemView.findViewById(R.id.network_error);
        }

        public void setData(int status) {
            Log.d("TAG", "reduAdapter" + status + "");
            switch (status) {
                case All_Gone:
//                    setAllGone();
//                    tv_loading.setVisibility(View.GONE);
//                    tv_loading_end.setVisibility(View.GONE);
//                    tv_network_error.setVisibility(View.GONE);
                    break;
                case Loading:
//                    setAllGone();
                    tv_loading.setVisibility(View.VISIBLE);
                    tv_loading_end.setVisibility(View.GONE);
                    tv_network_error.setVisibility(View.GONE);
                    Log.e(TAG, "setData: loading"+tv_loading.getVisibility() );
                    break;
                case Loading_end:
//                    setAllGone();
                    tv_loading.setVisibility(View.GONE);
                    tv_loading_end.setVisibility(View.VISIBLE);
                    tv_network_error.setVisibility(View.GONE);
                    break;
                case NetWorkError:
//                    setAllGone();
                    tv_loading.setVisibility(View.GONE);
                    tv_loading_end.setVisibility(View.GONE);
                    tv_network_error.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }

        }

        void setAllGone() {
            if (tv_loading != null) {
                tv_loading.setVisibility(View.GONE);
            }
            if (tv_loading_end != null) {
                tv_loading_end.setVisibility(View.GONE);
            }
            if (tv_network_error != null) {
                tv_network_error.setVisibility(View.GONE);
            }
        }
    }
}

