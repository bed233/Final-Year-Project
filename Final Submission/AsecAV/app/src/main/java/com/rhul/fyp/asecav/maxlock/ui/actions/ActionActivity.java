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

package com.rhul.fyp.asecav.maxlock.ui.actions;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.rhul.fyp.asecav.maxlock.Common;
import com.rhul.fyp.asecav.maxlock.ui.actions.widget.MasterSwitchWidget;

import com.rhul.fyp.asecav.maxlock.ui.lockscreen.LockView;
import com.rhul.fyp.asecav.maxlock.util.AuthenticationSucceededListener;
import com.rhul.fyp.asecav.maxlock.util.MLPreferences;

public class ActionActivity extends AppCompatActivity implements AuthenticationSucceededListener {

    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefsApps = MLPreferences.getPrefsApps(this);
        mode = getIntent().getIntExtra(ActionsHelper.ACTION_EXTRA_KEY, ActionsHelper.ACTION_TOGGLE_MASTER_SWITCH);
        if ((mode == ActionsHelper.ACTION_MASTER_SWITCH_OFF || mode == ActionsHelper.ACTION_TOGGLE_MASTER_SWITCH) && prefsApps.getBoolean(Common.MASTER_SWITCH_ON, true)) {
            super.onCreate(savedInstanceState);
            setContentView(new LockView(this, Common.MASTER_SWITCH_ON, getClass().getName()), new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        } else {
            ActionsHelper.callAction(mode, this);
            fireIntentAndFinish();
            super.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onAuthenticationSucceeded() {
        ActionsHelper.callAction(mode, this);
        fireIntentAndFinish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        fireIntentAndFinish();
    }

    private void fireIntentAndFinish() {
        // Update MasterSwitch Widget if existent
        switch (mode) {
            case ActionsHelper.ACTION_TOGGLE_MASTER_SWITCH:
            case ActionsHelper.ACTION_MASTER_SWITCH_ON:
            case ActionsHelper.ACTION_MASTER_SWITCH_OFF:
                Intent intent = new Intent(this, MasterSwitchWidget.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), MasterSwitchWidget.class));
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                sendBroadcast(intent);
        }
        finish();
    }
}