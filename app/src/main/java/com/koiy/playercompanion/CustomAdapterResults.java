package com.koiy.playercompanion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class CustomAdapterResults extends BaseAdapter {

    Integer[] smallNumbers =  { 4,  5,6,7, 8, 9,  10};
    Integer[] bigNumbers =  {  11,12,13, 14,15,16,  17   };


//    private int maxRows = 6; // Maximum number of rows

    Context context;
    List<Integer> numbers;
    LayoutInflater inflter;
    public CustomAdapterResults(Context applicationContext, List<Integer> num) {
        this.context = applicationContext;
        this.numbers = num;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return numbers.size();
//        int maxItems = maxRows * 1;
//        return Math.min(maxItems, numbers.size());
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.result_layout, null); // inflate the layout
        TextView txtBox = (TextView) view.findViewById(R.id.txtBox);
        TextView txtBoxText = (TextView) view.findViewById(R.id.txtBoxText);
        LinearLayout layoutBox  = (LinearLayout)view.findViewById(R.id.layoutBox);

        List<Integer> bigs = Arrays.asList(bigNumbers);
        List<Integer> smalls = Arrays.asList(smallNumbers);


        if(numbers.size() != 0){


            int r = numbers.get(i);

            if(bigs.contains(r)){
                txtBox.setText(r+"");
                txtBoxText.setText("Big");
                layoutBox.setBackgroundResource(R.color.red);

            }else if(smalls.contains(r)){
                txtBox.setText(r+"");
                txtBoxText.setText("Small");
                layoutBox.setBackgroundResource(R.color.blue);
            }else{
                txtBox.setText(r+"");
                txtBoxText.setText("Triple");
                layoutBox.setBackgroundResource(R.color.darkRed);
            }



        }




        return view;

    }
}
