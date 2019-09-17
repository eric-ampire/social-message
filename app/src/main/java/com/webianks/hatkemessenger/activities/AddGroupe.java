package com.webianks.hatkemessenger.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.webianks.hatkemessenger.Contact;
import com.webianks.hatkemessenger.R;
import com.webianks.hatkemessenger.adapters.ContactAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AddGroupe extends AppCompatActivity {
    public EditText titreGroupe;
    public Button terminer;
    public ImageView suiteContact;
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private ProgressBar progressBar2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_groupe);
        Toolbar toolbar = findViewById(R.id.toolbar);
        progressBar2 = findViewById(R.id.progressBar2);
        setSupportActionBar(toolbar);
        titreGroupe = (EditText) findViewById(R.id.titregroupe);
        terminer = (Button) findViewById(R.id.terminer);
        suiteContact = (ImageView) findViewById(R.id.imbtngroupe);
        suiteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titreGroupe.getText().toString().length() == 0) {
                    titreGroupe.setError("TAPER UN TITRE DU GROUPE DE DIFFUSION");
                } else {
                    initRcv();
                }
            }
        });

        terminer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(), Groupes.class));
            }
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initRcv() {
        recyclerView = findViewById(R.id.contactsrcv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        contactAdapter = new ContactAdapter(this, allContact(), titreGroupe.getText().toString());
        recyclerView.setAdapter(contactAdapter);
    }

    public List<Contact> allContact() {

        final List<Contact> contacts = new ArrayList<>();
        showProgressBar();

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                while (phones.moveToNext()) {
                    for (int i = 0; i < phones.getColumnCount(); i++) {

                        try {
                            Log.i(phones.getColumnName(i) + "", phones.getString(i));
                        } catch (Exception e) {
                            Log.i("ereur", i + "");
                        }

                    }

                    String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Log.i("num ", name);
                    Log.i("nom ", phoneNumber);
                    contacts.add(new Contact(name, phoneNumber, 0));
                }
                phones.close();
                Collections.sort(contacts);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgressBar();
                    }
                });
            }
        });

        return contacts;
    }

    @MainThread
    private void showProgressBar() {
        progressBar2.setVisibility(View.VISIBLE);
    }

    @MainThread
    private void hideProgressBar() {
        progressBar2.setVisibility(View.INVISIBLE);
    }
}
