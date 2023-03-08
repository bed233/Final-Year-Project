package com.rhul.fyp.asecav;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsActivity;

public class AboutLibrariesActivity extends LibsActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setIntent(new LibsBuilder().withActivityTitle(getResources().getString(R.string.open_source_libraries)).withAboutIconShown(true).withLicenseShown(true).intent(this));
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
