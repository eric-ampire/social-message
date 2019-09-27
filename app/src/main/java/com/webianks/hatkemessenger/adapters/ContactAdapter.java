package com.webianks.hatkemessenger.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.webianks.hatkemessenger.Contact;
import com.webianks.hatkemessenger.Diffusion;
import com.webianks.hatkemessenger.Groupe;
import com.webianks.hatkemessenger.Ormlite;
import com.webianks.hatkemessenger.R;
import com.webianks.hatkemessenger.utils.ColorGeneratorModified;

import java.util.List;

/**
 * Created by 4B6555S .
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private ColorGeneratorModified generator;
    private Context context;
    private String titreGroupe;
    private List<Contact> contacts;

    public ContactAdapter(Context context, List<Contact> contacts, String titreGroupe) {
        this.context = context;
        this.contacts = contacts;
        this.titreGroupe = titreGroupe;
        generator = ColorGeneratorModified.MATERIAL;
    }

    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cardviewcontact, parent, false);
        ContactAdapter.MyViewHolder myHolder = new ContactAdapter.MyViewHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(final ContactAdapter.MyViewHolder holder, final int position) {
        final Contact contactActuel = contacts.get(position);
        holder.nom.setText(contacts.get(position).getNom());
        holder.numero.setText(contacts.get(position).getNumber());
        holder.email.setText(contacts.get(position).getEmail());
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                holder.cover.setColorFilter(generator.getColor(contacts.get(position).getNom()));
                holder.ischeck.setBackgroundColor(generator.getColor(contacts.get(position + 1).getNom()));
                contactActuel.setPar_sms(holder.par_sms.isChecked());
                contactActuel.setPar_mail(holder.par_mail.isChecked());
                Ormlite ormlite = new Ormlite(context);
                if (!ormlite.isContactExist(contactActuel)) {
                    if (ormlite.insertContact(contactActuel)) {
                        if (ormlite.isGroupeExist(titreGroupe)) {
                            Log.e("Onlogclick", "groupe exist");
                            if (ormlite.insertDiffusion(new Diffusion(ormlite.getIdContact(contactActuel), ormlite.getIdGroupeByname(titreGroupe)))) {
                                Log.e("Onlogclick", "inserteGoupe ok");
                            } else {
                                Log.e("Onlogclick", "diffusion faild");
                            }
                        } else {
                            Log.e("Onlogclick", "nouveau groupe");
                            if (ormlite.insertGroupe(new Groupe(titreGroupe, 0))) {
                                if (ormlite.insertDiffusion(new Diffusion(ormlite.getIdContact(contactActuel), ormlite.getIdGroupeByname(titreGroupe)))) {
                                    Log.e("Onlogclick", "inserteGoupe ok");
                                } else {
                                    Log.e("Onlogclick", "diffusion faild");
                                }
                            } else {
                                Log.e("Onlogclick", "inserteGroupe faild");
                            }
                        }

                    } else {
                        Log.e("Onlogclick", "inserteContact faild");
                    }
                } else {
                    Log.e("Onlogclick", "contact exist");
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nom;
        private TextView numero;
        private TextView email;
        private LinearLayout card;
        private FrameLayout ischeck;
        private ImageView cover;
        private RadioButton par_sms;
        private RadioButton par_mail;

        public MyViewHolder(View itemView) {
            super(itemView);

            nom = (TextView) itemView.findViewById(R.id.nomcontact);
            numero = (TextView) itemView.findViewById(R.id.numcontact);
            email = (TextView) itemView.findViewById(R.id.emailContact);
            card = (LinearLayout) itemView.findViewById(R.id.cardcontact);
            cover = (ImageView) itemView.findViewById(R.id.covercontact);
            ischeck = (FrameLayout) itemView.findViewById(R.id.ischeck);

            par_sms = (RadioButton) itemView.findViewById(R.id.parSms);
            par_mail = (RadioButton) itemView.findViewById(R.id.parMail);
        }

    }

}
