package com.aashishtripathee.anda;

/**
 * Created by aashish on 5/29/15.
 */
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.aashishtripathee.anda.Egg;
import com.aashishtripathee.anda.Brick;

public class GameBoard extends View {


    public static final float gravity = 6000.0F;
    public static final float drag = 0.01F;

    private Paint p;
    Context context;

    private Egg egg;
    private ArrayList<Brick> bricks = new ArrayList<Brick>();



    public GameBoard(Context context, AttributeSet aSet) {
        super(context, aSet);
        this.context = context;
        //it's best not to create any new objects in the on draw
        //initialize them as class variables here
        p = new Paint();

        int maxX = findViewById(R.id.the_canvas).getWidth();
        int maxY = findViewById(R.id.the_canvas).getHeight();

        Egg e = new Egg(new PointF(200, 250));
        this.egg = e;


    }

    public static float getGravity() {
        return gravity;
    }

    public static float getDrag() {
        return drag;
    }

    @Override
    synchronized public void onDraw(Canvas canvas) {

        for(int i = 0; i < this.bricks.size(); i++) {
            Brick b = this.bricks.get(i);
            canvas.drawBitmap(b.getBitmap(this.context), b.getPosition().x, b.getPosition().y, null);
        }

        canvas.drawBitmap(this.egg.getBitmap(this.context), this.egg.getPosition().x, this.egg.getPosition().y, null);

        if ((this.bricks.size() == 0)) {
            this.addRandomBricks(canvas.getWidth(), canvas.getHeight());
        }

        Boolean collision = this.collisionDetected(canvas.getWidth(), canvas.getHeight());
        if (collision) {
            this.bounceEgg();
        }

        this.egg.move();

        for (int i = 0; i < this.getBricks().size(); i++) {
            this.getBricks().get(i).move();
        }
    }



    private void addRandomBricks(int maxX, int maxY) {

        for (int i = 0; i < 3; i++) {
            Random r = new Random();

            int minX = 0;
            int minY = 0;

            float x = r.nextInt(maxX - minX + 1) + minX;
            float y = r.nextInt(maxY - minY + 1) + minY;

            PointF position = new PointF(x, y);
            PointF velocity = new PointF(800.0F, 0);

            this.bricks.add(new Brick(position, velocity));
        }
    }

    synchronized public Egg getEgg() {
        return this.egg;
    }

    synchronized public ArrayList<Brick> getBricks() {
        return this.bricks;
    }

    synchronized private Boolean collisionDetected(int maxX, int maxY) {
        float egg_x = this.egg.getPosition().x;
        float egg_y = this.egg.getPosition().y;

        for(int i = 0; i < this.bricks.size(); i++) {
            float brick_x = this.bricks.get(i).getPosition().x;
            float brick_y = this.bricks.get(i).getPosition().y;
            float brick_width = this.bricks.get(i).getWidth();

//            if ((egg_y <= brick_y) && ((egg_x > brick_x) && (egg_x < brick_x + brick_width))) {
//                return true;
//            }

            // For debugging purpose only, remove later.
            if (egg_y > maxY - 10) {
                return true;
            }
        }
        return false;
    }

    synchronized private void bounceEgg() {
        this.egg.reverseVelocity();
    }

}