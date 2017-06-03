package com.willy.ratingbar;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

/**
 * Created by willy on 2017/5/10.
 */
interface SimpleRatingBar {

    void setNumStars(int numStars);

    int getNumStars();

    void setRating(float rating);

    float getRating();

    void setStarPadding(int ratingPadding);

    int getStarPadding();


    void setEmptyDrawable(Drawable drawable);

    void setEmptyDrawableRes(@DrawableRes int res);

    void setFilledDrawable(Drawable drawable);

    void setFilledDrawableRes(@DrawableRes int res);


}
