package com.webianks.hatkemessenger.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.webianks.hatkemessenger.Contact;
import com.webianks.hatkemessenger.Diffusion;
import com.webianks.hatkemessenger.Ormlite;
import com.webianks.hatkemessenger.R;
import com.webianks.hatkemessenger.Smspg;
import com.webianks.hatkemessenger.adapters.DetailGroupeContactAdapter;
import com.webianks.hatkemessenger.adapters.DetailGroupeSmsAdapter;
import com.webianks.hatkemessenger.utils.SimUtils;

import java.util.ArrayList;
import java.util.List;

public class GroupeDetail extends AppCompatActivity {
    public int idGroupe;
    public int nbContact;
    public List<Diffusion> diffusions;
    public List<Contact> contacts = new ArrayList<Contact>();
    public List<Contact> contacts_pref_par_sms = new ArrayList<Contact>();
    public List<Contact> contacts_pref_par_mail = new ArrayList<Contact>();
    public List<Smspg> sms = new ArrayList<Smspg>();
    private RecyclerView recyclerViewContact;
    private RecyclerView recyclerViewSms;
    public ImageView sendBt;
    public EditText smsEdit;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupe_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        smsEdit = (EditText)findViewById(R.id.groupeMessage);
        sendBt = (ImageView)findViewById(R.id.groupebtSend);
        sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (smsEdit.getText().toString().length()==0){
                    smsEdit.setError("sms vide \uD83D\uDE02");
                }else{
                    Ormlite ormlite = new Ormlite(getApplicationContext());
                    if (ormlite.insertSmsGroupe(new Smspg(smsEdit.getText().toString(), false, idGroupe))){
                        Log.e("inserting","ok");
                        initRcvsms();
                        Toast.makeText(getApplicationContext(), "Envoi par SMS en cours...", Toast.LENGTH_SHORT).show();
                        new SimUtils().diffusion(contacts_pref_par_sms,getApplicationContext(), null,smsEdit.getText().toString(),null,null);
                        envoyerParMail(smsEdit.getText().toString());
                        smsEdit.setText("");
                    }else{
                        Log.e("inserting","faild");
                    }
                }
            }
        });


        Ormlite ormlite = new Ormlite(this);
        idGroupe = getIntent().getExtras().getInt("idGroupe");
        diffusions = ormlite.getDiffusionByGroupeId(idGroupe);
        nbContact = diffusions.size();
        int i = 0;
        while (i<nbContact){
            Contact cont = ormlite.getContactById(diffusions.get(i).getIdContact());
            contacts.add(cont);
            if(cont.isPar_sms()) {
                contacts_pref_par_sms.add(cont);
            } else if (cont.isPar_mail()){
                contacts_pref_par_mail.add(cont);
            }
            i++;
        }
        initRcvContact(nbContact,contacts);
        Log.e("e",contacts.toString());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void envoyerParMail(String message) {
        int t = this.contacts_pref_par_mail.size();
        String email_address[] = new String[t];
        for (int i = 0; i < t; i++) {
            Contact c = this.contacts_pref_par_mail.get(i);
            if(c.getEmail().contains("@")) {
                email_address[i] = c.getEmail();
            }
        }
        if(email_address.length != 0) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, email_address);
            intent.putExtra(Intent.EXTRA_SUBJECT, "ALLSMS");
            intent.putExtra(Intent.EXTRA_TEXT, message);
            this.startActivity(Intent.createChooser(intent, "ALLSMS Par Mail"));
        }
    }

    public void initRcvContact(int nbContact, List<Contact> contacts){
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),nbContact);
        recyclerViewContact = (RecyclerView)findViewById(R.id.contactrcv);
        recyclerViewContact.setHasFixedSize(true);
        recyclerViewContact.setLayoutManager(gridLayoutManager);
        DetailGroupeContactAdapter contactAdapter = new DetailGroupeContactAdapter(getApplicationContext(),contacts);
        recyclerViewContact.setAdapter(contactAdapter);
        initRcvsms();
    }
    public void initRcvsms(){
        Ormlite ormlite = new Ormlite(getApplicationContext());
        sms = ormlite.getSmspgByIdgroupe(idGroupe);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),1);
        recyclerViewSms = (RecyclerView)findViewById(R.id.smsrcvgroupe);
        recyclerViewSms.setHasFixedSize(true);
        recyclerViewSms.setLayoutManager(gridLayoutManager);
        DetailGroupeSmsAdapter smsAdapter = new DetailGroupeSmsAdapter(getApplicationContext(),sms);
        recyclerViewSms.setAdapter(smsAdapter);
    }

}
