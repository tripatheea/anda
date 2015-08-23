package com.aashishtripathee.anda;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.graphics.PointF;

/**
 * Created by aashish on 5/29/15.
 */
public class Egg {
    private PointF position;
    private PointF velocity; // 1500.0F
    private static int width;   // 125
    private static int height;

    public Egg(PointF position, int screenWidth, int screenHeight) {
        this.position = position;
        this.velocity = new PointF(0.0F, 0.0F);


        this.width = (int) (0.07 * screenWidth);
        this.height = (int) (0.07 * screenHeight);
    }

    public Egg(PointF position, PointF velocity, int screenWidth, int screenHeight) {
        this.position = position;
        this.velocity = velocity;

        this.width = (int) (0.07 * screenWidth);
        this.height = (int) (0.07 * screenHeight);
    }

    public PointF getPosition() {
        return this.position;
    }

    public PointF getVelocity() {
        return this.velocity;
    }

    public Bitmap getBitmap(Context c) {
        Bitmap natural_size_bitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.egg);
        return Bitmap.createScaledBitmap(natural_size_bitmap, this.width, this.height, true);
    }

    public void moveVertically() {
        this.velocity.y = (float) (- this.height * 0.5);
    }

    public void setPosition(PointF newPosition) {
        this.position = newPosition;
    }

    public void updatePosition(String direction) {

        float delta_T = 1; //0.005F // 25 frames per second.

        if (direction == "y") {
            float new_y = this.position.y + this.velocity.y * delta_T;
            this.position = new PointF(position.x, new_y);
            this.velocity.y = this.velocity.y + GameBoard.getGravity() * delta_T;
        }
        else if (direction == "x") {
            float new_x = this.position.x + this.velocity.x * delta_T;
            this.position = new PointF(new_x, this.position.y);
        }

        // THis is as the baskets keep moving down.
        //this.position.y += 5 * delta_T;

    }

    synchronized public void setVelocity(PointF vel) {

        this.velocity = vel;
    }

    synchronized  public void updatePosition(PointF pos) {

        this.position = pos;
    }

    @Override
    public String toString() {
        return "Position: (" + this.position.x + ", " + this.position.y + "); Velocity: (" + this.velocity + "\n";
    }

    public void reverseVelocity(String direction) {
        if (direction == "x") {
            this.velocity.x = 0 - this.velocity.x;
        }
        else {
            this.velocity.y = 0 - this.velocity.y;
        }
    }

    public static float getWidth() {
        return width;
    }

    public static float getHeight() { return height; }

    public void moveDownWithBasket(Basket b) {
        this.position.y = b.getPosition().y;
    }
}
