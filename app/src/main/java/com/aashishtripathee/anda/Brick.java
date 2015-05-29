package com.aashishtripathee.anda;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import android.content.Context;

import com.aashishtripathee.anda.util.SystemUiHider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


/**
 * Created by aashish on 5/29/15.
 */
public class Brick {

    private PointF position;
    private String type;
    private PointF velocity;
    private Boolean stationary;


    public Brick(PointF position, String type, PointF velocity) {
        this.position = position;
        this.type = type;
        this.velocity = velocity;
    }

    public Brick(PointF position, PointF velocity) {
        this.position = position;
        this.type = this.getRandomType();   // Generate this randomly.
        this.velocity = velocity;

        this.stationary = (Math.random() < 0.1) ? true : false;
    }

    public String getType() {
        return this.type;
    }

    public PointF getPosition() {
        return this.position;
    }

    public PointF get_velocity() {
        return this.velocity;
    }

    public double getElasticity() {
        if (this.type == "orange") {
            return 1.0;
        }
        else if (this.type == "red") {
            return 0.8;
        }
        else if (this.type == "blue") {
            return 0.5;
        }
        else if (this.type == "green") {
            return 1.5;
        }

        return 1.0;
    }

    public Bitmap getBitmap(Context c) {
        int bitmap_id;

        if (this.type == "green") {
            bitmap_id = R.mipmap.green_brick;
        }
        else if (this.type == "red") {
            bitmap_id = R.mipmap.red_brick;
        }
        else if (this.type == "blue") {
            bitmap_id = R.mipmap.blue_brick;
        }
        else {
            bitmap_id = R.mipmap.orange_brick;
        }

        Bitmap b = BitmapFactory.decodeResource(c.getResources(), bitmap_id);

        return b;
    }

    private String getRandomType() {
        Random r = new Random();

        int index = r.nextInt(12 - 0 + 1) + 0;

        ArrayList<String> types = new ArrayList<String>(Arrays.asList("orange", "orange", "orange","orange","orange","orange","orange","red", "red","red", "green","green", "blue"));

        return types.get(index);

    }

    @Override
    public String toString() {
        return "Position: (" + this.position.x + ", " + this.position.y + "); Velocity: (" + this.velocity.x + ", " + this.velocity.y + "); Type: " + this.type + "\n";
    }

    public float getWidth() {
        return 50.0F;
    }

    public void move() {

        float time = 0.005F; // 25 frames per second.

//        System.out.println("At least I'm trying!");

        if ( ! this.stationary) {
            this.position.x += this.velocity.x * time;
            System.out.println(this.velocity.x + "\n");
        }
    }

}
