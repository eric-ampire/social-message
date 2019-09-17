package com.webianks.hatkemessenger.adapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.webianks.hatkemessenger.Contact;
import com.webianks.hatkemessenger.R;
import com.webianks.hatkemessenger.utils.ColorGeneratorModified;

import java.util.List;

/**
     * Created by 4B6555S .
     */

    public class DetailGroupeContactAdapter extends RecyclerView.Adapter<DetailGroupeContactAdapter.MyViewHolder> {

        private ColorGeneratorModified generator;
        private Context context;
        private List<Contact> contacts;
        public DetailGroupeContactAdapter(Context context, List<Contact> contacts) {
            this.context = context;
            this.contacts = contacts;
            generator = ColorGeneratorModified.MATERIAL;
        }

        @Override
        public DetailGroupeContactAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.cardviewgroupecontact, parent, false);
            DetailGroupeContactAdapter.MyViewHolder myHolder = new DetailGroupeContactAdapter.MyViewHolder(view);
            return myHolder;
        }

        @Override
        public void onBindViewHolder(final DetailGroupeContactAdapter.MyViewHolder holder, final int position) {
            final Contact contactActuel = contacts.get(position);
            holder.nom.setText(contactActuel.getNom().length()>13?contactActuel.getNom().substring(0,13)+"...":contactActuel.getNom());
            holder.numero.setText(contactActuel.getNumber());
            holder.cover.setColorFilter(generator.getColor(contactActuel));

        }
        @Override
        public int getItemCount() {
            return contacts.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {

            private TextView nom;
            private TextView numero;
            private ImageView cover;

            public MyViewHolder(View itemView) {
                 super(itemView);

                 nom = (TextView) itemView.findViewById(R.id.namecontact);
                 numero = (TextView) itemView.findViewById(R.id.contactnumber);
                 cover = (ImageView)itemView.findViewById(R.id.covercontactgroupe);
            }

        }

}
