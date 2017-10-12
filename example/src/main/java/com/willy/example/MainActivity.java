package com.willy.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.willy.ratingbar.BaseRatingBar;
import com.willy.ratingbar.RotationRatingBar;
import com.willy.ratingbar.ScaleRatingBar;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "SimpleRatingBar";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final BaseRatingBar baseRatingBar = (BaseRatingBar) findViewById(R.id.baseratingbar_main);
        final ScaleRatingBar scaleRatingBar = (ScaleRatingBar) findViewById(R.id.scaleRatingBar);
        final RotationRatingBar rotationRatingBar = (RotationRatingBar) findViewById(R.id.rotationratingbar_main);

        baseRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating) {
                Log.d(TAG, "BaseRatingBar onRatingChange: " + rating);
            }
        });

        scaleRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating) {
                Log.d(TAG, "ScaleRatingBar onRatingChange: " + rating);
            }
        });

        rotationRatingBar.setOnRatingChangeListener(new BaseRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(BaseRatingBar ratingBar, float rating) {
                Log.d(TAG, "RotationRatingBar onRatingChange: " + rating);
            }
        });

        Button addRatingButton = (Button) findViewById(R.id.button_main_add_rating);
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
