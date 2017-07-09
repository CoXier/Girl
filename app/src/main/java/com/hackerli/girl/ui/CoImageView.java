package com.hackerli.girl.ui;

/**
 * Created by lijianxin on 2017/7/9.
 */

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


/**
 * An ImageView with random ratio for width and height.
 */
public class CoImageView extends AppCompatImageView {
    private static float[] sRatios = new float[10];

    static {
        for (int i = 0; i < 10; i++) {
            float ratio = (float) (Math.random() + 1);
            while (ratio > 1.6 || ratio < 1.1) {
                ratio = (float) (Math.random() + 1);
            }
            sRatios[i] = ratio;
        }
    }

    public CoImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int randomIndex = (int) (Math.random() * 10);
        int height = (int) (width * sRatios[randomIndex]);
        setMeasuredDimension(width, height);
    }
}
