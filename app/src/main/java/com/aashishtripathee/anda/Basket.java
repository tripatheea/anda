package com.aashishtripathee.anda;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import android.content.Context;

import android.view.View;

import com.aashishtripathee.anda.util.SystemUiHider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 * Created by aashish on 5/29/15.
 */
public class Basket {

    private PointF position;
    private PointF velocity;
    private Boolean active = true;     // Only active baskets can hold an egg. Once an egg jumps out of a basket, it becomes inactive.

    private static int width; // 200
    private static int height; // 133

//    public final static float verticalVelocity = 1F;
    public final static float verticalVelocity = 2F;

    public Basket(PointF position, PointF velocity, int screenWidth, int screenHeight) {
        this.position = position;
        this.velocity = velocity;

        this.width = (int) (screenWidth * 0.20);
        this.height = (int) (screenHeight * 0.08);
    }


    public PointF getPosition() {

        return this.position;
    }

    public PointF getVelocity() {
        return this.velocity;
    }


    public Bitmap getBitmap(Context c) {
        Bitmap natural_size_bitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.basket);
        return Bitmap.createScaledBitmap(natural_size_bitmap, this.width, this.height, true);
    }

    public Boolean active() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "Position: (" + this.position.x + ", " + this.position.y + "); Velocity: (" + this.velocity + "\n";
    }

    public static float getWidth() {
        return width;
    }

    public static float getHeight() {
        return height;
    }

    public void move() {

        float delta_T = 1; // 50 frames per second.

        this.position.x += this.velocity.x * delta_T;

        // Let the y velocity depend on the level the player is on.
        this.position.y += verticalVelocity * delta_T;

    }

    public void reverseXVelocity() {
        this.velocity.x = 0 - this.velocity.x;

    }




}
