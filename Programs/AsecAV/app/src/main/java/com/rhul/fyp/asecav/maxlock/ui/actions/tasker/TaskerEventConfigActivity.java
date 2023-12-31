/*
 * MaxLock, an Xposed applock module for Android
 * Copyright (C) 2014-2018  Max Rumpf alias Maxr1998
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.rhul.fyp.asecav.maxlock.ui.actions.tasker;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.rhul.fyp.asecav.R;
import com.rhul.fyp.asecav.maxlock.ui.actions.BundleScrubber;
import com.rhul.fyp.asecav.maxlock.util.Util;

import static com.rhul.fyp.asecav.maxlock.ui.actions.tasker.TaskerHelper.EVENT_TYPE_EXTRA_KEY;
import static com.rhul.fyp.asecav.maxlock.ui.actions.tasker.TaskerHelper.EVENT_UNLOCK_ATTEMPT;
import static com.rhul.fyp.asecav.maxlock.ui.actions.tasker.TaskerHelper.EVENT_UNLOCK_FAILED;
import static com.rhul.fyp.asecav.maxlock.ui.actions.tasker.TaskerHelper.EVENT_UNLOCK_SUCCESS;
import static com.twofortyfouram.locale.api.Intent.EXTRA_BUNDLE;
import static com.twofortyfouram.locale.api.Intent.EXTRA_STRING_BLURB;

import com.rhul.fyp.asecav.maxlock.ui.actions.BundleScrubber;

public class TaskerEventConfigActivity extends AppCompatActivity {

    private Intent result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Util.setTheme(this);
        super.onCreate(savedInstanceState);
        if (getCallingActivity() == null || BundleScrubber.scrub(getIntent())) {
            finish();
            return;
        }
        final Bundle extra = getIntent().getBundleExtra(EXTRA_BUNDLE);
        if (BundleScrubber.scrub(extra)) {
            finish();
            return;
        }

        int resultCode;
        String component = getIntent().getComponent().getClassName();
        switch (component.substring(component.lastIndexOf('.') + 1)) {
            case "EventUnlockAttempt":
                resultCode = EVENT_UNLOCK_ATTEMPT;
                break;
            case "EventUnlockSuccess":
                resultCode = EVENT_UNLOCK_SUCCESS;
                break;
            case "EventUnlockFailed":
                resultCode = EVENT_UNLOCK_FAILED;
                break;
            default:
                return;
        }

        Bundle resultBundle = new Bundle();
        resultBundle.putInt(EVENT_TYPE_EXTRA_KEY, resultCode);
        result = new Intent();
        result.putExtra(EXTRA_BUNDLE, resultBundle);
        result.putExtra(EXTRA_STRING_BLURB, getString(R.string.app_name));
        finish();
    }

    @Override
    public void finish() {
        if (result != null) {
            setResult(RESULT_OK, result);
        } else setResult(RESULT_CANCELED);
        super.finish();
    }
}