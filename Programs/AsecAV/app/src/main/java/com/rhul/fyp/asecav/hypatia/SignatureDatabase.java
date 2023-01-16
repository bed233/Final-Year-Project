/*
Hypatia: A realtime malware scanner for Android
Copyright (c) 2017-2018 Divested Computing Group

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/
package com.rhul.fyp.asecav.hypatia;

/**
 * Used to create Signature Databses for multiple signature types by creating multiple instances of this class.
 */
class SignatureDatabase {

    private final String baseURL;
    private final String name;

    public SignatureDatabase(String baseURL, String name) {
        this.baseURL = baseURL;
        this.name = name;
    }

    public final String getBaseUrl() {
        return baseURL;
    }

    public final String getName() {
        return name;
    }

    public final String getUrl() { return baseURL + name; }

}