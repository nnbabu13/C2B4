package com.koiy.playercompanion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterBaccaratBigRoad extends RecyclerView.Adapter<AdapterBaccaratBigRoad.ViewHolder> {
    private List<String> dataList;

    public AdapterBaccaratBigRoad(List<String> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_baccarat_hongkong_big_road, parent, false);
        return new ViewHolder(view);
    }

    public void updateData(List<String> newData) {
        dataList.clear();
        dataList.addAll(newData);
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String data = dataList.get(position);
        int pos = position + 1;
//        holder.textView.setText(data+" "+pos);
//        holder.txtCardNumber.setText(pos+"");

        if (data.equals("P")) {
            holder.textView.setBackgroundResource(R.drawable.rounded_corner_player_big_road);
        } else if (data.equals("B")) {
            holder.textView.setBackgroundResource(R.drawable.rounded_corner_banker_big_road);
        } else {

//            holder.textView.setBackgroundResource(R.drawable.green_circle_background);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView,txtCardNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
//            txtCardNumber =  itemView.findViewById(R.id.txtCardNumber);
        }
    }
}
