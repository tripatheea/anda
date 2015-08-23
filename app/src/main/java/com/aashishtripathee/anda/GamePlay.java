package com.aashishtripathee.anda;

import com.aashishtripathee.anda.util.SystemUiHider;

import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import android.content.SharedPreferences;

import android.content.Context;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.widget.TextView;


import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class GamePlay extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 500; // 3000

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    private Handler frame = new Handler();

    private static final long FRAME_RATE = 1; // 50 frames per second




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);
        Handler h = new Handler();

        initializeViews();

        //We can't initialize the graphics immediately because the layout manager
        //needs to run first, thus we call back in a sec.
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                initGfx();
            }
        }, 1000);


        // Get highest score.
        SharedPreferences prefs = getSharedPreferences("andaPreferences", Context.MODE_PRIVATE);
        int lastHighestScore = prefs.getInt("highestScore", 0); //0 is the default value
        ((GameBoard) findViewById(R.id.the_canvas)).setHighestScore(lastHighestScore);


    }

    public void initializeViews() {

    }



    synchronized public void initGfx() {
        //It's a good idea to remove any existing callbacks to keep
        //them from inadvertently stacking up.

        frame.removeCallbacks(frameUpdate);
        frame.postDelayed(frameUpdate, FRAME_RATE);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }


    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            ((GameBoard) findViewById(R.id.the_canvas)).invalidate();
            frame.postDelayed(frameUpdate, FRAME_RATE);
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    private Runnable frameUpdate = new Runnable() {
        @Override
        synchronized public void run() {
            ((GameBoard) findViewById(R.id.the_canvas)).invalidate();
            frame.postDelayed(frameUpdate, FRAME_RATE);


            Button button = (Button) findViewById(R.id.play_again);

            button.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ((GameBoard) findViewById(R.id.the_canvas)).setGameStatus("gamePlay");
                    findViewById(R.id.play_again).setVisibility(View.GONE);
                    findViewById(R.id.play_again).setEnabled(false);

                }
            });


            if (((GameBoard) findViewById(R.id.the_canvas)).getGameStatus() == "gameOver") {
                button.setVisibility(View.VISIBLE);
                button.setEnabled(true);
            }

            // Get highest score.

            SharedPreferences prefs = getSharedPreferences("andaPreferences", Context.MODE_PRIVATE);

            int lastHighestScore = prefs.getInt("highestScore", 0); //0 is the default value
            int currentScore = ((GameBoard) findViewById(R.id.the_canvas)).getScore();


            if (currentScore > lastHighestScore) {
                // Store highest score.
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("highestScore", currentScore);
                editor.commit();

                System.out.println("Writing highest score!");

                ((GameBoard) findViewById(R.id.the_canvas)).setHighestScore(currentScore);
            }

        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        ((GameBoard) findViewById(R.id.the_canvas)).moveEggFromBasket();

        ((GameBoard) findViewById(R.id.the_canvas)).invalidate();
        frame.postDelayed(frameUpdate, FRAME_RATE);


        return true;

    }




}
