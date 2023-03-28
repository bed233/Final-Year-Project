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

package com.rhul.fyp.asecav.maxlock.ui.settings_new.screens

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.rhul.fyp.asecav.R
import com.rhul.fyp.asecav.maxlock.ui.settings.applist.AppListAdapter
import com.rhul.fyp.asecav.maxlock.ui.settings.applist.AppListModel
import com.rhul.fyp.asecav.maxlock.util.GenericEventLiveData

class AppListScreen(activity: ComponentActivity) : CustomScreen() {
    override val viewModel: AppListModel by activity.viewModels()

    override val titleRes = R.string.pref_screen_apps
    override val hasOptionsMenu = true

    override val adapter: AppListAdapter
        get() = viewModel.adapter

    override val progressLiveData: GenericEventLiveData<Any?>?
        get() = if (!viewModel.loaded.get()) viewModel.appsLoadedListener else null
}