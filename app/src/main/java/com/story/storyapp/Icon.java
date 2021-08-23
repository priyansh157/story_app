package com.story.storyapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

@SuppressLint("AppCompatCustomView")
public class Icon extends ImageView {
    public Icon(Context context) {
        super(context);
    }

    public Icon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Icon(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Icon(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width=getMeasuredWidth();
        int height=getMeasuredHeight();

        if(width>height)
            setMeasuredDimension(height,height);
        else
            setMeasuredDimension(width,width);
    }
}
