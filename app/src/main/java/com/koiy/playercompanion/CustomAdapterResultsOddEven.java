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

public class CustomAdapterResultsOddEven extends BaseAdapter {

    Integer[] oddNumbers = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19};
    Integer[] evenNumbers = {2, 4, 6, 8, 10, 12, 14, 16, 18};


    Context context;
    List<DiceOutCome> numbers;
    LayoutInflater inflter;

    public CustomAdapterResultsOddEven(Context applicationContext, List<DiceOutCome> num) {
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
        LinearLayout layoutBox = (LinearLayout) view.findViewById(R.id.layoutBox);

        List<Integer> bigs = Arrays.asList(evenNumbers);
        List<Integer> smalls = Arrays.asList(oddNumbers);


        if (numbers.size() != 0) {


            DiceOutCome dice = numbers.get(i);
            txtBox.setText(dice.getTotal() + "");
            if (bigs.contains(dice.getTotal()) && !dice.isTriple()) {

                txtBoxText.setText("Even");
                layoutBox.setBackgroundResource(R.color.red);

            } else if (smalls.contains(dice.getTotal()) && !dice.isTriple()) {
                txtBoxText.setText("Odd");
                layoutBox.setBackgroundResource(R.color.blue);
            } else {

                txtBoxText.setText("Triple");
                layoutBox.setBackgroundResource(R.color.darkRed);
            }


        }


        return view;

    }
}
