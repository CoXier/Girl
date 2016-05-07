package com.hackerli.retrofit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * The code segment is I learn from Meizhi which was created by drakeet
 * Meizhi is under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
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
