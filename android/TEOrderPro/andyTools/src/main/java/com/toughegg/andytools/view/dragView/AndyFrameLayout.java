package com.toughegg.andytools.view.dragView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;


public class AndyFrameLayout extends FrameLayout
{
    private AndyDragView dl;

    public AndyFrameLayout(Context context) {
        super(context);
    }

    public AndyFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AndyFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setDragLayout(AndyDragView dl) {
        this.dl = dl;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (dl.getStatus() != AndyDragView.Status.Close) {
            return true;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (dl.getStatus() != AndyDragView.Status.Close) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                dl.close();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

}
