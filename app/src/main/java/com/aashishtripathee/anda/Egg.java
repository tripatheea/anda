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
    private float velocity = 1500.0F;

    public Egg(PointF position) {
        this.position = position;
    }

    public PointF getPosition() {
        return this.position;
    }

    public float getVelocity() {
        return this.velocity;
    }

    public Bitmap getBitmap(Context c) {
        return BitmapFactory.decodeResource(c.getResources(), R.mipmap.egg);
    }

    public void move() {
        float time = 0.005F; // 25 frames per second.
        float new_y = this.position.y + this.velocity * time;

        this.position = new PointF(this.position.x, new_y);

        this.velocity = this.velocity + GameBoard.getGravity() * time;
        this.velocity = (this.velocity > 0) ? (this.velocity - GameBoard.getDrag()*this.getVelocity()) : (this.velocity + GameBoard.getDrag()*this.getVelocity());

//        System.out.println(this.velocity + "\n");
    }

    @Override
    public String toString() {
        return "Position: (" + this.position.x + ", " + this.position.y + "); Velocity: (" + this.velocity + "\n";
    }

    public void reverseVelocity() {
        this.velocity = 0 - this.velocity;
    }
}
