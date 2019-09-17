package com.webianks.hatkemessenger.utils;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.widget.Toast;

import com.webianks.hatkemessenger.Contact;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class SimUtils {
    public int i = 0;

    public void diffusion(final List<Contact> contacts, final Context ctx, final String centerNum, final String smsText, final PendingIntent sentIntent, final PendingIntent deliveryIntent) {

        while (i < contacts.size()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (sendSMS(ctx, contacts.get(i).getNumber(), centerNum, smsText, sentIntent, deliveryIntent)) {

                        Log.e("diffusion", contacts.get(i).toString());

                    }
                    i++;
                }
            }).start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    public static boolean sendSMS(Context ctx, String toNum, String centerNum, final String smsText, final PendingIntent sentIntent, final PendingIntent deliveryIntent) {
        toNum = toNum.replace(" ", "");
        int simID = getIdsimSlot(ctx, toNum);
        String name;
        try {
            if (simID == 0) {
                name = Build.MODEL.equals("Philips T939") ? "isms0" : "isms";
            } else if (simID == 1) {
                name = "isms2";
            } else {
                throw new Exception("can not get service which for sim '" + simID + "', only 0,1 accepted as values");
            }
            Method method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
            method.setAccessible(true);
            Object param = method.invoke(null, name);

            method = Class.forName("com.android.internal.telephony.ISms$Stub").getDeclaredMethod("asInterface", IBinder.class);
            method.setAccessible(true);
            Object stubObj = method.invoke(null, param);
            Log.e("send msg", smsText);
            if (Build.VERSION.SDK_INT < 18) {
                method = stubObj.getClass().getMethod("sendText", String.class, String.class, String.class, PendingIntent.class, PendingIntent.class);
                method.invoke(stubObj, toNum, centerNum, smsText, sentIntent, deliveryIntent);
            } else {
                if (isDualSim(ctx)) {
                    SmsManager.getSmsManagerForSubscriptionId(simID + 1).sendTextMessage(toNum, null, smsText, sentIntent, deliveryIntent);
                } else {
                    SmsManager.getDefault().sendTextMessage(toNum, null, smsText, sentIntent, deliveryIntent);
                }
            }

            return true;
        } catch (ClassNotFoundException e) {
            Log.e("SimUtl", "ClassNotFoundException:" + e.getMessage());
            Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show();
        } catch (NoSuchMethodException e) {
            Log.e("SimUtl", "NoSuchMethodException:" + e.getMessage());
            Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show();
        } catch (InvocationTargetException e) {
            Log.e("SimUtl", "InvocationTargetException:" + e.getMessage());
            Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show();
        } catch (IllegalAccessException e) {
            Log.e("SimUtl", "IllegalAccessException:" + e.getMessage());
            Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("SimUtl", "Exception:" + e.getMessage());
            Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    public static int getIdsimSlot(Context context, String toNumber) {
        String operator = "";
        String prefix = toNumber.replace("+243", "0").substring(0, 3);

        switch (prefix) {
            case "099":
            case "097":
                operator = "Airtel";
                break;
            case "081":
            case "082":
                operator = "Vodacom";
                break;
            case "089":
            case "085":
            case "084":
                operator = "Orange RDC";
                break;
            case "090":
                operator = "Africell";
                break;
            default:
                Log.e("operator", "operateur inconnu" + prefix);
                break;
        }

        Log.e("OPERATEUR", operator + " " + prefix);
        /*if (Build.VERSION.SDK_INT >= 22) {
            if (SubscriptionManager.from(context).getActiveSubscriptionInfoForSimSlotIndex(0).getDisplayName().equals(operator)) {
                return 0;// ca veut dire le slot SIM1
            } else if (SubscriptionManager.from(context).getActiveSubscriptionInfoForSimSlotIndex(1).getDisplayName().equals(operator)) {
                return 1;// ca veut dire le slot SIM2
            } else {
                Log.e("operator dont exist", operator);
            }*/
        if (Build.VERSION.SDK_INT >= 22) {
            if (SubscriptionManager.from(context).getActiveSubscriptionInfoForSimSlotIndex(0).getDisplayName().equals(operator)){
                return 0;// ca veut dire le slot SIM1
            }else if (SubscriptionManager.from(context).getActiveSubscriptionInfoForSimSlotIndex(1).getDisplayName().equals(operator)){
                return 1;// ca veut dire le slot SIM2
            }else{
                Log.e("operator dont exist",operator);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                return preferences.getInt("InterconnectionSim",0);//si la sim n'est pas configuree on send avec sim1
            }
        } else {
            Log.e("dualsim", "faild");
        }
        return 0;
    }

    @SuppressLint("MissingPermission")
    public static boolean isDualSim(Context context) {
        if (Build.VERSION.SDK_INT >= 22) {
            return SubscriptionManager.from(context).getActiveSubscriptionInfoCount() > 1;
        } else {
            return false;
        }
    }
}
