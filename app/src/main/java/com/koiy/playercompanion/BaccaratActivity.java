package com.koiy.playercompanion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BaccaratActivity extends AppCompatActivity {
    RelativeLayout layoutSignalBaccarat;

    LinearLayout AttemptsLayout;
    TextView txtSignalBaccarat, txtAttemptBaccarat;
    private AdapterBaccarat adapterBaccarat;
    LinearLayout container;
    RecyclerView recyclerView;

    List<String> resultsList, resultListAll;

ImageButton undoButton;
    int attempt = 0;
    private MediaPlayer mediaPlayer;
    List<Integer> cardsCollection = new ArrayList<>();

    List<Integer> attempts = new ArrayList<>();

    String PLAYER = "P";
    String BANKER = "B";
    String TIE = "T";

    List<String> cardList = new ArrayList<>();
    List<String> setA = new ArrayList<>(Arrays.asList("P", "P", "P", "B", "B", "B"));
    List<String> setB = new ArrayList<>(Arrays.asList("P", "B", "P", "P", "B", "P"));
    List<String> setC = new ArrayList<>(Arrays.asList("B", "B", "B", "P", "P", "P"));
    List<String> setD = new ArrayList<>(Arrays.asList("B", "P", "B", "B", "P", "B"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baccarat);
        container = (LinearLayout) this.findViewById(R.id.container);
        AttemptsLayout = (LinearLayout) this.findViewById(R.id.AttemptsLayout);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        resultListAll = new ArrayList<>();

        recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        container.addView(recyclerView);

        resultsList = new ArrayList<>();
        displayResultToView(resultsList);

        AttemptsLayout = (LinearLayout) this.findViewById(R.id.AttemptsLayout);
        layoutSignalBaccarat = (RelativeLayout) this.findViewById(R.id.layoutSignalBaccarat);
        txtSignalBaccarat = (TextView) this.findViewById(R.id.txtSignalBaccarat);
        txtAttemptBaccarat = (TextView) this.findViewById(R.id.txtAttemptBaccarat);
        undoButton = this.findViewById(R.id.undoButton);

        undoButton.setVisibility(View.INVISIBLE);

    }


    public void bankerBaccatClicked(View view) {
        updateView("B");
    }

    public void playerBaccaratClicked(View view) {


        updateView("P");


    }

    void updateView(String result) {

        List<String> list = new ArrayList<>();
        list.addAll(resultsList);


        if (result.equals("UNDO")) {

            int lastIndex = list.size() - 1;
            if (lastIndex >= 0) {
                list.remove(lastIndex);
            }
            adapterBaccarat.updateData(list);

            if (list.size() == 0) {
                Toast.makeText(this, "Only 1 column is allowed to undo", Toast.LENGTH_SHORT).show();
            }

        } else {
            resultListAll.add(result);

            list.add(result);
            if (list.size() == 7) {


                recyclerView = new RecyclerView(this);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                container.addView(recyclerView);

                resultsList = new ArrayList<>();
                resultsList.add(result);
                displayResultToView(resultsList);

            } else {

                adapterBaccarat.updateData(list);
            }
        }

        addCardToList(result);

    }

    public void tieBaccaratClicked(View view) {
        updateView("T");
    }


    private void displayResultToView(List<String> result) {


        adapterBaccarat = new AdapterBaccarat(result);
        recyclerView.setAdapter(adapterBaccarat);


    }

    public void undoBaccatClicked(View view) {

        updateView("UNDO");
    }

    public void resetBaccaratClicked(View view) {


        showConfirmationDialog();


    }


    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setMessage("Do you want to proceed?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked Yes
                        resetAll();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked No
                        // Toast.makeText(BaccaratActivity.this, "No clicked", Toast.LENGTH_SHORT).show();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void resetAll() {

        container.removeAllViews();
        recyclerView = new RecyclerView(BaccaratActivity.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(BaccaratActivity.this));
        container.addView(recyclerView);

        resultsList = new ArrayList<>();
        displayResultToView(resultsList);

        layoutSignalBaccarat.setBackgroundResource(R.color.dark_green);
        txtAttemptBaccarat.setText("");
        txtSignalBaccarat.setText("");

        AttemptsLayout.removeAllViews();
    }


    private void addCardToList(String card) {

//        for (String card : cards) {


        if (card.equals(PLAYER)) {
            cardList.add(PLAYER);
            checkIt();
        } else if (card.equals(BANKER)) {
            cardList.add(BANKER);
            checkIt();
        } else {
            if (cardList.size() >= 2) {
                cardList.add(TIE);
            }
        }


//        }

    }


    private void checkIt() {

        int x = cardList.size();


        String card1 = "";
        String card2 = "";
        String card3 = "";
        String card4 = "";
        String card5 = "";
        String card6 = "";

        String playerSignal = "PLAYER";
        String bankerSignal = "BANKER";

        switch (x) {

            case 2:
                card1 = cardList.get(0);
                card2 = cardList.get(1);
                if (card1.equals(PLAYER) && card2.equals(PLAYER)) {
                    attempt++;
                    txtSignalBaccarat.setText(playerSignal);
                    layoutSignalBaccarat.setBackgroundResource(R.color.blue);
                } else if (card1.equals(PLAYER) && card2.equals(BANKER)) {
                    attempt++;
                    txtSignalBaccarat.setText(playerSignal);
                    layoutSignalBaccarat.setBackgroundResource(R.color.blue);
                } else if (card1.equals(BANKER) && card2.equals(BANKER)) {
                    attempt++;
                    txtSignalBaccarat.setText(bankerSignal);
                    layoutSignalBaccarat.setBackgroundResource(R.color.red);
                } else if (card1.equals(BANKER) && card2.equals(PLAYER)) {
                    attempt++;
                    txtSignalBaccarat.setText(bankerSignal);
                    layoutSignalBaccarat.setBackgroundResource(R.color.red);
                }

                break;

            case 3:
                card1 = cardList.get(0);
                card2 = cardList.get(1);
                card3 = cardList.get(2);

                if (card1.equals(PLAYER) && card2.equals(PLAYER)) {
                    if (!card3.equals(setA.get(2))) {
                        attempt++;
                        txtSignalBaccarat.setText(bankerSignal);
                        layoutSignalBaccarat.setBackgroundResource(R.color.red);
                    } else {

                        txtSignalBaccarat.setText("");
                        layoutSignalBaccarat.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }


                } else if (card1.equals(PLAYER) && card2.equals(BANKER)) {
                    if (!card3.equals(setB.get(2))) {
                        attempt++;
                        txtSignalBaccarat.setText(playerSignal);
                        layoutSignalBaccarat.setBackgroundResource(R.color.blue);
                    } else {

                        txtSignalBaccarat.setText("");
                        layoutSignalBaccarat.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                } else if (card1.equals(BANKER) && card2.equals(BANKER)) {
                    if (!card3.equals(setC.get(2))) {
                        attempt++;
                        txtSignalBaccarat.setText(playerSignal);
                        layoutSignalBaccarat.setBackgroundResource(R.color.blue);
                    } else {

                        txtSignalBaccarat.setText("");
                        layoutSignalBaccarat.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                } else if (card1.equals(BANKER) && card2.equals(PLAYER)) {

                    if (!card3.equals(setD.get(2))) {
                        attempt++;
                        txtSignalBaccarat.setText(bankerSignal);
                        layoutSignalBaccarat.setBackgroundResource(R.color.red);
                    } else {

                        txtSignalBaccarat.setText("");
                        layoutSignalBaccarat.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                }

                break;

            case 4:
                card1 = cardList.get(0);
                card2 = cardList.get(1);
                card4 = cardList.get(3);

                if (card1.equals(PLAYER) && card2.equals(PLAYER)) {
                    if (!card4.equals(setA.get(3))) {
                        attempt++;
                        txtSignalBaccarat.setText(bankerSignal);
                        layoutSignalBaccarat.setBackgroundResource(R.color.red);
                    } else {

                        txtSignalBaccarat.setText("");
                        layoutSignalBaccarat.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                } else if (card1.equals(BANKER) && card2.equals(BANKER)) {
                    if (!card4.equals(setB.get(3))) {
                        attempt++;
                        txtSignalBaccarat.setText(playerSignal);
                        layoutSignalBaccarat.setBackgroundResource(R.color.blue);
                    } else {

                        txtSignalBaccarat.setText("");
                        layoutSignalBaccarat.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                } else if (card1.equals(PLAYER) && card2.equals(BANKER)) {
                    if (!card4.equals(setC.get(3))) {
                        attempt++;
                        txtSignalBaccarat.setText(bankerSignal);
                        layoutSignalBaccarat.setBackgroundResource(R.color.red);
                    } else {

                        txtSignalBaccarat.setText("");
                        layoutSignalBaccarat.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                } else if (card1.equals(BANKER) && card2.equals(PLAYER)) {

                    if (!card4.equals(setD.get(3))) {
                        attempt++;
                        txtSignalBaccarat.setText(playerSignal);
                        layoutSignalBaccarat.setBackgroundResource(R.color.blue);
                    } else {

                        txtSignalBaccarat.setText("");
                        layoutSignalBaccarat.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                }

                break;


            case 5:
                card1 = cardList.get(0);
                card2 = cardList.get(1);
                card5 = cardList.get(4);

                if (card1.equals(PLAYER) && card2.equals(PLAYER)) {
                    if (!card5.equals(setA.get(4))) {
                        attempt++;
                        txtSignalBaccarat.setText(bankerSignal);
                        layoutSignalBaccarat.setBackgroundResource(R.color.red);
                    } else {

                        txtSignalBaccarat.setText("");
                        layoutSignalBaccarat.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                } else if (card1.equals(PLAYER) && card2.equals(BANKER)) {
                    if (!card5.equals(setB.get(4))) {
                        attempt++;
                        txtSignalBaccarat.setText(playerSignal);
                        layoutSignalBaccarat.setBackgroundResource(R.color.blue);
                    } else {

                        txtSignalBaccarat.setText("");
                        layoutSignalBaccarat.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                } else if (card1.equals(BANKER) && card2.equals(BANKER)) {
                    if (!card5.equals(setC.get(4))) {
                        attempt++;
                        txtSignalBaccarat.setText(playerSignal);
                        layoutSignalBaccarat.setBackgroundResource(R.color.blue);
                    } else {

                        txtSignalBaccarat.setText("");
                        layoutSignalBaccarat.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                } else if (card1.equals(BANKER) && card2.equals(PLAYER)) {

                    if (!card5.equals(setD.get(4))) {
                        attempt++;
                        txtSignalBaccarat.setText(bankerSignal);
                        layoutSignalBaccarat.setBackgroundResource(R.color.red);
                    } else {

                        txtSignalBaccarat.setText("");
                        layoutSignalBaccarat.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);

                    }
                }

                break;


            case 6:
                card1 = cardList.get(0);
                card2 = cardList.get(1);
                card6 = cardList.get(5);

                if (card1.equals(PLAYER) && card2.equals(PLAYER)) {
                    Log.e("setA", card6 + " " + setA.get(5));
                    if (card6.equals(setA.get(5))) {

                        txtSignalBaccarat.setText("");
                        layoutSignalBaccarat.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);
                    } else {
                        addResultIcon(0);
                    }
                } else if (card1.equals(PLAYER) && card2.equals(BANKER)) {
                    Log.e("setB", card6 + " " + setB.get(5));

                    if (card6.equals(setB.get(5))) {

                        txtSignalBaccarat.setText("");
                        layoutSignalBaccarat.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);
                    } else {
                        addResultIcon(0);
                    }
                } else if (card1.equals(BANKER) && card2.equals(BANKER)) {
                    Log.e("setC", card6 + " " + setC.get(5));
                    if (card6.equals(setC.get(5))) {

                        txtSignalBaccarat.setText("");
                        layoutSignalBaccarat.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);
                    } else {
                        addResultIcon(0);
                    }
                } else if (card1.equals(BANKER) && card2.equals(PLAYER)) {
                    Log.e("setD", card6 + " " + setD.get(5));
                    if (card6.equals(setD.get(5))) {
                        txtSignalBaccarat.setText("");
                        layoutSignalBaccarat.setBackgroundResource(R.color.dark_blue);
                        cardList.clear();
                        addResultIcon(1);
                    } else {
                        addResultIcon(0);
                    }
                }

                break;


        }


        viewAttempt();
    }

    private void viewAttempt() {


        txtAttemptBaccarat.setText("Attempt " + attempt);


        if (attempt == 4) {

            playNotification();

            txtAttemptBaccarat.setVisibility(View.VISIBLE);
            txtAttemptBaccarat.setTextColor(getResources().getColor(R.color.darkRed));

            // Create the blinking animation
            Animation blinkAnimation = new AlphaAnimation(0.0f, 1.0f);
            blinkAnimation.setDuration(200); // Set the duration of each blink
            blinkAnimation.setRepeatMode(Animation.REVERSE);
            blinkAnimation.setRepeatCount(Animation.INFINITE);
            // Apply the animation to the TextView
            txtAttemptBaccarat.startAnimation(blinkAnimation);


        } else if (attempt == 3) {


            txtAttemptBaccarat.setVisibility(View.VISIBLE);
            txtAttemptBaccarat.setTextColor(Color.YELLOW);

            // Create the blinking animation
            Animation blinkAnimation = new AlphaAnimation(0.0f, 1.0f);
            blinkAnimation.setDuration(200); // Set the duration of each blink
            blinkAnimation.setRepeatMode(Animation.REVERSE);
            blinkAnimation.setRepeatCount(Animation.INFINITE);
            // Apply the animation to the TextView
            txtAttemptBaccarat.startAnimation(blinkAnimation);


        } else if (attempt == 2) {
            txtAttemptBaccarat.setVisibility(View.VISIBLE);
            txtAttemptBaccarat.setTextColor(Color.WHITE);

        } else if (attempt == 1) {

            txtAttemptBaccarat.setTextColor(Color.WHITE);
            txtAttemptBaccarat.setVisibility(View.VISIBLE);
        } else {

            txtAttemptBaccarat.clearAnimation();
            txtAttemptBaccarat.setVisibility(View.INVISIBLE);
            layoutSignalBaccarat.setBackgroundResource(R.color.dark_green);


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

        TextView  textView =  new TextView(BaccaratActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
               60,
                60
        );
        layoutParams.setMargins(2,2,2,2);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.white));

        if (i == 1) {
            attempts.add(attempt);
            textView.setText(attempt+"");
            textView.setBackgroundResource(R.drawable.circle_green);

        } else {

            cardList.clear();
            attempts.add(0); //this is for DS
            txtSignalBaccarat.setText("");
            layoutSignalBaccarat.setBackgroundResource(R.color.dark_green);
            textView.setText("X");
            textView.setBackgroundResource(R.drawable.circle_red);
        }
        AttemptsLayout.addView(textView);
        attempt = 0;

    }


}
