package com.stillbox.game.savehope.gameobject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;

import java.util.ArrayList;

public class TextBox extends GameObject {

    private static int defaultTextSize;
    private static int timePerChar = 60;

    private String text;
    private ArrayList<LineInfo> lineInfos;

    private int updateTime;
    private float updateRate;
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
        updateRate = 1f;
        currentChar = 0;
        currentLine = 0;
        bIsTextOver = false;

        borderSize = Border.BORDER_SIZE * MainView.rate;
        border = new Border(x, y, w, h, MainView.rate);
        rectText = new RectF(x - w / 2 + borderSize, y - h / 2 + borderSize * 2 / 3,
                x + w / 2 - borderSize, y + h / 2 - borderSize * 2 / 3);

        defaultTextSize = (int) (48f * MainView.rate);
        setTextSize(defaultTextSize);
    }

    @Override
    public void onDestroy() {

        border.onDestroy();
        lineInfos.clear();
        lineInfos = null;
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
            try {
                canvas.drawText(text, lineInfos.get(currentLine).begChar, currentChar, text_x, text_y, paint);
            } catch (IndexOutOfBoundsException e) {
                Log.e("TextIndex", text + " Line " + currentLine + " info size " + lineInfos.size() + " current char " + currentChar);
            }


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
                    if (currentChar >= text.length()) {
                        currentChar = text.length();
                        bIsTextOver = true;
                    } else if (currentChar >= lineInfos.get(currentLine).endChar) {
                        currentLine++;
                        currentChar = lineInfos.get(currentLine).begChar;
                    }
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

    }

    public void setText(String text) {

        this.text = text;
        setTextSize(defaultTextSize);

        updateTime = 0;
        updateRate = 1f;
        currentChar = 0;
        currentLine = 0;
        bIsTextOver = false;

        lineInfos.clear();
        lineInfos = new ArrayList<>();

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

    public void setText(String text, int textSize) {

        this.text = text;
        setTextSize(textSize);

        updateTime = 0;
        updateRate = 1f;
        currentChar = 0;
        currentLine = 0;
        bIsTextOver = false;

        lineInfos.clear();
        lineInfos = new ArrayList<>();

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

    public void setTextSize(int textSize) {

        if (this.textSize == textSize)
            return;

        this.textSize = textSize;

        float maxHeight = rectText.height() - textSize;
        maxLines = 1;
        while (maxHeight >= 1.5f * textSize) {
            maxHeight -= 1.5f * textSize;
            maxLines++;
        }
    }

    public void accelerate(float rate) {

        updateRate = rate;
    }

    public void open(Border.OnOpenedListener listener) {

        if (border.isClosed()) {
            border.open(listener);
        }
    }

    public void close(Border.OnClosedListener listener) {

        if (border.isReady()) {
            border.close(listener);
        }
    }

    public boolean isTextOver() {

        return bIsTextOver;
    }

    public boolean checkTouchPoint(float touch_x, float touch_y) {
        return border.checkTouchPoint(touch_x, touch_y);
    }

    private class LineInfo {

        int begChar;
        int endChar;
    }
}
