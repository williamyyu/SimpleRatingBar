package com.willy.example;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.RotationRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

public class DemoFragment extends Fragment {

    public static final String TAG = "SimpleRatingBar";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_demo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final BaseRatingBar baseRatingBar = (BaseRatingBar) view.findViewById(R.id.baseratingbar_main);
        final ScaleRatingBar scaleRatingBar = (ScaleRatingBar) view.findViewById(R.id.scaleRatingBar);
        final RotationRatingBar rotationRatingBar = (RotationRatingBar) view.findViewById(R.id.rotationratingbar_main);
        baseRatingBar.setClearRatingEnabled(false);
        baseRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                Log.d(TAG, "BaseRatingBar onRatingChange: " + rating);
            }
        });

        scaleRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                Log.d(TAG, "ScaleRatingBar onRatingChange: " + rating);
            }
        });

        rotationRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating, boolean fromUser) {
                Log.d(TAG, "RotationRatingBar onRatingChange: " + rating);
            }
        });

        Button addRatingButton = (Button) view.findViewById(R.id.button_main_add_rating);
        addRatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float currentRating = baseRatingBar.getRating();
                baseRatingBar.setRating(currentRating + 0.25f);

                currentRating = scaleRatingBar.getRating();
                scaleRatingBar.setRating(currentRating + 0.25f);

                currentRating = rotationRatingBar.getRating();
                rotationRatingBar.setRating(currentRating + 0.25f);
            }
        });
    }
}
