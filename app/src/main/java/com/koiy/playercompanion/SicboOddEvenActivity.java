package com.koiy.playercompanion;

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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SicboOddEvenActivity extends AppCompatActivity {

    Runnable runnable;
    Handler handler;
    int attempt = 0;
    private MediaPlayer mediaPlayer;
    List<Integer> cardsCollection = new ArrayList<>();
    Integer[] oddNumbers = {1, 3, 5, 7, 9, 11, 13, 15, 17, 19};
    Integer[] evenNumbers = {2, 4, 6, 8, 10, 12, 14, 16, 18};
    List<Integer> attempts = new ArrayList<>();

    String ODD = "O";
    String EVEN = "E";
    String TRIPLE = "T";


    List<String> moneyPickerList = new ArrayList<>();

    String mp1 = "";
    String mp2 = "";
    String mp3 = "";
    String mp4 = "";


    List<String> cardList = new ArrayList<>();
    List<String> set1 = new ArrayList<>(Arrays.asList("O", "O", "O", "O"));
    List<String> set2 = new ArrayList<>(Arrays.asList("O", "O", "O", "E"));
    List<String> set3 = new ArrayList<>(Arrays.asList("O", "O", "E", "O"));
    List<String> set4 = new ArrayList<>(Arrays.asList("O", "E", "O", "O"));
    List<String> set5 = new ArrayList<>(Arrays.asList("O", "E", "E", "E"));
    List<String> set6 = new ArrayList<>(Arrays.asList("O", "E", "O", "E"));
    List<String> set7 = new ArrayList<>(Arrays.asList("O", "E", "E", "O"));
    List<String> set8 = new ArrayList<>(Arrays.asList("O", "O", "E", "E"));
    List<String> set9 = new ArrayList<>(Arrays.asList("E", "E", "E", "E"));
    List<String> set10 = new ArrayList<>(Arrays.asList("E", "E", "E", "O"));
    List<String> set11 = new ArrayList<>(Arrays.asList("E", "E", "O", "E"));
    List<String> set12 = new ArrayList<>(Arrays.asList("E", "O", "E", "E"));
    List<String> set13 = new ArrayList<>(Arrays.asList("E", "O", "O", "O"));
    List<String> set14 = new ArrayList<>(Arrays.asList("E", "O", "O", "E"));
    List<String> set15 = new ArrayList<>(Arrays.asList("E", "O", "E", "O"));
    List<String> set16 = new ArrayList<>(Arrays.asList("E", "E", "O", "O"));


    GridView resultGridview;
    GridView attemptsGridview;
    TextView lblSignal = null;
    TextView txtBoxAttemptCount = null;
    TextView attemptLabel = null;

    ImageView imgSicBoLogoCenter = null;

    LinearLayout layoutSignal;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sicbo_oddeven);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        imgSicBoLogoCenter = (ImageView) this.findViewById(R.id.imgSicBoLogoCenter);
        layoutSignal = (LinearLayout) this.findViewById(R.id.layoutSignal);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        lblSignal = (TextView) this.findViewById(R.id.lblSignal);


        txtBoxAttemptCount = (TextView) this.findViewById(R.id.txtBoxAttemptCount);
        attemptLabel = (TextView) this.findViewById(R.id.attemptLabel);

        attemptLabel.setVisibility(View.INVISIBLE);
        txtBoxAttemptCount.setVisibility(View.INVISIBLE);


        attempt = 0;

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // do your work

                new getSuperSicBoResult().execute();

                String r = variables.RESULT;

                if (r != null && !r.equals("")) {

                    Gson gson = new Gson();
                    SicBoResponse sicBoResponse = gson.fromJson(r, SicBoResponse.class);
                    long whenResult = sicBoResponse.getData()[0].getWhen();
                    long[] diceResult = sicBoResponse.getData()[0].getDice();

                    int totalDiceResult = 0;
                    for (long i : diceResult) {
                        totalDiceResult += i;
                    }

                    if (diceResult[0] == diceResult[1] && diceResult[1] == diceResult[2]) {
                        totalDiceResult = 0;
                    }

                    if (variables.whenLastResult != whenResult) {
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

    private void displayToView(Integer x) {


        cardsCollection.add(x);

        List<Integer> cardsResults = new ArrayList<>();
        cardsResults.addAll(cardsCollection);
        Collections.reverse(cardsResults);
        //RESULT GRIDVIEW
        resultGridview = (GridView) findViewById(R.id.grid);
        CustomAdapterResultsOddEven customAdapterResults = new CustomAdapterResultsOddEven(getApplicationContext(), cardsResults);
        resultGridview.setAdapter(customAdapterResults);


        List<Integer> evens = Arrays.asList(evenNumbers);
        List<Integer> odds = Arrays.asList(oddNumbers);


        List<Integer> attemptResults = new ArrayList<>();
        attemptResults.addAll(cardsCollection);
        Collections.reverse(attemptResults);

        int c = attemptResults.get(0);
        if (evens.contains(c)) {
            cardList.add(EVEN);

        } else if (odds.contains(c)) {
            cardList.add(ODD);
        } else {
            if (cardList.size() > 2) {
                cardList.add(TRIPLE);
            }
        }


            checkIt(x);

            List<Integer> attemptsReversed = new ArrayList<>();
            attemptsReversed.addAll(attempts);
            Collections.reverse(attemptsReversed);


            //ATTEMPTS GRIDVIEW
            attemptsGridview = (GridView) findViewById(R.id.gridviewAttempts);
            CustomAdapterAttempts customAdapterAttempts = new CustomAdapterAttempts(getApplicationContext(), attemptsReversed);
            attemptsGridview.setAdapter(customAdapterAttempts);



    }

    List<String> getMoneyPicker(int selector) {
        List<String> list = new ArrayList<>();

        if (selector == 3) {
            list.addAll(set1);
        } else if (selector == 4) {
            list.addAll(set2);
        } else if (selector == 5) {
            list.addAll(set3);
        } else if (selector == 6) {
            list.addAll(set4);
        } else if (selector == 7) {
            list.addAll(set5);
        } else if (selector == 8) {
            list.addAll(set6);
        } else if (selector == 9) {
            list.addAll(set7);
        } else if (selector == 10) {
            list.addAll(set8);
        } else if (selector == 11) {
            list.addAll(set9);
        } else if (selector == 12) {
            list.addAll(set10);
        } else if (selector == 13) {
            list.addAll(set11);
        } else if (selector == 14) {
            list.addAll(set12);
        } else if (selector == 15) {
            list.addAll(set13);
        } else if (selector == 16) {
            list.addAll(set14);
        } else if (selector == 17) {
            list.addAll(set15);
        } else if (selector == 18) {
            list.addAll(set16);
        }

        return list;
    }


    void setForcast(String forcast) {

        txtBoxAttemptCount.setText(attempt + "");
        if (forcast.equals("O")) {
            lblSignal.setText("BET ODD");
            lblSignal.setBackgroundResource(R.color.blue);
        } else {
            lblSignal.setText("BET EVEN");
            lblSignal.setBackgroundResource(R.color.red);
        }

    }


    private void checkIt(int selector) {

        int x = cardList.size();


        String card2 = "";
        String card3 = "";
        String card4 = "";
        String card5 = "";


        switch (x) {
            case 1:


                moneyPickerList = getMoneyPicker(selector);

                mp1 = moneyPickerList.get(0);
                mp2 = moneyPickerList.get(1);
                mp3 = moneyPickerList.get(2);
                mp4 = moneyPickerList.get(3);


                attempt++;
                setForcast(mp1);
                attemptLabel.setText(mp1 + " - " + mp2 + " - " + mp3 + " - " + mp4);

                break;
            case 2:


                card2 = cardList.get(1);

                if (!card2.equals(mp1)) {
                    attempt++;
                    setForcast(mp2);
                } else {

                    addResultIcon(1);

                }
                System.out.println(x + " CASE 2");
                break;
            case 3:

                card3 = cardList.get(2);

                if (!card3.equals(mp2)) {
                    attempt++;
                    setForcast(mp3);
                } else {

                    addResultIcon(1);
                }
                break;
            case 4:

                card4 = cardList.get(3);

                if (!card4.equals(mp3)) {
                    attempt++;
                    setForcast(mp4);
                } else {

                    addResultIcon(1);
                }

                System.out.println(x + " CASE 3");
                break;
            case 5:

                card5 = cardList.get(4);

                if (!card5.equals(mp4)) {


                    addResultIcon(0);
                    cardList.clear();

                } else {
                    addResultIcon(1);
                    cardList.clear();
                }

                System.out.println(x + " CASE 4");
                break;
            default:

        }

        viewAttempt();

    }

    private void viewAttempt() {


        txtBoxAttemptCount.setText(attempt + "");


        layoutSignal.setVisibility(View.VISIBLE);
        imgSicBoLogoCenter.setVisibility(View.INVISIBLE);

        if (attempt == 4) {

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
        } else {

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
        if (i == 1) {
            attempts.add(attempt);
            cardList.clear();
        } else {

            cardList.clear();
            attempts.add(0); //this is for DS
            lblSignal.setText("");
            lblSignal.setBackgroundResource(R.color.dark_blue);
        }

        attempt = 0;
        imgSicBoLogoCenter.setVisibility(View.VISIBLE);
    }


    public void evenClicked(View view) {


        displayToView(2);
        variables.RESULT = "";
    }

    public void oddClicked(View view) {
        displayToView(3);
        variables.RESULT = "";
    }
}