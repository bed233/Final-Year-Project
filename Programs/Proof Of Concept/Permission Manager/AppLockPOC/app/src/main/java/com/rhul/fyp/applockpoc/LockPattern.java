package com.rhul.fyp.applockpoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.rhul.fyp.applockpoc.model.pattern;
import com.shuhart.stepview.StepView;

import java.util.List;

public class LockPattern extends AppCompatActivity {
    StepView stepView;
    LinearLayout linearLayout;
    RelativeLayout relativeLayout;
    pattern patternState;
    String userPattern;
    TextView currentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_pattern);

        stepView = findViewById(R.id.stepView);
        linearLayout = findViewById(R.id.topBar);
        relativeLayout = findViewById(R.id.main_lock_pattern_layout);
        patternState = new pattern(this);
        currentState = findViewById(R.id.currentState);
        currentState.setText(patternState.firstPatternInput);
        if (patternState.getPatternKey() == null) {
            linearLayout.setVisibility(View.GONE);
            stepView.setVisibility(View.VISIBLE);
            stepView.setStepsNumber(2);
            stepView.go(0, true);
        } else {
            linearLayout.setVisibility(View.VISIBLE);
            stepView.setVisibility(View.GONE);
            int BackgroundColour = ResourcesCompat.getColor(getResources(), R.color.blue, null);
            relativeLayout.setBackgroundColor(BackgroundColour);
            currentState.setTextColor(Color.WHITE);
        }
        setUpPatternListener();


    }

    private void setUpPatternListener() {
        final PatternLockView patternLockView = findViewById(R.id.patternView);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            /**
             * Fired when the pattern drawing has just started
             */
            @Override
            public void onStarted() {

            }

            /**
             * Fired when the pattern is still being drawn and progressed to one
             * more {@link PatternLockView.Dot}
             *
             * @param progressPattern
             */
            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            /**
             * Fired when the user has completed drawing the pattern and has
             * moved their finger away from the view
             *
             * @param pattern
             */
            @Override
            public void onComplete(List<PatternLockView.Dot> pattern) {
                String stringPattern = PatternLockUtils.patternToString(patternLockView, pattern);
                if (stringPattern.length() < 4) {
                    currentState.setText(patternState.patternFailed);
                    patternLockView.clearPattern();
                    return;
                }
                if (patternState.getPatternKey() == null) {
                    if (patternState.firstAttempt) {
                        userPattern = stringPattern;
                        patternState.setFirstAttempt(false);
                        currentState.setText(patternState.confirmPattern);
                        stepView.go(1, true);
                    } else {
                        if (userPattern.equals(stringPattern)) {
                            patternState.setPatternKey(stringPattern);
                            currentState.setText(patternState.patternSet);
                            stepView.done(true);
                            goToMainActivity();


                        }else{
                            if (patternState.patternIsCorrect(userPattern)){
                                currentState.setText(patternState.patternSet);
                                goToMainActivity();
                            }else{
                                currentState.setText(patternState.patternIncorrect);
                            }
                            patternLockView.clearPattern();
                        }

                    }
                }
            }

            /**
             * Fired when the patten has been cleared from the view
             */
            @Override
            public void onCleared() {

            }
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(LockPattern.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(patternState.getPatternKey() == null && !patternState.firstAttempt){
            stepView.go(0, true);
            patternState.setFirstAttempt(true);
            currentState.setText(patternState.firstPatternInput);
        }else{
            finish();
            super.onBackPressed();
        }
    }
}