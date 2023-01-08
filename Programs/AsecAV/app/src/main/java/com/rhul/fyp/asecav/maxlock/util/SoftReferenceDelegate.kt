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

package com.rhul.fyp.asecav.maxlock.util

import java.lang.ref.SoftReference
import kotlin.reflect.KProperty

class SoftReferenceDelegate<T> {
    private var _value = SoftReference<T>(null)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? = _value.get()
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        _value = SoftReference<T>(value)
    }
}