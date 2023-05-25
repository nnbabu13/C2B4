package com.koiy.playercompanion;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SicboActivity extends AppCompatActivity {

    Runnable runnable;
    Handler handler;
    int attempt = 0;
    private MediaPlayer mediaPlayer;
    List<Integer> cardsCollection =  new ArrayList<>();

    Integer[] smallNumbers =  { 4,  5,6,7, 8, 9,  10};
    Integer[] bigNumbers =  {  11,12,13, 14,15,16,  17   };


    List<Integer> attempts = new  ArrayList<>();

    String SMALL = "S";
    String BIG = "B";
    String TRIPLE = "T";
    boolean isStarted;
    List<String> cardList = new ArrayList<>();
    List<String> setA = new ArrayList<>(Arrays.asList("S", "S", "S", "B", "B", "B")) ;
    List<String> setB = new ArrayList<>(Arrays.asList( "S", "B", "S", "S", "B", "S" )) ;
    List<String> setC = new ArrayList<>(Arrays.asList( "B", "B", "B", "S", "S", "S" )) ;
    List<String> setD = new ArrayList<>(Arrays.asList( "B", "S", "B", "B", "S", "B" )) ;


    GridView resultGridview;
    GridView attemptsGridview;
    TextView lblSignal  = null;
    TextView txtBoxAttemptCount = null;
    TextView attemptLabel = null;

    ImageView imgSicBoLogoCenter =  null;

     LinearLayout layoutSignal;
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sicbo);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        imgSicBoLogoCenter =  (ImageView)this.findViewById(R.id.imgSicBoLogoCenter);
        layoutSignal = (LinearLayout)this.findViewById(R.id.layoutSignal);

//        imgSicBoLogoCenter.setVisibility(View.INVISIBLE);
        // Keep the screen on while the activity is visible
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        lblSignal = (TextView) this.findViewById(R.id.lblSignal);


        txtBoxAttemptCount= (TextView) this.findViewById(R.id.txtBoxAttemptCount);
        attemptLabel= (TextView) this.findViewById(R.id.attemptLabel);

        attemptLabel.setVisibility(View.INVISIBLE);
        txtBoxAttemptCount.setVisibility(View.INVISIBLE);



        attempt = 0;

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // do your work

                new getSuperSicBoResult().execute();

                String r   =  variables.RESULT;

                if(r != null && !r.equals("")){

                    Gson gson = new Gson();
                    SicBoResponse sicBoResponse = gson.fromJson(r, SicBoResponse.class);
                    long whenResult = sicBoResponse.getData()[0].getWhen();
                    long[] diceResult = sicBoResponse.getData()[0].getDice();

                    int totalDiceResult = 0;
                    for (long i:diceResult) {
                        totalDiceResult += i;
                    }

                    if (diceResult[0] == diceResult[1] && diceResult[1] == diceResult[2])
                    {
                        totalDiceResult = 0;
                    }

                    if (variables.whenLastResult != whenResult)
                    {
                        variables.whenLastResult = whenResult;

                        displayToView(totalDiceResult);
                        variables.RESULT = "";
                    }

                }


                handler.postDelayed(this, 2000);



            }
        };
        handler.postDelayed(runnable, 2000);




    }

    private  void displayToView(Integer x){


        cardsCollection.add(x);

        List<Integer> cardsResults = new ArrayList<>();
        cardsResults.addAll(cardsCollection);
        Collections.reverse(cardsResults);
        //RESULT GRIDVIEW
        resultGridview = (GridView) findViewById(R.id.grid);
        CustomAdapterResults customAdapterResults = new CustomAdapterResults(getApplicationContext(), cardsResults);
        resultGridview.setAdapter(customAdapterResults);




        List<Integer> bigs = Arrays.asList(bigNumbers);
        List<Integer> smalls = Arrays.asList(smallNumbers);


        List<Integer> attemptResults = new ArrayList<>();
        attemptResults.addAll(cardsCollection);
        Collections.reverse(attemptResults);

        int c = attemptResults.get(0);
        if (bigs.contains(c))
        {
            cardList.add(BIG);

        }
        else if (smalls.contains(c))
        {
            cardList.add(SMALL);
        }
        else
        {
            if(cardList.size() > 2)
            {
                cardList.add(TRIPLE);
            }
        }


        checkIt();


        List<Integer> attemptsReversed = new ArrayList<>();
        attemptsReversed.addAll(attempts);
        Collections.reverse(attemptsReversed);



        //ATTEMPTS GRIDVIEW
        attemptsGridview = (GridView) findViewById(R.id.gridviewAttempts);
        CustomAdapterAttempts customAdapterAttempts = new CustomAdapterAttempts(getApplicationContext(), attemptsReversed);
        attemptsGridview.setAdapter(customAdapterAttempts);



    }



    private void checkIt() {

        int x = cardList.size();




        String card1 = "";
        String card2 = "";
        String card3 = "";
        String card4 = "";
        String card5 = "";
        String card6 = "";

        String smallSignal = "SMALL";
        String bigSignal = "BIG";

        switch (x) {

            case 2:
                card1 = cardList.get(0);
                card2 = cardList.get(1);
                if (card1.equals(SMALL) && card2.equals(SMALL))
                {
                    attempt++;

                    lblSignal.setText(smallSignal);
                    lblSignal.setBackgroundResource(R.color.blue);
                }
                else if (card1.equals(SMALL) && card2.equals(BIG))
                {
                    attempt++;
                    lblSignal.setText(smallSignal);
                    lblSignal.setBackgroundResource(R.color.blue);
                }
                else if (card1.equals(BIG) && card2.equals(BIG))
                {
                    attempt++;
                    lblSignal.setText(bigSignal);
                    lblSignal.setBackgroundResource(R.color.red);
                }
                else if (card1.equals(BIG) && card2.equals(SMALL))
                {

                    attempt++;
                    lblSignal.setText(bigSignal);
                    lblSignal.setBackgroundResource(R.color.red);
                }

                break;

            case 3:
                card1 = cardList.get(0);
                card2 = cardList.get(1);
                card3 = cardList.get(2);

                if (card1.equals(SMALL) && card2.equals(SMALL))
                {
                    if (!card3.equals(setA.get(2)))
                    {
                        attempt++;
                        lblSignal.setText(bigSignal);
                        lblSignal.setBackgroundResource(R.color.red);
                    }
                    else
                    {

                        lblSignal.setText("");
                        lblSignal.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }



                }
                else if (card1.equals(SMALL) && card2.equals(BIG))
                {
                    if (!card3.equals(setB.get(2)))
                    {
                        attempt++;
                        lblSignal.setText(smallSignal);
                        lblSignal.setBackgroundResource(R.color.blue);
                    }
                    else
                    {

                        lblSignal.setText("");
                        lblSignal.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                }
                else if (card1.equals(BIG) && card2.equals(BIG))
                {
                    if (!card3.equals(setC.get(2)))
                    {
                        attempt++;
                        lblSignal.setText(smallSignal);
                        lblSignal.setBackgroundResource(R.color.blue);
                    }
                    else
                    {

                        lblSignal.setText("");
                        lblSignal.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                }
                else if (card1.equals(BIG) && card2.equals(SMALL))
                {

                    if (!card3.equals(setD.get(2)))
                    {
                        attempt++;
                        lblSignal.setText(bigSignal);
                        lblSignal.setBackgroundResource(R.color.red);
                    }
                    else
                    {

                        lblSignal.setText("");
                        lblSignal.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                }

                break;

            case 4:
                card1 = cardList.get(0);
                card2 = cardList.get(1);
                card4 = cardList.get(3);

                if (card1.equals(SMALL) && card2.equals(SMALL))
                {
                    if (!card4.equals(setA.get(3)))
                    {
                        attempt++;
                        lblSignal.setText(bigSignal);
                        lblSignal.setBackgroundResource(R.color.red);
                    }
                    else
                    {

                        lblSignal.setText("");
                        lblSignal.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                }
                else if (card1.equals(SMALL) && card2.equals(BIG))
                {
                    if (!card4.equals(setB.get(3)))
                    {
                        attempt++;
                        lblSignal.setText(bigSignal);
                        lblSignal.setBackgroundResource(R.color.red);
                    }
                    else
                    {

                        lblSignal.setText("");
                        lblSignal.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                }
                else if (card1.equals(BIG) && card2.equals(BIG))
                {
                    if (!card4.equals(setC.get(3)))
                    {
                        attempt++;
                        lblSignal.setText(smallSignal);
                        lblSignal.setBackgroundResource(R.color.blue);
                    }
                    else
                    {

                        lblSignal.setText("");
                        lblSignal.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                }
                else if (card1.equals(BIG) && card2.equals(SMALL))
                {

                    if (!card4.equals(setD.get(3)))
                    {
                        attempt++;
                        lblSignal.setText(smallSignal);
                        lblSignal.setBackgroundResource(R.color.blue);
                    }
                    else
                    {

                        lblSignal.setText("");
                        lblSignal.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                }

                break;


            case 5:
                card1 = cardList.get(0);
                card2 = cardList.get(1);
                card5 = cardList.get(4);

                if (card1.equals(SMALL) && card2.equals(SMALL))
                {
                    if (!card5.equals(setA.get(4)))
                    {
                        attempt++;
                        lblSignal.setText(bigSignal);
                        lblSignal.setBackgroundResource(R.color.red);
                    }
                    else
                    {

                        lblSignal.setText("");
                        lblSignal.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                }
                else if (card1.equals(SMALL) && card2.equals(BIG))
                {
                    if (!card5.equals(setB.get(4)))
                    {
                        attempt++;
                        lblSignal.setText(smallSignal);
                        lblSignal.setBackgroundResource(R.color.blue);
                    }
                    else
                    {

                        lblSignal.setText("");
                        lblSignal.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                }
                else if (card1.equals(BIG) && card2.equals(BIG))
                {
                    if (!card5.equals(setC.get(4)))
                    {
                        attempt++;
                        lblSignal.setText(smallSignal);
                        lblSignal.setBackgroundResource(R.color.blue);
                    }
                    else
                    {

                        lblSignal.setText("");
                        lblSignal.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                }
                else if (card1.equals(BIG) && card2.equals(SMALL))
                {

                    if (!card5.equals(setD.get(4)))
                    {
                        attempt++;
                        lblSignal.setText(bigSignal);
                        lblSignal.setBackgroundResource(R.color.red);
                    }
                    else
                    {

                        lblSignal.setText("");
                        lblSignal.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                }

                break;


            case 6:
                card1 = cardList.get(0);
                card2 = cardList.get(1);
                card6 = cardList.get(5);

                if (card1.equals(SMALL) && card2.equals(SMALL))
                {
                    if (card6.equals(setA.get(5)))
                    {

                        lblSignal.setText("");
                        lblSignal.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);
                    }
                    else
                    {
                        addResultIcon(0);
                    }
                }
                else if (card1.equals(SMALL) && card2.equals(BIG))
                {
                    if (card6.equals(setB.get(5)))
                    {

                        lblSignal.setText("");
                        lblSignal.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);
                    }
                    else
                    {
                        addResultIcon(0);
                    }
                }
                else if (card1.equals(BIG) && card2.equals(BIG))
                {
                    if (card6.equals(setC.get(5)))
                    {
                        lblSignal.setText("");
                        lblSignal.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);
                    }
                    else
                    {
                        addResultIcon(0);
                    }
                }
                else if (card1.equals(BIG) && card2.equals(SMALL))
                {

                    if (card6.equals(setD.get(5)))
                    {
                        lblSignal.setText("");
                        lblSignal.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);
                    }
                    else
                    {
                        addResultIcon(0);
                    }
                }

                break;






        }


        viewAttempt();
    }

    private void viewAttempt() {


        txtBoxAttemptCount.setText(attempt+"");


        layoutSignal.setVisibility(View.VISIBLE);
        imgSicBoLogoCenter.setVisibility(View.INVISIBLE);

        if (attempt == 4)        {

            playNotification();

            attemptLabel.setVisibility(View.VISIBLE);
            txtBoxAttemptCount.setVisibility(View.VISIBLE);
            txtBoxAttemptCount.setTextColor(Color.RED);

            // Create the blinking animation
            Animation blinkAnimation = new AlphaAnimation(0.0f, 1.0f);
            blinkAnimation.setDuration(200); // Set the duration of each blink
            blinkAnimation.setRepeatMode(Animation.REVERSE);
            blinkAnimation.setRepeatCount(Animation.INFINITE);
            // Apply the animation to the TextView
            txtBoxAttemptCount.startAnimation(blinkAnimation);


        } else if (attempt == 3) {


            attemptLabel.setVisibility(View.VISIBLE);
            txtBoxAttemptCount.setVisibility(View.VISIBLE);
            txtBoxAttemptCount.setTextColor(Color.YELLOW);

            // Create the blinking animation
            Animation blinkAnimation = new AlphaAnimation(0.0f, 1.0f);
            blinkAnimation.setDuration(200); // Set the duration of each blink
            blinkAnimation.setRepeatMode(Animation.REVERSE);
            blinkAnimation.setRepeatCount(Animation.INFINITE);
            // Apply the animation to the TextView
            txtBoxAttemptCount.startAnimation(blinkAnimation);



        } else if (attempt == 2) {

            attemptLabel.setVisibility(View.VISIBLE);
            txtBoxAttemptCount.setVisibility(View.VISIBLE);
            txtBoxAttemptCount.setTextColor(Color.WHITE);
        } else if (attempt == 1) {

            txtBoxAttemptCount.setTextColor(Color.WHITE);
            attemptLabel.setVisibility(View.VISIBLE);
            txtBoxAttemptCount.setVisibility(View.VISIBLE);
        }else{

            txtBoxAttemptCount.clearAnimation();
            attemptLabel.setVisibility(View.INVISIBLE);
            txtBoxAttemptCount.setVisibility(View.INVISIBLE);

            layoutSignal.setVisibility(View.INVISIBLE);
            imgSicBoLogoCenter.setVisibility(View.VISIBLE);



        }


    }

    private void playNotification() {

        mediaPlayer = MediaPlayer.create(this, R.raw.notification);
        mediaPlayer.start();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void addResultIcon(int i) {
        if(i == 1){
            attempts.add(attempt);
        }else {

            cardList.clear();
            attempts.add(0); //this is for DS
            lblSignal.setText("");
            lblSignal.setBackgroundResource(R.color.dark_blue);
        }

        attempt = 0;
        imgSicBoLogoCenter.setVisibility(View.VISIBLE);
    }



    public void bigClicked(View view) {

        displayToView(11);
        variables.RESULT = "";
    }

    public void smallClicked(View view) {
        displayToView(5);
        variables.RESULT = "";
    }

    public void zeroClicked(View view) {
        displayToView(0);
        variables.RESULT = "";
    }
}