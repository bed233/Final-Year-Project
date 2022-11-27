package com.rhul.fyp.applockpoc.model;

import android.content.Context;

import io.paperdb.Paper;

public class Pattern {
    private String patternKey = "Pattern Key";
    public String patternSet = "Pattern Set";
    public String confirmPattern = "Draw the Pattern again to confirm";
    public String patternIncorrect = "Please Try Again!";
    public String firstPatternInput = "Draw an unlock pattern to unlock app";
    public String patternFailed = "Pattern must connect at least 4 dots";
    public Boolean firstAttempt = true;

    public String getPatternKey() {
        return Paper.book().read(patternKey);
    }

    public void setPatternKey(String pattern) {
        Paper.book().write(patternKey, pattern);
    }

    public Boolean getFirstAttempt() {
        return firstAttempt;
    }

    public void setFirstAttempt(Boolean firstAttempt) {
        this.firstAttempt = firstAttempt;
    }

    public boolean patternIsCorrect(String pattern){
        return pattern.equals(getPatternKey());
    }

    public Pattern(Context context){
        Paper.init(context);

    }
}
