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

    boolean isDeathSquare;


    Integer setIndex = 0;
    int attempt = 0;
    private MediaPlayer mediaPlayer;
    List<DiceOutCome> cardsCollection = new ArrayList<>();

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
    String mp5 = "";
    String mp6 = "";
    String mp7 = "";

    String mp8 = "";


    List<String> cardList = new ArrayList<>();


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

        isDeathSquare = false;

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

                    DiceOutCome diceOutCome = new DiceOutCome();
                    int totalDiceResult = 0;
                    for (long i : diceResult) {
                        totalDiceResult += i;
                    }
                    diceOutCome.setTotal(totalDiceResult);
                    if (diceResult[0] == diceResult[1] && diceResult[1] == diceResult[2]) {

                        diceOutCome.setTotal(Integer.parseInt(diceResult[0] + ""));
                        diceOutCome.setTriple(true);
                    }

                    if (variables.whenLastResult != whenResult) {
                        variables.whenLastResult = whenResult;

                        displayToView(diceOutCome);
                        variables.RESULT = "";
                    }

                }


                handler.postDelayed(this, 2000);


            }
        };
        handler.postDelayed(runnable, 2000);


    }


    private void displayToView(DiceOutCome diceOutCome) {


        cardsCollection.add(diceOutCome);


        List<DiceOutCome> cardsResults = new ArrayList<>();
        cardsResults.addAll(cardsCollection);
        Collections.reverse(cardsResults);
        //RESULT GRIDVIEW
        resultGridview = (GridView) findViewById(R.id.grid);
        CustomAdapterResultsOddEven customAdapterResults = new CustomAdapterResultsOddEven(getApplicationContext(), cardsResults);
        resultGridview.setAdapter(customAdapterResults);


        List<Integer> evens = Arrays.asList(evenNumbers);
        List<Integer> odds = Arrays.asList(oddNumbers);


        List<DiceOutCome> attemptResults = new ArrayList<>();
        attemptResults.addAll(cardsCollection);
        Collections.reverse(attemptResults);

        int c = attemptResults.get(0).getTotal();
        if (evens.contains(c)) {
            cardList.add(EVEN);

        } else if (odds.contains(c)) {
            cardList.add(ODD);
        } else {
            if (cardList.size() > 2) {
                cardList.add(TRIPLE);
            }
        }


        checkItc2bet4(diceOutCome);


        List<Integer> attemptsReversed = new ArrayList<>();
        attemptsReversed.addAll(attempts);
        Collections.reverse(attemptsReversed);


        //ATTEMPTS GRIDVIEW
        attemptsGridview = (GridView) findViewById(R.id.gridviewAttempts);
        CustomAdapterAttempts customAdapterAttempts = new CustomAdapterAttempts(getApplicationContext(), attemptsReversed);
        attemptsGridview.setAdapter(customAdapterAttempts);


    }

    List<String> getMoneyPicker(int selector) {


        List<String> chosenOne = new ArrayList<>();

        if (selector == 1) {
            chosenOne = new StrategySets().getMoneyPicker16().get(0);
        } else if (selector == 2) {
            chosenOne = new StrategySets().getMoneyPicker16().get(1);
        } else if (selector == 3) {
            chosenOne = new StrategySets().getMoneyPicker16().get(2);
        } else if (selector == 4) {
            chosenOne = new StrategySets().getMoneyPicker16().get(3);
        } else if (selector == 5) {
            chosenOne = new StrategySets().getMoneyPicker16().get(4);
        } else if (selector == 6) {
            chosenOne = new StrategySets().getMoneyPicker16().get(5);
        } else if (selector == 7) {
            chosenOne = new StrategySets().getMoneyPicker16().get(6);
        } else if (selector == 8) {
            chosenOne = new StrategySets().getMoneyPicker16().get(7);
        } else if (selector == 9) {
            chosenOne = new StrategySets().getMoneyPicker16().get(8);
        } else if (selector == 10) {
            chosenOne = new StrategySets().getMoneyPicker16().get(9);
        } else if (selector == 11) {
            chosenOne = new StrategySets().getMoneyPicker16().get(10);
        } else if (selector == 12) {
            chosenOne = new StrategySets().getMoneyPicker16().get(11);
        } else if (selector == 13) {
            chosenOne = new StrategySets().getMoneyPicker16().get(12);
        } else if (selector == 14) {
            chosenOne = new StrategySets().getMoneyPicker16().get(13);
        } else if (selector == 15) {
            chosenOne = new StrategySets().getMoneyPicker16().get(14);
        } else if (selector == 16) {
            chosenOne = new StrategySets().getMoneyPicker16().get(15);
        }


        return chosenOne;
    }


    List<String> getMatix256() {

        if (setIndex == 256) {
            setIndex = 1;
        }

        List<String> chosenOne = new StrategySets().getMatrix256().get(setIndex);
        setIndex++;

        return chosenOne;
    }


    List<String> getc2Bet4() {
        List<String> chosenOne = new ArrayList<>();
        if (setIndex == 5) {
            setIndex = 1;
        }
        String t1 = cardList.get(0);
        String t2 = cardList.get(1);

        if (t1.equals("E") && t2.equals("E")) {
            chosenOne = new StrategySets().getC2bet4().get(0);
            setIndex++;
        } else if (t1.equals("E") && t2.equals("O")) {
            chosenOne = new StrategySets().getC2bet4().get(1);
        } else if (t1.equals("O") && t2.equals("O")) {
            chosenOne = new StrategySets().getC2bet4().get(2);
        } else if (t1.equals("O") && t2.equals("E")) {
            chosenOne = new StrategySets().getC2bet4().get(3);
        }


        return chosenOne;
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

    String replaceTrigger(String i) {

        if (i.equals("O")) {
            return "E";
        } else if (i.equals("E")) {
            return "O";
        }
        return i;
    }

    private void checkItMoneyPicker(DiceOutCome diceOutCome) {

        int x = cardList.size();


        String card2 = "";
        String card3 = "";
        String card4 = "";
        String card5 = "";


        switch (x) {
            case 2:


                moneyPickerList = getMoneyPicker(diceOutCome.getTotal());


                mp1 = moneyPickerList.get(0);
                mp2 = moneyPickerList.get(1);
                mp3 = moneyPickerList.get(2);
                mp4 = moneyPickerList.get(3);

                attempt = 1;
                setForcast(mp1);
                attemptLabel.setVisibility(View.VISIBLE);
                attemptLabel.setText("MP " + mp1 + " - " + mp2 + " - " + mp3 + " - " + mp4);
                System.out.println("CASE 1" + "MP " + mp1 + " - " + mp2 + " - " + mp3 + " - " + mp4);

                break;
            case 11:


                if (diceOutCome.isTriple()) {
                    attempt++;
                    setForcast(mp2);
                } else {

                    card2 = cardList.get(1);
                    if (!card2.equals(mp1)) {
                        attempt++;
                        setForcast(mp2);
                    } else {
                        addResultIcon(1);
                    }
                }
                System.out.println("CASE 2");

                break;
            case 12:
                if (diceOutCome.isTriple()) {
                    attempt++;
                    setForcast(mp3);
                } else {

                    card3 = cardList.get(2);
                    if (!card3.equals(mp2)) {
                        attempt++;
                        setForcast(mp3);
                    } else {
                        addResultIcon(1);
                    }
                }
                System.out.println("CASE 3");
                break;
            case 13:
                if (diceOutCome.isTriple()) {
                    attempt++;
                    setForcast(mp4);
                } else {
                    card4 = cardList.get(3);
                    if (!card4.equals(mp3)) {
                        attempt++;
                        setForcast(mp4);
                    } else {

                        addResultIcon(1);
                    }
                }
                System.out.println("CASE 4");
                break;
            case 14:
                if (diceOutCome.isTriple()) {
                    attempt++;
                    addResultIcon(0);
                    isDeathSquare = true;
                } else {
                    card5 = cardList.get(4);
                    if (!card5.equals(mp4)) {
                        addResultIcon(0);
                        isDeathSquare = true;

                    } else {
                        addResultIcon(1);

                    }

                }
                System.out.println("CASE 5");
                break;
            default:
        }
        viewAttemptMoneyPicker();

    }


    private void checkItMatrix256(DiceOutCome diceOutCome) {

        int x = cardList.size();


        String card1 = "";
        String card2 = "";
        String card3 = "";
        String card4 = "";
        String card5 = "";
        String card6 = "";
        String card7 = "";
        String card8 = "";


        switch (x) {
            case 1:


                moneyPickerList = getMoneyPicker(diceOutCome.getTotal());

                mp1 = moneyPickerList.get(0);
                mp2 = moneyPickerList.get(1);
                mp3 = moneyPickerList.get(2);
                mp4 = moneyPickerList.get(3);
                mp5 = moneyPickerList.get(4);
                mp6 = moneyPickerList.get(5);
                mp7 = moneyPickerList.get(6);
                mp8 = moneyPickerList.get(7);

                attempt = 1;
                setForcast(mp1);
                attemptLabel.setText(mp1 + " - " + mp2 + " - " + mp3 + " - " + mp4 + " - " + mp5 + " - " + mp6 + " - " + mp7 + " - " + mp8);

                //    Toast.makeText(this,x+" -> ",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                if (diceOutCome.isTriple()) {
                    attempt++;
                    setForcast(mp2);
                } else {

                    card1 = cardList.get(1);
                    if (!card1.equals(mp1)) {
                        attempt++;
                        setForcast(mp2);
                    } else {
                        addResultIcon(1);
                    }
                }
                //   Toast.makeText(this,card1+" -> "+mp1,Toast.LENGTH_SHORT).show();
                break;
            case 3:
                if (diceOutCome.isTriple()) {
                    attempt++;
                    setForcast(mp3);
                } else {
                    card2 = cardList.get(2);
                    if (!card2.equals(mp2)) {
                        attempt++;
                        setForcast(mp3);
                    } else {

                        addResultIcon(1);
                    }
                }
                //    Toast.makeText(this,card2+" -> "+mp2,Toast.LENGTH_SHORT).show();
                break;
            case 4:
                if (diceOutCome.isTriple()) {
                    attempt++;
                    setForcast(mp4);
                } else {
                    card3 = cardList.get(3);
                    if (!card3.equals(mp3)) {
                        attempt++;
                        setForcast(mp4);
                    } else {
                        addResultIcon(1);
                    }
                }
                //    Toast.makeText(this,card3+" -> "+mp3,Toast.LENGTH_SHORT).show();
                break;
            case 5:
                if (diceOutCome.isTriple()) {
                    attempt++;
                    setForcast(mp5);
                } else {
                    card4 = cardList.get(4);
                    if (!card4.equals(mp4)) {
                        attempt++;
                        setForcast(mp5);
                    } else {
                        addResultIcon(1);
                    }
                }
                //    Toast.makeText(this,card4+" -> "+mp4,Toast.LENGTH_SHORT).show();
                break;
            case 6:
                if (diceOutCome.isTriple()) {
                    attempt++;
                    setForcast(mp6);
                } else {
                    card5 = cardList.get(5);
                    if (!card5.equals(mp5)) {
                        attempt++;
                        setForcast(mp6);
                    } else {
                        addResultIcon(1);
                    }
                }
                //     Toast.makeText(this,card5+" -> "+mp5,Toast.LENGTH_SHORT).show();
                break;
            case 7:
                if (diceOutCome.isTriple()) {
                    attempt++;
                    setForcast(mp7);
                } else {
                    card6 = cardList.get(6);
                    if (!card6.equals(mp6)) {
                        attempt++;
                        setForcast(mp7);
                    } else {
                        addResultIcon(1);
                    }
                }
                //    Toast.makeText(this,card6+" -> "+mp6,Toast.LENGTH_SHORT).show();
                break;
            case 8:
                if (diceOutCome.isTriple()) {
                    attempt++;
                    setForcast(mp8);
                } else {
                    card7 = cardList.get(7);
                    if (!card7.equals(mp7)) {
                        attempt++;
                        setForcast(mp8);
                    } else {
                        addResultIcon(1);
                    }
                }
                //        Toast.makeText(this,card7+" -> "+mp7,Toast.LENGTH_SHORT).show();
                break;
            case 9:
                if (diceOutCome.isTriple()) {
                    attempt++;
                    addResultIcon(0);
                    isDeathSquare = true;
                } else {
                    card8 = cardList.get(8);
                    if (!card8.equals(mp8)) {
                        addResultIcon(0);
                        isDeathSquare = true;
                    } else {
                        addResultIcon(1);
                    }
                }
//                Toast.makeText(this,card8+" -> "+mp8,Toast.LENGTH_SHORT).show();
                break;
            default:

        }

        viewAttemptMatrix256();

    }

    private void checkItc2bet4(DiceOutCome diceOutCome) {

        int x = cardList.size();


        String card3 = "";
        String card4 = "";
        String card5 = "";
        String card6 = "";


        switch (x) {
            case 2:


                moneyPickerList = getc2Bet4();
                mp1 = moneyPickerList.get(0);
                mp2 = moneyPickerList.get(1);
                mp3 = moneyPickerList.get(2);
                mp4 = moneyPickerList.get(3);


                attempt = 1;
                setForcast(mp1);
                attemptLabel.setText("C2BET4: " + mp1 + " - " + mp2 + " - " + mp3 + " - " + mp4);

                break;
            case 3:
                if (diceOutCome.isTriple()) {
                    attempt++;
                    setForcast(mp2);
                } else {

                    card3 = cardList.get(2);
                    if (!card3.equals(mp1)) {
                        attempt++;
                        setForcast(mp2);
                    } else {
                        addResultIcon(1);
                    }
                }

                break;
            case 4:
                if (diceOutCome.isTriple()) {
                    attempt++;
                    setForcast(mp3);
                } else {
                    card4 = cardList.get(3);
                    if (!card4.equals(mp2)) {
                        attempt++;
                        setForcast(mp3);
                    } else {

                        addResultIcon(1);
                    }
                }
                //    Toast.makeText(this,card2+" -> "+mp2,Toast.LENGTH_SHORT).show();
                break;
            case 5:
                if (diceOutCome.isTriple()) {
                    attempt++;
                    setForcast(mp4);
                } else {
                    card5 = cardList.get(4);
                    if (!card5.equals(mp3)) {
                        attempt++;
                        setForcast(mp4);
                    } else {
                        addResultIcon(1);
                    }
                }
                //    Toast.makeText(this,card3+" -> "+mp3,Toast.LENGTH_SHORT).show();
                break;
            case 6:
                if (diceOutCome.isTriple()) {

                    attempt++;
                    addResultIcon(0);
                    isDeathSquare = true;
                } else {
                    card6 = cardList.get(5);
                    if (!card6.equals(mp4)) {
                        addResultIcon(0);
                        isDeathSquare = true;

                    } else {
                        addResultIcon(1);

                    }

                }

                break;
            default:

        }

        viewAttemptc2Bet4();

    }

    private void viewAttemptMatrix256() {


        txtBoxAttemptCount.setText(attempt + "");


        layoutSignal.setVisibility(View.VISIBLE);
        imgSicBoLogoCenter.setVisibility(View.INVISIBLE);

        if (attempt == 8) {

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


        } else if (attempt == 7) {

            playNotification();

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


        } else if (attempt >= 2 && attempt <= 6) {

            attemptLabel.setVisibility(View.VISIBLE);
            txtBoxAttemptCount.setVisibility(View.VISIBLE);
            txtBoxAttemptCount.setTextColor(Color.WHITE);

        } else if (attempt == 1) {


            txtBoxAttemptCount.setTextColor(Color.WHITE);
            attemptLabel.setVisibility(View.VISIBLE);
            txtBoxAttemptCount.setVisibility(View.VISIBLE);
        } else {
            //  Toast.makeText(this, "here "+attempt, Toast.LENGTH_SHORT).show();
            txtBoxAttemptCount.clearAnimation();
            attemptLabel.setVisibility(View.INVISIBLE);
            txtBoxAttemptCount.setVisibility(View.INVISIBLE);

            layoutSignal.setVisibility(View.INVISIBLE);
            imgSicBoLogoCenter.setVisibility(View.VISIBLE);


        }


    }

    private void viewAttemptMoneyPicker() {


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
            //  Toast.makeText(this, "here "+attempt, Toast.LENGTH_SHORT).show();
            txtBoxAttemptCount.clearAnimation();
            attemptLabel.setVisibility(View.INVISIBLE);
            txtBoxAttemptCount.setVisibility(View.INVISIBLE);

            layoutSignal.setVisibility(View.INVISIBLE);
            imgSicBoLogoCenter.setVisibility(View.VISIBLE);


        }


    }

    private void viewAttemptc2Bet4() {


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
            //  Toast.makeText(this, "here "+attempt, Toast.LENGTH_SHORT).show();
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
            isDeathSquare = false;


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


        displayToView(new DiceOutCome(3, false));
        variables.RESULT = "";


    }

    public void oddClicked(View view) {
        displayToView(new DiceOutCome(4, false));
        variables.RESULT = "";
    }

    public void tripleClicked(View view) {


        int min = 3; // Minimum value (inclusive)
        int max = 17; // Maximum value (exclusive)

        Random random = new Random();
        int randomNumber = random.nextInt(max - min) + min;


        displayToView(new DiceOutCome(randomNumber, false));
        variables.RESULT = "";
    }


}