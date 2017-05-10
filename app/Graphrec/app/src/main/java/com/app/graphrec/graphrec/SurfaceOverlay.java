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

        // not pixel perfect and probably not portable, fix please?
        float points[] = {
                // p1(x,y) p2(x,y)
                100, 100, width+10, 100, // line 1
                width, 100, width-10, height-100, // line 2
                width, height-100, 100-10, height-100, // line 3
                100, height-100, 100+10, 100, // line 4
        };

        canvas.drawLines(points, mPainter);

        canvas.drawCircle(1500.0f, 500.0f, 50.0f, mButtonInnerPainter);
        canvas.drawCircle(1500.0f, 500.0f, 100.0f, mButtonOuterPainter);
    }

    /**
     * Checks whether the overlay button was clicked or not.
     * @param event The motion event
     * @return Whether the button was clicked or not
     */
    public boolean didButtonClick(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            performClick();
            if (event.getX() < 1500.0f + 100.0f && event.getX() > 1500.0f - 100.0f
                    && (event.getY() < 500.0f + 100.0f && event.getY() > 500.0f - 100.0f)) {
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
