package com.stillbox.game.savehope.gameobject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;

import java.util.ArrayList;

public class TextBox extends GameObject {

    private static int timePerChar = 60;

    private String text;
    private ArrayList<LineInfo> lineInfos;

    private int updateTime;
    private int updateRate;
    private int currentChar;
    private int currentLine;
    private boolean bIsTextOver;

    private float borderSize;
    private Border border;
    private RectF rectText;
    private int textSize;
    private int maxLines;

    public TextBox() {

        setX(MainView.w / 2);
        setY(MainView.h * 4 / 5);
        setW(MainView.w - 2 * Border.BORDER_SIZE * MainView.rate);
        setH(MainView.h * 2 / 5 - 2 * Border.BORDER_SIZE * MainView.rate);

        lineInfos = new ArrayList<>();
        updateTime = 0;
        updateRate = 1;
        currentChar = 0;
        currentLine = 0;
        bIsTextOver = false;

        borderSize = Border.BORDER_SIZE * MainView.rate;
        border = new Border(x, y, w, h, MainView.rate);
        rectText = new RectF(x - w / 2 + borderSize, y - h / 2 + borderSize * 2 / 3,
                x + w / 2 - borderSize, y + h / 2 - borderSize * 2 / 3);

        textSize = (int) (48f * MainView.rate);
        float maxHeight = rectText.height() - textSize;
        maxLines = 1;
        while (maxHeight >= 1.5f * textSize) {
            maxHeight -= 1.5f * textSize;
            maxLines++;
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        border.draw(canvas, paint);

        if (border.isReady()) {

            paint.setColor(Color.BLACK);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.LEFT);
            int begLine = Math.max(currentLine - maxLines + 1, 0);
            float text_x, text_y;
            for (int line = begLine; line < currentLine; line++) {
                text_x = rectText.left;
                text_y = rectText.top + (1f + 1.5f * (line - begLine)) * textSize;
                canvas.drawText(text, lineInfos.get(line).begChar, lineInfos.get(line).endChar, text_x, text_y, paint);
            }
            text_x = rectText.left;
            text_y = rectText.top + (1f + 1.5f * (currentLine - begLine)) * textSize;
            canvas.drawText(text, lineInfos.get(currentLine).begChar, currentChar, text_x, text_y, paint);

            if (bIsTextOver) {

                float offset = borderSize / 8 - borderSize / 4 * Math.abs((float) updateTime / timePerChar / 8);
                Path path = new Path();
                path.moveTo(rectText.right, rectText.bottom + offset);
                path.lineTo(rectText.right - borderSize / 4, rectText.bottom - borderSize / 4 + offset);
                path.lineTo(rectText.right + borderSize / 4, rectText.bottom - borderSize / 4 + offset);
                path.close();
                paint.setColor(Color.BLACK);
                canvas.drawPath(path, paint);
            }
        }
    }

    @Override
    public void update(int elapsedTime) {

        border.update(elapsedTime);
        if (border.isReady()) {
            if (!bIsTextOver) {
                updateTime += updateRate * elapsedTime;
                while (updateTime >= timePerChar) {
                    updateTime -= timePerChar;
                    currentChar++;
                }
                if (currentChar >= text.length()) {
                    currentChar = text.length();
                    bIsTextOver = true;
                } else if (currentChar >= lineInfos.get(currentLine).endChar) {
                    currentLine++;
                }
            } else {
                updateTime += elapsedTime;
                if (updateTime >= 8 * timePerChar) {
                    updateTime -= 16 * timePerChar;
                }
            }
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float touch_x = event.getX();
            float touch_y = event.getY();
            if (border.checkTouchPoint(touch_x, touch_y)) {
                updateRate = 10;
            }
        }
    }

    public void setText(String text) {

        this.text = text;
        updateTime = 0;
        currentChar = 0;
        lineInfos.clear();

        Paint paint = new Paint();
        paint.setTextSize(textSize);
        LineInfo lineInfo = new LineInfo();
        lineInfo.begChar = 0;
        lineInfo.endChar = 0;

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                lineInfo.endChar = i;
                lineInfos.add(lineInfo);
                lineInfo = new LineInfo();
                lineInfo.begChar = i + 1;
                lineInfo.endChar = i + 1;
            } else if (paint.measureText(text, lineInfo.begChar, i + 1) > rectText.width()) {
                lineInfo.endChar = i;
                lineInfos.add(lineInfo);
                lineInfo = new LineInfo();
                lineInfo.begChar = i;
                lineInfo.endChar = i;
            }
        }
        lineInfo.endChar = text.length();
        lineInfos.add(lineInfo);
    }

    public boolean isTextOver() {

        return bIsTextOver;
    }

    private boolean checkTouchPoint(float touch_x, float touch_y) {
        return border.checkTouchPoint(touch_x, touch_y);
    }

    private class LineInfo {

        int begChar;
        int endChar;
    }
}
