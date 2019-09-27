package com.webianks.hatkemessenger.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class SmsOrMailDialog extends AppCompatDialogFragment {
    private SenderInterface sender_interface = null;

    public SmsOrMailDialog(SenderInterface sender_interface){
        this.sender_interface = sender_interface;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choiser le mode d'envoi")
                .setMessage("Envoyer par SMS ou par Mail")
                .setPositiveButton("SMS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sender_interface.sendSMS();
                    }
                })
                .setNegativeButton("MAIL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sender_interface.sendMAIL();
                    }
        });

        return builder.create();
    }


    public interface SenderInterface {
        public void sendSMS();
        public void sendMAIL();
    }
}
