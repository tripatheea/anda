package com.aashishtripathee.anda;

/**
 * Created by aashish on 5/29/15.
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.nfc.Tag;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;

import static java.util.Arrays.asList;
import android.widget.Button;

public class GameBoard extends View {


    public static final float gravity = 2.0F; // 6000.0F

    private Paint p;
    Context context;

    private Egg egg;
    private ArrayList<Basket> baskets = new ArrayList<Basket>();
    private int screenNumber;

    private int indexOfBasketHoldingEgg;

    private int screenWidth;
    private int screenHeight;

    private int highestScore;

    private int score;

    private Boolean holdOnAddingBaskets = false;

    private int whereIsEgg; // -1 if on air otherwiese indexOfBasketHoldingEgg.


    private String gameStatus = "gamePlay"; // "home", "gamePlay", or "gameOver"

    private ArrayList<Float> basketYPositions = new ArrayList<>(Arrays.asList(80.0F, 50.0F, 20.0F));


    public GameBoard(Context context, AttributeSet aSet) {
        super(context, aSet);
        this.context = context;
        //it's best not to create any new objects in the on draw
        //initialize them as class variables here
        p = new Paint();

        this.screenWidth = getResources().getDisplayMetrics().widthPixels;
        this.screenHeight = getResources().getDisplayMetrics().heightPixels;
        this.screenNumber = 1;

        this.score = 0;
        this.highestScore = 0;
        this.indexOfBasketHoldingEgg = 0;


        // Draw stuff.


        this.addBaskets();
        this.addEggInitial();   // Need to initialize baskets before the egg.
        //this.drawScore();
    }

    public int getScore() {
        return this.score;
    }

    protected void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    public static float getGravity() {
        return gravity;
    }



    @Override
    synchronized public void onDraw(Canvas canvas) {
        if ( this.gameStatus == "gamePlay" ) {
            this.drawGamePlay(canvas);
        }
        else {
            this.drawHome(canvas);
        }

    }

    public String getGameStatus() {
        return this.gameStatus;
    }

    public void setGameStatus(String status) {
        this.gameStatus = status;
    }

    private void drawHome(Canvas canvas) {
//        findViewById(R.id.play_again).setVisibility(View.VISIBLE);
//        findViewById(R.id.play_again).setEnabled(true);

    }

    public void resetGame() {
        this.score = 0;
        while (this.baskets.size() != 0) {
            this.baskets.remove(0);
        }
        this.addBaskets();
        this.addEggInitial();   // Need to initialize baskets before the egg.
    }


    private void drawGamePlay(Canvas canvas) {

        canvas.drawBitmap(this.egg.getBitmap(this.context), this.egg.getPosition().x, this.egg.getPosition().y, null);

        for(int i = 0; i < this.baskets.size(); i++) {
            Basket b = this.baskets.get(i);
            canvas.drawBitmap(b.getBitmap(this.context), b.getPosition().x, b.getPosition().y, null);
        }

        if ( ! this.eggInABasket()) {
            // The egg is free to move.
            // Update its y position.
            this.egg.updatePosition("y");
        }
        else if ((this.eggInABasket()) && (this.baskets.get(this.indexOfBasketHoldingEgg).getVelocity().x != 0.0F)){
            // The egg is supposed to move horizontally only if it's in a basket. So update x position only then.
            this.egg.updatePosition("x");
            this.egg.updatePosition("y");
        }


        for (int i = 0; i < this.getBaskets().size(); i++) {
            this.getBaskets().get(i).move();
        }

        this.detectBasketHittingWall();
        this.newScreen();
        this.gameOver();

        // Draw score.
        p.setColor(Color.rgb(50, 50, 50));
        p.setTextSize((float) (this.screenWidth * 0.1));
        PointF scorePosition = this.percentToAbsolute(45, 7);
        canvas.drawText(Integer.toString(this.score), scorePosition.x, scorePosition.y, p);

        // Draw the highest score.
        p.setColor(Color.rgb(50, 50, 50));
        p.setTextSize((float) (this.screenWidth * 0.1));
        PointF highestScorePosition = this.percentToAbsolute(5, 7);
        canvas.drawText(Integer.toString(this.highestScore), highestScorePosition.x, highestScorePosition.y, p);

        if (this.whereIsEgg != -1 && this.egg.getPosition().y < this.screenHeight) {
//            float basketX = this.baskets.get(this.whereIsEgg).getPosition().x + Basket.getWidth() / 2 - Egg.getWidth() / 2;
            float basketX = this.egg.getPosition().x;
            float basketY = this.baskets.get(this.whereIsEgg).getPosition().y - Egg.getWidth() / 5;


            this.egg.setPosition(new PointF(basketX, basketY));
        }
    }

    public Boolean eggInABasket() {
        PointF eggPosition = this.egg.getPosition();
        float eggX = eggPosition.x;
        float eggY = eggPosition.y;

        for (int i = 0; i < this.baskets.size(); i++) {
            Basket b = this.baskets.get(i);
            float basketX = b.getPosition().x;
            float basketY = b.getPosition().y;

            if (b.active() && (this.egg.getVelocity().y > 0) && (eggY < this.screenHeight)) {                                           // The basket must be active and the egg must be falling down.
                if ((eggX + Egg.getWidth() / 2 >= basketX + 0.25 * Basket.getWidth()) && ((eggX + Egg.getWidth() / 2) <= (basketX + 0.75 * Basket.getWidth()))) {    // Right x position.
                    if ( (eggY + Egg.getHeight() * 0.75 >= basketY) && (eggY + Egg.getHeight() <= basketY + Basket.getHeight())){                                // Right y position.

                        this.indexOfBasketHoldingEgg = i;

                        this.egg.setVelocity(new PointF(this.baskets.get(indexOfBasketHoldingEgg).getVelocity().x, this.baskets.get(indexOfBasketHoldingEgg).getVelocity().y));

                        if (this.whereIsEgg != indexOfBasketHoldingEgg) {
                            // Score for the current basket hasn't been updated yet.
                            this.score++;
                            this.whereIsEgg = indexOfBasketHoldingEgg;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void addEggInitial() {
        PointF firstBasketPosition = absoluteToPercent(this.baskets.get(0).getPosition().x, this.baskets.get(0).getPosition().y);
        PointF position = percentToAbsolute(firstBasketPosition.x, firstBasketPosition.y);
        float x = position.x  + Basket.getWidth() / 4;       // Basket is intentional here since we want the egg to be right in the middle of the basket.

//        float x = position.x + (float) 0.0 * Basket.getWidth() + Egg.getWidth() / 2;

        float y = position.y - Egg.getWidth() * 2;
//        float y = 0;

        this.egg = new Egg(new PointF(x, y), new PointF(0, 0), this.screenWidth, this.screenHeight);
    }

    private void addBaskets() {

        int maxX = 70 - Math.round(this.absoluteToPercent(Basket.getWidth(), 0).x); // We use these as percent values.
        int minX = 30;                                                                // We use these as percent values.

        float randomX = new Random().nextInt(maxX - minX + 1) + minX;

        PointF firstPosition = percentToAbsolute(randomX, 80F);
        PointF secondPosition = percentToAbsolute(randomX, 50F);
        PointF thirdPosition = percentToAbsolute(new Random().nextInt(maxX - minX + 1) + minX, 20F);
//        PointF firstPosition = percentToAbsolute(50F, 80F);
//        PointF secondPosition = percentToAbsolute(0F, 50F);
//        PointF thirdPosition = percentToAbsolute(90F, 20F);


        ArrayList<PointF> positions = new ArrayList<>(Arrays.asList(firstPosition, secondPosition, thirdPosition));
        //ArrayList<PointF> positions = new ArrayList<>(Arrays.asList(percentToAbsolute(50F, this.basketYPositions.get(0)), percentToAbsolute(50F, this.basketYPositions.get(1)), percentToAbsolute(50F, this.basketYPositions.get(2))));

        for (int i = 0; i < 3; i++) {
            PointF position = positions.get(i);

            PointF velocity = new PointF(new Random().nextInt(15 - 10) + 10, Basket.verticalVelocity);        // Decide this based on numberOfScreens (gameLevel)

            if (i > 1)
//            if (false)
                this.baskets.add(new Basket(position, velocity, this.screenWidth, this.screenHeight));
            else
                this.baskets.add(new Basket(position, new PointF(0, 0), this.screenWidth, this.screenHeight));
        }
    }

    private void newScreenUpdateBaskets() {
        if ( ! holdOnAddingBaskets) {
            PointF position = percentToAbsolute(50F, 0 - absoluteToPercent(0, this.baskets.get(0).getHeight()).y);
            PointF velocity = new PointF(new Random().nextInt(15 - 10) + 10, Basket.verticalVelocity);        // Decide this based on numberOfScreens (gameLevel)

//            PointF position = percentToAbsolute(50F, 10F - absoluteToPercent(0, this.baskets.get(0).getHeight()).y);
//            PointF velocity = new PointF(0F, 0F);

            this.baskets.add(new Basket(position, velocity, this.screenWidth, this.screenHeight));
        }
    }





    synchronized public ArrayList<Basket> getBaskets() {
        return this.baskets;
    }

    private void detectBasketHittingWall() {
        int maxX = this.screenWidth;

        for(int i = 0; i < this.baskets.size(); i++) {
            float basket_x = this.baskets.get(i).getPosition().x;

            if ((basket_x > maxX - Basket.getWidth()) || (basket_x < 0)) {
                this.baskets.get(i).reverseXVelocity();
            }
        }
    }

    private PointF percentToAbsolute(float x, float y) {
        return new PointF(0.01F * x * this.screenWidth, 0.01F * y * this.screenHeight);
    }

    private PointF absoluteToPercent(float x, float y) {
        return new PointF(100 * x / this.screenWidth, 100 * y / this.screenHeight);
    }



    public void moveEggFromBasket() {
        if (this.whereIsEgg != -1) {
//        if (true ) {

            // Move the egg up only if it's already in a basket.
            this.egg.moveVertically();

            while (this.eggInABasket()) {
                this.egg.updatePosition("y");
            }

            this.baskets.get(this.indexOfBasketHoldingEgg).setActive(false);

            this.whereIsEgg = -1;    // Marks that the egg is in the air.

        }
    }

    private void newScreen() {

        if (this.baskets.get(0).getPosition().y > this.screenHeight) {
            this.newScreenUpdateBaskets();
            holdOnAddingBaskets = true;
        }

        if (this.baskets.get(0).getPosition().y > 1.05 * this.screenHeight) {
            this.baskets.remove(0);

            if (this.whereIsEgg > 0) {
                this.whereIsEgg--;
                this.indexOfBasketHoldingEgg--;
            }
            holdOnAddingBaskets= false;
            // Because we're removing the 0th basket, we also need to update the egg's position in respect to the basket's index.
        }
    }

    private void gameOver() {
        if (this.egg.getPosition().y > 2 * this.screenHeight) {
            // Do stuff that's supposed to happen when game's over here!
            this.resetGame();
            this.gameStatus = "gameOver";
        }
    }

}