package com.koiy.playercompanion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;

public class BaccaratHongkongActivity extends AppCompatActivity {
    RelativeLayout layoutSignalBaccarat;
    private AudioManager audioManager;


    String previousCard;
    LinearLayout AttemptsLayout;
    TextView txtSignalBaccarat, txtAttemptBaccarat;
    private AdapterBaccaratBigRoad adapterBaccaratBigRoad;
    private AdapterBaccaratBeadRoad adapterBaccaratBeadRoad;
    LinearLayout containerBigRoad;
    LinearLayout containerBeadRoad;
    RecyclerView recyclerViewBigRoad, recyclerViewBeadRoad;

    List<String> resultsListBigRaod, resultListAll, hongkongPatternList, hongkongToCompareList;
    List<String> resultsListBeadRoad;
    ImageButton undoButton;
    int attempt = 0;

    private Handler handler;
    private Timer timer;
    private int scrollSpeed = 1; // Adjust this value to control scroll speed
    private int delayMillis = 10; // Adjust this value to control delay between scrolls

    private HorizontalScrollView horizontalScrollViewBeadRoad, horizontalScrollViewBigRoad;

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

        setContentView(R.layout.activity_baccarat_hongkong);

        containerBigRoad = (LinearLayout) this.findViewById(R.id.containerBigRoad);
        containerBeadRoad = (LinearLayout) this.findViewById(R.id.containerBeadRoad);

        AttemptsLayout = (LinearLayout) this.findViewById(R.id.AttemptsLayout);
        horizontalScrollViewBeadRoad = findViewById(R.id.horizontalScrollViewBeadRoad);
        horizontalScrollViewBigRoad = findViewById(R.id.horizontalScrollViewBigRoad);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        resultListAll = new ArrayList<>();
        hongkongPatternList = new ArrayList<>();
        hongkongToCompareList = new ArrayList<>();

        recyclerViewBeadRoad = new RecyclerView(this);
        recyclerViewBeadRoad.setLayoutManager(new LinearLayoutManager(this));
        containerBeadRoad.addView(recyclerViewBeadRoad);
        resultsListBeadRoad = new ArrayList<>();
        displayResultToViewBeadRoad(resultsListBeadRoad);


        recyclerViewBigRoad = new RecyclerView(this);
        recyclerViewBigRoad.setLayoutManager(new LinearLayoutManager(this));
        containerBigRoad.addView(recyclerViewBigRoad);
        resultsListBigRaod = new ArrayList<>();
        displayResultToViewBigRoad(resultsListBigRaod);


        layoutSignalBaccarat = (RelativeLayout) this.findViewById(R.id.layoutSignalBaccarat);
        txtSignalBaccarat = (TextView) this.findViewById(R.id.txtSignalBaccarat);
        txtAttemptBaccarat = (TextView) this.findViewById(R.id.txtAttemptBaccarat);
        undoButton = this.findViewById(R.id.undoButton);




        handler = new Handler();

        startAutoScroll();


    }

    void updateViewBeadRoad(String result) {

        List<String> list = new ArrayList<>();
        list.addAll(resultsListBeadRoad);


        if (result.equals("UNDO")) {

            int lastIndex = list.size() - 1;
            if (lastIndex >= 0) {
                list.remove(lastIndex);
            }
            adapterBaccaratBeadRoad.updateData(list);

            if (list.size() == 0) {
                Toast.makeText(this, "Only 1 column is allowed to undo", Toast.LENGTH_SHORT).show();
            }

        } else {
            resultsListBeadRoad.add(result);

            list.add(result);
            if (list.size() == 7) {


                recyclerViewBeadRoad = new RecyclerView(this);
                recyclerViewBeadRoad.setLayoutManager(new LinearLayoutManager(this));
                containerBeadRoad.addView(recyclerViewBeadRoad);

                resultsListBeadRoad = new ArrayList<>();
                resultsListBeadRoad.add(result);
                displayResultToViewBeadRoad(resultsListBeadRoad);

            } else {

                adapterBaccaratBeadRoad.updateData(list);
            }
        }


    }

    private void startAutoScroll() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int scrollX = horizontalScrollViewBigRoad.getScrollX();
                int maxScrollX = containerBigRoad.getWidth() - horizontalScrollViewBigRoad.getWidth();

                if (scrollX < maxScrollX) {
                    horizontalScrollViewBigRoad.smoothScrollTo(scrollX + scrollSpeed, 0);
                }

                handler.postDelayed(this, delayMillis);
            }
        }, delayMillis);
    }


    public void bankerBaccatClicked(View view) {

        updateViewBeadRoad("B");
        updateViewBigRoad("B");
    }

    public void playerBaccaratClicked(View view) {


        updateViewBigRoad("P");
        updateViewBeadRoad("P");


    }

    void updateViewBigRoad(String result) {


        if (result.equals("UNDO")) {
            List<String> list = new ArrayList<>();
            list.addAll(resultsListBigRaod);
            int lastIndex = list.size() - 1;
            if (lastIndex >= 0) {
                list.remove(lastIndex);
            }
            adapterBaccaratBigRoad.updateData(list);


        } else {
            List<String> list = new ArrayList<>();
            list.addAll(resultsListBigRaod);


            resultListAll.add(result);

            if (resultListAll.size() <= 5) {

                hongkongPatternList.clear();
                hongkongPatternList.addAll(resultListAll);

            } else {

                String lastItem = resultListAll.get(resultListAll.size() - 1);
                hongkongToCompareList.add(lastItem);
            }


            checkIt(hongkongPatternList, hongkongToCompareList);


            list.add(result);
            if (result != TIE && previousCard != result) {
                previousCard = result;
                recyclerViewBigRoad = new RecyclerView(this);
                recyclerViewBigRoad.setLayoutManager(new LinearLayoutManager(this));
                containerBigRoad.addView(recyclerViewBigRoad);

                resultsListBigRaod = new ArrayList<>();
                resultsListBigRaod.add(result);
                displayResultToViewBigRoad(resultsListBigRaod);

            } else {

                adapterBaccaratBigRoad.updateData(list);
            }


            playSound();
        }

//        addCardToList(result);


    }


    private void playSound() {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.clicked_sound);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release(); // Release the MediaPlayer object after the sound has finished playing
            }
        });
    }


    public void tieBaccaratClicked(View view) {
        updateViewBeadRoad("T");


    }


    private void displayResultToViewBigRoad(List<String> result) {

        adapterBaccaratBigRoad = new AdapterBaccaratBigRoad(result);
        recyclerViewBigRoad.setAdapter(adapterBaccaratBigRoad);


    }


    private void displayResultToViewBeadRoad(List<String> result) {

        adapterBaccaratBeadRoad = new AdapterBaccaratBeadRoad(result);
        recyclerViewBeadRoad.setAdapter(adapterBaccaratBeadRoad);


    }


    public void undoBaccatClicked(View view) {

        updateViewBigRoad("UNDO");
    }

    public void resetBaccaratClicked(View view) {


        showConfirmationDialog();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
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

    void viewSignal(String card) {


        txtAttemptBaccarat.setText("Attempt:" + attempt);
        if (card.equals("P")) {
            txtSignalBaccarat.setText("PLAYER");
            layoutSignalBaccarat.setBackgroundResource(R.color.blue);
        } else if (card.equals("B")) {
            txtSignalBaccarat.setText("BANKER");
            layoutSignalBaccarat.setBackgroundResource(R.color.red);
        } else {
            txtSignalBaccarat.setText("");
            txtAttemptBaccarat.setText("");
            layoutSignalBaccarat.setBackgroundResource(R.color.white);
        }


    }

    private void resetAll() {


        previousCard = "";

        containerBigRoad.removeAllViews();
        recyclerViewBigRoad = new RecyclerView(this);
        recyclerViewBigRoad.setLayoutManager(new LinearLayoutManager(BaccaratHongkongActivity.this));
        containerBigRoad.addView(recyclerViewBigRoad);
        resultsListBigRaod = new ArrayList<>();
        displayResultToViewBigRoad(resultsListBigRaod);

        containerBeadRoad.removeAllViews();
        recyclerViewBeadRoad = new RecyclerView(this);
        recyclerViewBeadRoad.setLayoutManager(new LinearLayoutManager(BaccaratHongkongActivity.this));
        containerBeadRoad.addView(recyclerViewBeadRoad);
        resultsListBeadRoad = new ArrayList<>();
        displayResultToViewBeadRoad(resultsListBeadRoad);







        layoutSignalBaccarat.setBackgroundResource(R.color.dark_green);
        txtAttemptBaccarat.setText("");
        txtSignalBaccarat.setText("");

        attempt = 0;
        attempts.clear();
        cardList.clear();




    }


    boolean comparePreviousResult(String patter, String toCompare) {
        boolean isTheSame = false;

        if (patter.equals(toCompare)) {
            isTheSame = true;
        }
        return isTheSame;
    }

    String extractCard(List<String> cards, int index) {
        return cards.get(index);
    }

    private void checkIt(List<String> pattern, List<String> toCompare) {


        int patternSize = pattern.size();


        if (patternSize == 5) {


            String card1 = pattern.get(0);
            String card2 = pattern.get(1);
            String card3 = pattern.get(2);
            String card4 = pattern.get(3);
            String card5 = pattern.get(4);


            int toCompareSize = toCompare.size();

            if (toCompareSize == 1) {

                attempt++;
                String toCompareCard1 = toCompare.get(0);
                Boolean isTheSame = comparePreviousResult(card1, toCompareCard1);

                if (isTheSame) {
                    String nextSignal = extractCard(hongkongPatternList, 1);
                    Log.e("SET 1-A", nextSignal);
                    viewSignal(nextSignal);

                } else {

                    String nextSignal = extractCard(hongkongPatternList, 1);
                    if (nextSignal.equals("P")) {
                        nextSignal = "B";
                    } else {
                        nextSignal = "P";
                    }

                    Log.e("SET 1-B", nextSignal);
                    viewSignal(nextSignal);
                }


            } else if (toCompareSize == 2) {

                attempt++;

                String toCompareCard2 = toCompare.get(1);
                Boolean isTheSame = comparePreviousResult(card2, toCompareCard2);
                if (isTheSame) {

                    addResultIcon(1);
                } else {

                    String nextSignal = extractCard(hongkongPatternList, 2);
                    if (nextSignal.equals("P")) {
                        nextSignal = "B";
                    } else {
                        nextSignal = "P";
                    }

                    Log.e("SET 2-B", nextSignal);
                    viewSignal(nextSignal);

                }

            } else if (toCompareSize == 3) {

                attempt++;
                String toCompareCard3 = toCompare.get(2);
                Boolean isTheSame = comparePreviousResult(card3, toCompareCard3);
                if (isTheSame) {

                    addResultIcon(1);


                } else {

                    String nextSignal = extractCard(hongkongPatternList, 3);
                    if (nextSignal.equals("P")) {
                        nextSignal = "B";
                    } else {
                        nextSignal = "P";
                    }


                    Log.e("SET 3-B", nextSignal);
                    viewSignal(nextSignal);

                }

            }
        }

    }


    private void playNotification() {

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.notification);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release(); // Release the MediaPlayer object after the sound has finished playing
            }
        });


    }


    private void addResultIcon(int i) {

        ImageView imgResult = new ImageView(BaccaratHongkongActivity.this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                70, 70
        );
        layoutParams.setMargins(2, 2, 2, 2);
        imgResult.setLayoutParams(layoutParams);


        if (i == 1) {
            attempts.add(attempt);
            imgResult.setImageDrawable(getResources().getDrawable(R.drawable.check_circle));

        } else {

            cardList.clear();
            attempts.add(0); //this is for DS
            txtSignalBaccarat.setText("");
            layoutSignalBaccarat.setBackgroundResource(R.color.dark_green);
            imgResult.setImageDrawable(getResources().getDrawable(R.drawable.cross_circle));
        }
        AttemptsLayout.addView(imgResult);

        attempt = 0;
        txtSignalBaccarat.setText("");
        txtAttemptBaccarat.setText("");
        layoutSignalBaccarat.setBackgroundResource(R.color.dark_green);


        hongkongToCompareList.clear();


    }


}
