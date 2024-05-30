package com.koiy.playercompanion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomAdapterAttempts extends BaseAdapter {

    Integer[] attempts =  { 1,2,3,4,5,6,7,8};




    Context context;
    List<Integer> numbers;
    LayoutInflater inflter;


    public CustomAdapterAttempts(Context applicationContext, List<Integer> num) {
        this.context = applicationContext;
        this.numbers = num;
        inflter = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return numbers.size();
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
        view = inflter.inflate(R.layout.attempts_layout, null); // inflate the layout
        TextView txtBoxAttempt = (TextView) view.findViewById(R.id.txtBoxAttempt);
        TextView txtBoxAttemptLabel = (TextView) view.findViewById(R.id.txtBoxAttemptLabel);


        LinearLayout layoutBoxAttempts  = (LinearLayout)view.findViewById(R.id.layoutBoxAttempts);

        List<Integer> attemptsList = Arrays.asList(attempts);





        if(numbers.size() != 0){
            int r = numbers.get(i);


            if(attemptsList.contains(r)){
                txtBoxAttempt.setText(r+"");
                layoutBoxAttempts.setBackgroundResource(R.color.green);

            }else{
                txtBoxAttempt.setText("DS");
                txtBoxAttemptLabel.setVisibility(View.INVISIBLE);
                layoutBoxAttempts.setBackgroundResource(R.color.darkRed);
            }




        }




        return view;

    }
}
