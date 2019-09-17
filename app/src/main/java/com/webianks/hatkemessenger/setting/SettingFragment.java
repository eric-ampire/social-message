package com.webianks.hatkemessenger.setting;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionManager;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.webianks.hatkemessenger.R;

import java.util.Objects;

public class SettingFragment extends PreferenceFragmentCompat implements PermissionListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences pref;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_general, rootKey);

        Dexter.withActivity(getActivity())
            .withPermission(Manifest.permission.READ_PHONE_STATE)
            .withListener(this)
            .check();

        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        pref.registerOnSharedPreferenceChangeListener(this);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (SubscriptionManager.from(getContext()).getActiveSubscriptionInfoCount() > 1) {
                setupDefaultConfig();
            } else {
                Toast.makeText(getContext(), "Impossible de trouver deux sim", Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @SuppressLint("MissingPermission")
    private void setupDefaultConfig() {


        SubscriptionManager subScriptionManager = SubscriptionManager.from(getContext());

        String firstSimName = subScriptionManager.getActiveSubscriptionInfoForSimSlotIndex(0).getDisplayName().toString();
        String secondSimName = subScriptionManager.getActiveSubscriptionInfoForSimSlotIndex(1).getDisplayName().toString();

        Objects.requireNonNull(findPreference(getString(R.string.key_sim1))).setTitle(firstSimName);
        Objects.requireNonNull(findPreference(getString(R.string.key_sim2))).setTitle(secondSimName);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pref.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {

    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Todo: Disable or Enable
    }
}
