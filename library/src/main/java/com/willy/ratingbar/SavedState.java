package com.willy.ratingbar;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * Created by willy on 2017/11/16.
 */

class SavedState extends View.BaseSavedState {

    private float rating;

    /**
     * Constructor called from {@link BaseRatingBar#onSaveInstanceState()}
     */
    SavedState(Parcelable superState) {
        super(superState);
    }

    /**
     * Constructor called from {@link #CREATOR}
     */
    private SavedState(Parcel in) {
        super(in);
        rating = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeFloat(rating);
    }

    public static final Parcelable.Creator<SavedState> CREATOR
            = new Parcelable.Creator<SavedState>() {
        public SavedState createFromParcel(Parcel in) {
            return new SavedState(in);
        }

        public SavedState[] newArray(int size) {
            return new SavedState[size];
        }
    };

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
