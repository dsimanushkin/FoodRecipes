package com.devlab74.foodrecipes;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.devlab74.foodrecipes.util.HorizontalDottedProgress;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {

        ConstraintLayout constraintLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = constraintLayout.findViewById(R.id.activity_content);

        getLayoutInflater().inflate(layoutResID, frameLayout, true);

        super.setContentView(constraintLayout);
    }
}
