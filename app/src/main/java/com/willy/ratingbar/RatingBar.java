package com.willy.ratingbar;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by willy on 2017/5/5.
 */

public class RatingBar extends android.support.v7.widget.AppCompatRatingBar {

    public RatingBar(Context context) {
        super(context);
        init();
    }

    public RatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setStepSize(1);
    }
}
