package com.willy.example;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.willy.ratingbar.ScaleRatingBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.layout);

        final ScaleRatingBar ratingBarXML = (ScaleRatingBar) findViewById(R.id.simpleRatingBar);

        final ScaleRatingBar ratingBarCode = new ScaleRatingBar(this);
        ratingBarCode.setNumStars(8);
        ratingBarCode.setRating(2);
//        constraintLayout.addView(ratingBarCode);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int currentRating = ratingBarXML.getRating();
//                ratingBarXML.setRating(currentRating + 1);
//
//                ratingBarCode.setNumStars(3);
                ratingBarXML.setNumStars(ratingBarXML.getNumStars() - 1);
//                ratingBarXML.setStarPadding(5);
            }
        });
    }
}
