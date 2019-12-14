package com.willy.example;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.RotationRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

public class DemoFragment
        extends Fragment {

    public static final String TAG = "SimpleRatingBar";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_demo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final BaseRatingBar baseRatingBar = view.findViewById(R.id.baseratingbar_main);
        final ScaleRatingBar scaleRatingBar = view.findViewById(R.id.scaleRatingBar);
        final RotationRatingBar rotationRatingBar = view.findViewById(R.id.rotationratingbar_main);
        baseRatingBar.setClearRatingEnabled(false);
        baseRatingBar.setOnRatingChangeListener((ratingBar, rating, fromUser) -> Log
                .d(TAG, "BaseRatingBar onRatingChange: " + rating));

        scaleRatingBar.setOnRatingChangeListener((ratingBar, rating, fromUser) -> Log
                .d(TAG, "ScaleRatingBar onRatingChange: " + rating));

        rotationRatingBar.setOnRatingChangeListener((ratingBar, rating, fromUser) -> Log
                .d(TAG, "RotationRatingBar onRatingChange: " + rating));

        Button addRatingButton = view.findViewById(R.id.button_main_add_rating);
        addRatingButton.setOnClickListener(v -> {
            float currentRating = baseRatingBar.getRating();
            baseRatingBar.setRating(currentRating + 0.25f);

            currentRating = scaleRatingBar.getRating();
            scaleRatingBar.setRating(currentRating + 0.25f);

            currentRating = rotationRatingBar.getRating();
            rotationRatingBar.setRating(currentRating + 0.25f);
        });
    }
}
