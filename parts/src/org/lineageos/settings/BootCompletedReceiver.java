/*
 * Copyright (C) 2015 The CyanogenMod Project
 *               2017-2020 The LineageOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.lineageos.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.util.Log;
import android.os.IBinder;
import android.view.Display.HdrCapabilities;
import android.view.Display;

import org.lineageos.settings.camera.NfcCameraService;
import org.lineageos.settings.display.ColorService;
import org.lineageos.settings.dolby.DolbyUtils;
import org.lineageos.settings.doze.AodBrightnessService;
import org.lineageos.settings.doze.DozeUtils;
import org.lineageos.settings.doze.PocketService;
import org.lineageos.settings.refreshrate.RefreshUtils;
import org.lineageos.settings.thermal.ThermalUtils;

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final boolean DEBUG = false;
    private static final String TAG = "XiaomiParts-BCR";

    @Override
    public void onReceive(final Context context, Intent intent) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (DEBUG) Log.d(TAG, "Received boot completed intent");

        Log.i(TAG, "Boot completed");

        // Dolby Atmos
        DolbyUtils.getInstance(context);

        // Doze
        DozeUtils.checkDozeService(context);

        // Refresh Rate
        RefreshUtils.initialize(context);

        // Thermal Profiles
        ThermalUtils.startService(context);

        // Pocket
        PocketService.startService(context);

        // DisplayFeature
        ColorService.startService(context);

        // NFC
        NfcCameraService.startService(context);

        // AOD
        AodBrightnessService.startService(context);

        // Override HDR types
        final DisplayManager displayManager = context.getSystemService(DisplayManager.class);
        displayManager.overrideHdrTypes(Display.DEFAULT_DISPLAY, new int[]{
                HdrCapabilities.HDR_TYPE_DOLBY_VISION, HdrCapabilities.HDR_TYPE_HDR10,
                HdrCapabilities.HDR_TYPE_HLG, HdrCapabilities.HDR_TYPE_HDR10_PLUS});
    }
}
