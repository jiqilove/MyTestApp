package com.example.mytestapp.bluetooth_BLE;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mytestapp.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private List<BluetoothDevice> deviceList;

    private IonClickItemView IonClickItemView;

    public ListAdapter(List<BluetoothDevice> deviceList) {
        this.deviceList = deviceList;

    }

    public void OnItemClick(IonClickItemView onClickItemView) {
        this.IonClickItemView = onClickItemView;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_bluetooth_ble_item, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        String address_str = deviceList.get(i).getAddress();
        String name_str = deviceList.get(i).getName();
        myViewHolder.txt_address.setText("设备地址："+address_str);
        myViewHolder.txt_name.setText("设备名称："+name_str);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IonClickItemView!=null){
                    IonClickItemView.onClickItem(v, i);

                }

            }
        });
        myViewHolder.itemView.setTag(i);


    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name;
        TextView txt_address;
        LinearLayout ll_ble;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_address = itemView.findViewById(R.id.txt_address);
            ll_ble = itemView.findViewById(R.id.ll_ble);
        }
    }


    interface IonClickItemView {
        void onClickItem(View view, int position);
    }
}
