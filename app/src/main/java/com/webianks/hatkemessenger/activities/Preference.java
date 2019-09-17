package com.webianks.hatkemessenger.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.telephony.SubscriptionManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.webianks.hatkemessenger.R;

public class Preference extends AppCompatActivity {

    public Switch swsim1,swsim2;
    public TextView ops1, ops2,numsim1,numsim2;
    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        numsim1 = (TextView)findViewById(R.id.numsim1);
        numsim2 = (TextView)findViewById(R.id.numsim2);
        ops1 = (TextView)findViewById(R.id.ops1);
        ops2 = (TextView)findViewById(R.id.ops2);
        swsim1 = (Switch)findViewById(R.id.swsim1);
        swsim2 = (Switch)findViewById(R.id.swsim2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 ) {
            ops1.setText(SubscriptionManager.from(this).getActiveSubscriptionInfoForSimSlotIndex(0).getDisplayName());
            numsim1.setText(SubscriptionManager.from(this).getActiveSubscriptionInfoForSimSlotIndex(0).getNumber());

            if (isDualSim(this)){
                ops2.setText(SubscriptionManager.from(this).getActiveSubscriptionInfoForSimSlotIndex(1).getDisplayName());
                numsim2.setText(SubscriptionManager.from(this).getActiveSubscriptionInfoForSimSlotIndex(1).getNumber());
            }else {
                ops2.setText("Sim absent");
                numsim2.setText("");
            }
        }
        swsim1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor edit_pref=preferences.edit();
                    edit_pref.putInt("InterconnectionSim",0 );
                    edit_pref.apply();
                    swsim2.setChecked(false);
                }

            }
        });

        swsim2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor edit_pref=preferences.edit();
                    edit_pref.putInt("InterconnectionSim",1 );
                    edit_pref.apply();
                    swsim1.setChecked(false);
                }

            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @SuppressLint("MissingPermission")
    public boolean isDualSim(Context context){
        if(Build.VERSION.SDK_INT>=22){
            return SubscriptionManager.from(context).getActiveSubscriptionInfoCount()>1;
        }else{
            return false;
        }
    }

}
