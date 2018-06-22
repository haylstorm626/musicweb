package com.capstone.musicweb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import java.util.ArrayList;

import static android.R.attr.scaleX;
import static android.R.attr.translationZ;
import static android.view.MotionEvent.INVALID_POINTER_ID;


public class WebView extends View {

    public interface IMenuListener {
        void onMenuClick(MenuCircle item);
    }

    public static class MenuCircle {
        private int x, y;
        public int id;
        public String text;

    }

    private Paint firstPaint;
    private Paint secondPaint;
    private TextPaint textPaint;
    private int radius;
    private int radius2;
    private int centerX;
    private int centerY;

    private int menuInnerPadding = 40;
    private double startAngle = -Math.PI / 2f;
    private double startAngle2 = -Math.PI / 1.1f;
    private double startAngle3 = -Math.PI / .085f;
    private ArrayList<MenuCircle> elements;
    private IMenuListener listener;

    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;

    public void setListener(IMenuListener listener) {
        this.listener = listener;
    }

    public void clear() {
        elements.clear();
        listener = null;
    }

    public WebView(Context context) {
        super(context);
        init(context);
    }

    public WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        elements = new ArrayList<>();
        //mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public void addMenuItem(String text, int id) {
        MenuCircle item = new MenuCircle();
        item.id = id;  //element position
        item.text = text;  //element name (artist name)
        elements.add(item);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        //circle border paint
        firstPaint = new Paint();
        firstPaint.setColor(Color.DKGRAY);
        firstPaint.setStyle(Paint.Style.STROKE);
        firstPaint.setStrokeWidth(6);

        //circle fill paint & connecting line paint
        secondPaint = new Paint();
        secondPaint.setColor(Color.LTGRAY);
        secondPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        secondPaint.setStrokeWidth(4);

        //artist name paint
        textPaint = new TextPaint();
        textPaint.setColor(Color.DKGRAY);
        textPaint.setFakeBoldText(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (elements.size() != 0) {
            ugh(canvas);
        }

    }

    double j, k, l, m;

    public void ugh(Canvas canvas) {
        double L; //level num
        int cnt = 0;    //cnt = current element position
        centerX = canvas.getWidth() / 2;
        centerY = canvas.getHeight() / 2;
        textPaint.setTextSize(30);
        radius = 125;
        //draw center circle & text
        canvas.drawCircle(centerX, centerY, radius, secondPaint);
        canvas.drawCircle(centerX, centerY, radius, firstPaint);
        //draw artist names
        StaticLayout sl = new StaticLayout(elements.get(cnt).text, textPaint, radius * 2, Layout.Alignment.ALIGN_CENTER, 1, 1, false);

        canvas.save();
        canvas.translate(centerX - radius, centerY - radius / 6);
        sl.draw(canvas);
        canvas.restore();
        cnt++;
        j=0; k=0; l=0; m=0;
        for (int i = 0; i < 5; i++) {
            L = 1; //LEVEL 1
            radius2 = 200;
            centerX = canvas.getWidth() / 2;
            centerY = canvas.getHeight() / 2;
            boop(canvas, cnt, L);
            centerX = elements.get(cnt).x;
            centerY = elements.get(cnt).y;
            int cX = centerX;
            int cY = centerY;
            cnt++;
            for (int j = 0; j < 3; j++) {
                L = 2; //LEVEL 2
                radius2 = 175;
                centerX = cX;
                centerY = cY;
                boop(canvas, cnt, L);
                centerX = elements.get(cnt).x;
                centerY = elements.get(cnt).y;
                cnt++;
                for (int k = 0; k < 3; k++) {
                    L = 3; //LEVEL 3
                    radius2 = 75;
                    textPaint.setTextSize(radius2 / 2);
                    boop(canvas, cnt, L);
                    cnt++;
                }
            }
        }//why are you like this
    }

    public void boop(Canvas canvas, int i, double L) {
        double angle;


        if (i == 0) {
            angle = startAngle;
        } else {
            if (L == 1) {                  //generate angle on level 1
                angle = startAngle + (i * ((2 * Math.PI) / 66));
                radius = 100;
                textPaint.setTextSize(radius / 4);
            } else if (L == 2) {             //generate angle on Level 2
                angle = startAngle2 + ((i - j) * ((3 * Math.PI) / 56));
                if (k == 2) {
                    k = 0;
                    j = j + 5.5;
                } else {
                    k++;
                }
                radius = 88;
                textPaint.setTextSize(radius / 4);
            } else {                      //generate angle on Level 3
                angle = startAngle3 + ((i - m) * ((4 * Math.PI) / 16));
                if (l == 2 && k == 0) {
                    l = 0;
                    m = m + 5.6;
                } else if (l == 2) {
                    l = 0;
                    m = m + 2.9;
                } else {
                    l++;
                }
                radius = 63;
                textPaint.setTextSize(radius / 3);
            }
        }

        //getting X and Y position of current element
        elements.get(i).x = (int) (centerX + Math.cos(angle) * (radius2 + menuInnerPadding + radius2));
        elements.get(i).y = (int) (centerY + Math.sin(angle) * (radius2 + menuInnerPadding + radius2));

        //draw connecting lines between circles
        canvas.drawLine(centerX+ (float)Math.cos(angle) * 50, centerY+ (float)Math.sin(angle) * 50, elements.get(i).x, elements.get(i).y, secondPaint);

        //draw circles with border
        canvas.drawCircle(elements.get(i).x, elements.get(i).y, radius, secondPaint);
        canvas.drawCircle(elements.get(i).x, elements.get(i).y, radius, firstPaint);

        //draw artist names
        StaticLayout s2 = new StaticLayout(elements.get(i).text, textPaint, radius * 2, Layout.Alignment.ALIGN_CENTER, 1, 1, false);

        canvas.save();
        canvas.translate(elements.get(i).x - radius, elements.get(i).y - radius / 6);
        s2.draw(canvas);
        canvas.restore();
    }

    private float mPosX =0;
    private float mPosY =0;

    private float mLastTouchX;
    private float mLastTouchY;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //mScaleDetector.onTouchEvent(event);

        int action = event.getAction();
        switch(action & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN: {

                final float x = event.getX();
                final float y = event.getY();

                mLastTouchX = x;
                mLastTouchY = y;

                mActivePointerId = event.getPointerId(0);



                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = event.findPointerIndex(mActivePointerId);

                final float x = event.getX(pointerIndex);
                final float y = event.getY(pointerIndex);

                //if (!mScaleDetector.isInProgress()) {
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    mPosX += dx;
                    mPosY += dy;

                    this.setTranslationX(mPosX);
                    this.setTranslationY(mPosY);

                    invalidate();
                //}

                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                for(MenuCircle mc : elements){
                    double distance =  Math.hypot(event.getX()-mc.x,event.getY()-mc.y);
                    if(distance<= radius){
                        //touched
                        if(listener!=null)
                            listener.onMenuClick(mc);
                        return true;
                    }
                }
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (action & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = event.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = event.getX(newPointerIndex);
                    mLastTouchY = event.getY(newPointerIndex);
                    mActivePointerId = event.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //getting "web view" size based on screen size
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentWidth*4, parentHeight*3);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

   /* private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            setScaleX(mScaleFactor);
            setScaleY(mScaleFactor);

            invalidate();
            return true;
        }
    }*/



}