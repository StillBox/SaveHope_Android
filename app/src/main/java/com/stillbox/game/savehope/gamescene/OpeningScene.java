package com.stillbox.game.savehope.gamescene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;

import java.util.ArrayList;
import java.util.Random;

public class OpeningScene extends GameScene {

    private float spacing;
    private int blockCount_x, blockCount_y;

    private ArrayList<Block> blocks;
    private Meteor meteor;

    private int updateTime;

    private boolean bSceneOver;
    private int countDown;

    public OpeningScene() {

        int spacing_x = (int) screenW / 160;
        int spacing_y = (int) screenH / 90;
        spacing = Math.min(spacing_x, spacing_y);
        blockCount_x = (int) screenW / (int) spacing;
        blockCount_y = (int) screenH / (int) spacing;

        blocks = new ArrayList<>();
        int x, y;
        int[] blocks_x, blocks_y;
        //S
        x = blockCount_x / 2 - 28;
        y = blockCount_y / 2 + 1;
        blocks_x = MainView.resources.getIntArray(R.array.S_x);
        blocks_y = MainView.resources.getIntArray(R.array.S_y);
        for (int i = 0; i < blocks_x.length; i++)
            blocks.add(new Block(x + blocks_x[i], y + blocks_y[i]));
        //t
        x = blockCount_x / 2 - 19;
        blocks_x = MainView.resources.getIntArray(R.array.t_x);
        blocks_y = MainView.resources.getIntArray(R.array.t_y);
        for (int i = 0; i < blocks_x.length; i++)
            blocks.add(new Block(x + blocks_x[i], y + blocks_y[i]));
        //i
        x = blockCount_x / 2 - 12;
        blocks_x = MainView.resources.getIntArray(R.array.i_x);
        blocks_y = MainView.resources.getIntArray(R.array.i_y);
        for (int i = 0; i < blocks_x.length; i++)
            blocks.add(new Block(x + blocks_x[i], y + blocks_y[i]));
        //l
        x = blockCount_x / 2 - 7;
        blocks_x = MainView.resources.getIntArray(R.array.l_x);
        blocks_y = MainView.resources.getIntArray(R.array.l_y);
        for (int i = 0; i < blocks_x.length; i++)
            blocks.add(new Block(x + blocks_x[i], y + blocks_y[i]));
        //l
        x = blockCount_x / 2 - 2;
        for (int i = 0; i < blocks_x.length; i++)
            blocks.add(new Block(x + blocks_x[i], y + blocks_y[i]));
        //B
        x = blockCount_x / 2 + 6;
        blocks_x = MainView.resources.getIntArray(R.array.B_x);
        blocks_y = MainView.resources.getIntArray(R.array.B_y);
        for (int i = 0; i < blocks_x.length; i++)
            blocks.add(new Block(x + blocks_x[i], y + blocks_y[i]));
        //o
        x = blockCount_x / 2 + 15;
        blocks_x = MainView.resources.getIntArray(R.array.o_x);
        blocks_y = MainView.resources.getIntArray(R.array.o_y);
        for (int i = 0; i < blocks_x.length; i++)
            blocks.add(new Block(x + blocks_x[i], y + blocks_y[i]));
        //x
        x = blockCount_x / 2 + 23;
        blocks_x = MainView.resources.getIntArray(R.array.x_x);
        blocks_y = MainView.resources.getIntArray(R.array.x_y);
        for (int i = 0; i < blocks_x.length; i++)
            blocks.add(new Block(x + blocks_x[i], y + blocks_y[i]));

        meteor = new Meteor();
    }

    @Override
    public void init() {

        updateTime = 0;
        bSceneOver = false;
        countDown = 1024;
    }

    @Override
    public void draw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        if (updateTime < 1024) {
            int alpha = updateTime / 4;
            paint.setAlpha(alpha);
        }
        canvas.drawRect(0, 0, screenW, screenH, paint);
        meteor.draw(canvas);
        for (Block block : blocks) {
            block.draw(canvas);
        }
        if (bSceneOver) {
            paint.setColor(Color.BLACK);
            if (countDown > 0) {
                int alpha = (1024 - countDown) / 4;
                paint.setAlpha(alpha);
            }
            canvas.drawRect(0, 0, screenW, screenH, paint);
        }
    }

    @Override
    public void update(long elapsedTime) {

        updateTime += elapsedTime;
        meteor.update(elapsedTime);
        for (Block block : blocks) {
            block.update(elapsedTime);
        }

        if (updateTime > 6000) {
            bSceneOver = true;
            countDown = 0;
        }

        if (bSceneOver) {
            countDown -= elapsedTime;
            if (countDown <= 0)
                MainView.mainView.setScene(MainView.GAME_MENU);
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

        if (!bSceneOver && updateTime >= 1000 && event.getAction() == MotionEvent.ACTION_DOWN) {
            bSceneOver = true;
            countDown = 1024;
        }
    }

    private class Block {

        private static final int SHOW_TIME = 1024;
        private int x, y;
        private float offset;
        private int updateTime;
        private int activeTime;
        private boolean bShow;
        private boolean bActivate;

        Block(int x, int y) {
            this.x = x;
            this.y = y;
            updateTime = 0;
            activeTime = 0;
            bShow = false;
            bActivate = false;
        }

        void draw(Canvas canvas) {

            if (bShow) {
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(Color.BLACK);
                if (updateTime < SHOW_TIME / 4) {
                    int alpha = (updateTime + SHOW_TIME / 4) / 2;
                    paint.setAlpha(alpha);
                }
                canvas.drawRect(x * spacing + 1, y * spacing - offset + 1,
                        (x + 1) * spacing, (y + 1) * spacing - offset, paint);
            }
        }

        void update(long elapsedTime) {

            int delta = x - 2 * y -  (int) ((meteor.getX() - 2f * meteor.getY()) / spacing);

            if (!bShow) {

                if (!bActivate) {
                   if (delta <= 5)
                        bActivate = true;
                }
                if (bActivate) {
                    updateTime += elapsedTime;
                    activeTime += elapsedTime;
                    Random random = new Random();
                    while (updateTime >= 20 && !bShow) {
                        updateTime -= 20;
                        if (random.nextInt() % 8 == 0 || activeTime >= SHOW_TIME / 4) {
                            bShow = true;
                            updateTime = -SHOW_TIME / 4;
                        }
                    }
                }
            }

            if (bShow) {
                if (updateTime < SHOW_TIME)
                    updateTime += elapsedTime;
                if (updateTime > SHOW_TIME)
                    updateTime = SHOW_TIME;
                offset = spacing * (SHOW_TIME - updateTime) * updateTime / 100000f;
            }
        }
    }

    private class Meteor {

        private float x, y;
        private float scale;
        private float angle;
        private int updateTime;

        Meteor() {
            x = 0f;
            y = 44f * spacing;
            scale = 1f;
            angle = 0f;
            updateTime = -1000;
        }

        void draw(Canvas canvas) {

            Path path = new Path();
            path.moveTo(x + spacing * scale * (float) (Math.sqrt(2) * Math.cos(angle)), y + spacing * scale * (float) (Math.sqrt(2) * Math.sin(angle)));
            path.lineTo(x - spacing * scale * (float) (Math.sqrt(2) * Math.sin(angle)), y + spacing * scale * (float) (Math.sqrt(2) * Math.cos(angle)));
            path.lineTo(x - spacing * scale * (float) (Math.sqrt(2) * Math.cos(angle)), y - spacing * scale * (float) (Math.sqrt(2) * Math.sin(angle)));
            path.lineTo(x + spacing * scale * (float) (Math.sqrt(2) * Math.sin(angle)), y - spacing * scale * (float) (Math.sqrt(2) * Math.cos(angle)));
            path.close();

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            canvas.drawPath(path, paint);
        }

        void update(long elapsedTime) {

            updateTime += elapsedTime;
            angle = updateTime * 0.002f * (float) Math.PI;
            if (angle >= Math.PI /2) angle -= Math.PI / 2;

            if (updateTime <= 1500f) {
                x = spacing * (blockCount_x / 2 + 29) * updateTime / 1500f;
                y = spacing * (blockCount_y / 2 + 4);
            } else if (updateTime <= 1750f) {
                x = spacing * (blockCount_x / 2 + 29) + 10f * spacing * (float) Math.sin(Math.PI * 0.5f * (updateTime - 1500f) / 250f);
                y = spacing * (blockCount_y / 2 - 6) + 10f * spacing * (float) Math.cos(Math.PI * 0.5f * (updateTime - 1500f) / 250f);
            } else if (updateTime <= 2375f) {
                x = spacing * (blockCount_x / 2 + 14) + 25f * spacing * (float) Math.sin(Math.PI * 0.5f * ((updateTime - 1750f) / 625f + 1f));
                y = spacing * (blockCount_y / 2 - 6) + 25f * spacing * (float) Math.cos(Math.PI * 0.5f * ((updateTime - 1750f) / 625f + 1f));
            } else if (updateTime <= 3000f) {
                x = spacing * (blockCount_x / 2 + 14) + 25f * spacing * (float) Math.sin(Math.PI * 0.5f * ((updateTime - 2375f) / 625f + 2f));
                y = spacing * (blockCount_y / 2 - 6) + 25f * spacing * (float) Math.cos(Math.PI * 0.5f * ((updateTime - 2375f) / 625f + 2f));
            } else {
                x = spacing * (blockCount_x / 2 - 11);
                y = spacing * (blockCount_y / 2 - 6);
            }

            if (updateTime >= 4000f)
                scale = 1f + (float) Math.pow((updateTime - 4000f) / 100f, 2);
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }
}
