package com.app.graphrec.graphrec;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * An overlay view class to draw upon a surface view.
 * @author gustav
 */
public class SurfaceOverlay extends View {
    private Paint mPainter;
    private Paint mButtonInnerPainter;
    private Paint mButtonOuterPainter;

    /**
     * Constructor used for the benefit of android studio.
     * @param context The current context
     * @param attrs Attributes from xml tag
     */
    public SurfaceOverlay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    /**
     * Creates and sets up the painter objects
     */
    private void init() {
        mPainter = new Paint();
        mPainter.setStrokeWidth(20.0f);
        mPainter.setStyle(Paint.Style.FILL_AND_STROKE);

        mButtonInnerPainter = new Paint();
        mButtonInnerPainter.setStyle(Paint.Style.FILL);
        mButtonInnerPainter.setColor(Color.RED);
        mButtonInnerPainter.setStrokeWidth(25.0f);

        mButtonOuterPainter = new Paint();
        mButtonOuterPainter.setStyle(Paint.Style.STROKE);
        mButtonOuterPainter.setColor(Color.RED);
        mButtonOuterPainter.setStrokeWidth(25.0f);

    }

    /**
     * Draws the overlay.
     * @param canvas The drawing canvas
     */
    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth()/2;
        int height = getHeight();
        // we use this to fix the rows
        int pixelOffset = 10;

        float buttonOffset = 150.0f;
        float indicatorOffset = 200.0f;

        float leftCornerX = width-indicatorOffset;
        float leftCornerY = indicatorOffset;
        float indicatorHeight = width;
        float indicatorWidth = width;

        float points[] = {
                // p1(x,y) p2(x,y)
                leftCornerX-pixelOffset, leftCornerY, leftCornerX+indicatorWidth+pixelOffset, leftCornerY, // line 1
                leftCornerX+indicatorWidth, leftCornerY-pixelOffset, leftCornerX+indicatorWidth, leftCornerY+indicatorHeight+pixelOffset, // line 2
                leftCornerX+indicatorWidth-pixelOffset, leftCornerY+indicatorHeight, leftCornerX-pixelOffset, leftCornerY+indicatorHeight, // line 3
                leftCornerX, leftCornerY+indicatorHeight+pixelOffset, leftCornerX, leftCornerY-pixelOffset, // line 4
        };


        canvas.drawLines(points, mPainter);

        canvas.drawCircle(width, height-buttonOffset, 50.0f, mButtonInnerPainter);
        canvas.drawCircle(width, height-buttonOffset, 100.0f, mButtonOuterPainter);
    }

    /**
     * Checks whether the overlay button was clicked or not.
     * @param event The motion event
     * @return Whether the button was clicked or not
     */
    public boolean didButtonClick(MotionEvent event) {
        int width = getWidth()/2;
        int height = getHeight()-150;

        if (event.getAction() == MotionEvent.ACTION_UP) {
            performClick();
            if (event.getX() < width + 100.0f && event.getX() > width - 100.0f
                    && (event.getY() < height + 100.0f && event.getY() > height - 100.0f)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Ensures that auxiliary click methods are run.
     * @return True there was an assigned OnClickListener that was called, false otherwise is returned.
     */
    @Override
    public boolean performClick() {
        return true;
    }

}
