package com.hackerli.retrofit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/3/25.
 */
public class RatioImageview extends ImageView {
    private int originalWidth;
    private int originalHeight;

    public RatioImageview(Context context) {
        super(context);
    }

    public RatioImageview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RatioImageview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOriginalSize(int originalWidth, int originalHeight) {
        this.originalWidth = originalWidth;
        this.originalHeight = originalHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (originalWidth > 0 && originalHeight > 0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = MeasureSpec.getSize(heightMeasureSpec);
            float ratio = (float)originalWidth / (float)originalHeight;
            if (width>0){
                height = (int) (width / ratio);
            }
            setMeasuredDimension(width,height);

        } else
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
