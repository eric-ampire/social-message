package com.webianks.hatkemessenger.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;

import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.webianks.hatkemessenger.Contact;
import com.webianks.hatkemessenger.R;
import com.webianks.hatkemessenger.SMS;
import com.webianks.hatkemessenger.utils.ColorGeneratorModified;
import com.webianks.hatkemessenger.utils.Helpers;

import java.util.ArrayList;
import java.util.List;


public class AllConversationAdapter extends RecyclerView.Adapter<AllConversationAdapter.MyHolder> {

    private Context context;
    private List<SMS> data;
    List<Contact> contacts;

    private ItemCLickListener itemClickListener;
    ColorGeneratorModified generator = ColorGeneratorModified.MATERIAL;


    public AllConversationAdapter(Context context, List<SMS> data, List<Contact> contacts) {
        this.context = context;
        this.data = data;
        this.contacts = contacts;
    }

    @Override
    public AllConversationAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.single_sms_small_layout, parent, false);
        MyHolder myHolder = new MyHolder(view);
        return myHolder;
    }

    private String getNameByNumber(String number) {
        String name = null;
        for (Contact contact: contacts) {
            String numberFormatted = contact.getNumber().replace(" ", "");
            String newNumber = takeLast(numberFormatted, 9);

            if (number.contains(newNumber)) {
                name = contact.getNom();
                break;
            }
        }

        if (name == null) {
            return number;
        } else {
            return name;
        }
    }

    public static String takeLast(String value, int count) {
        if (value == null || value.trim().length() == 0) return "";
        if (count < 1) return "";

        if (value.length() > count) {
            return value.substring(value.length() - count);
        } else {
            return value;
        }
    }

    @Override
    public void onBindViewHolder(AllConversationAdapter.MyHolder holder, int position) {

        SMS sms = data.get(position);

        String name = getNameByNumber(sms.getAddress());


        holder.senderContact.setText(name);
        holder.message.setText(sms.getMsg());


        int color = generator.getColor(sms.getAddress());
        String firstChar = String.valueOf(name.charAt(0));
        TextDrawable drawable = TextDrawable.builder().buildRound(firstChar, color);

        if (sms.getAddress().equals(name)) {
            holder.senderImage.setImageResource(R.drawable.default_user);
        } else {
            holder.senderImage.setImageDrawable(drawable);
        }

        sms.setColor(color);


        if (sms.getReadState().equals("0")) {
            holder.newSmsIncator.setVisibility(View.GONE);
            holder.senderContact.setTypeface(holder.senderContact.getTypeface(), Typeface.BOLD);
            holder.message.setTypeface(holder.message.getTypeface(), Typeface.BOLD);
            holder.message.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.time.setTypeface(holder.time.getTypeface(), Typeface.BOLD);
            holder.time.setTextColor(ContextCompat.getColor(context, R.color.black));
        } else {
            holder.newSmsIncator.setVisibility(View.GONE);
            holder.senderContact.setTypeface(null, Typeface.NORMAL);
            holder.message.setTypeface(null, Typeface.NORMAL);
            holder.time.setTypeface(null, Typeface.NORMAL);

        }

        holder.time.setText(Helpers.getDate(sms.getTime()));
    }


    @Override
    public int getItemCount() {
        return (data == null) ? 0 : data.size();
    }


    public void setItemClickListener(ItemCLickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private ImageView senderImage;
        private TextView senderContact;
        private TextView message;
        private TextView time;
        private RelativeLayout mainLayout;
        private ImageView newSmsIncator;

        public MyHolder(View itemView) {
            super(itemView);
            senderImage = (ImageView) itemView.findViewById(R.id.smsImage);
            senderContact = (TextView) itemView.findViewById(R.id.smsSender);
            message = (TextView) itemView.findViewById(R.id.smsContent);
            time = (TextView) itemView.findViewById(R.id.time);
            mainLayout = (RelativeLayout) itemView.findViewById(R.id.small_layout_main);
            newSmsIncator = (ImageView) itemView.findViewById(R.id.newsms);
            mainLayout.setOnClickListener(this);
            mainLayout.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {

                data.get(getAdapterPosition()).setReadState("1");
                notifyItemChanged(getAdapterPosition());

                itemClickListener.itemClicked(data.get(getAdapterPosition()).getColor(),
                        senderContact.getText().toString(),
                        data.get(getAdapterPosition()).getId(),
                        data.get(getAdapterPosition()).getReadState());
            }

        }

        @Override
        public boolean onLongClick(View view) {

            String[] items = {"Delete"};

            ArrayAdapter<String> adapter = new ArrayAdapter<>(context
                    , android.R.layout.simple_list_item_1, android.R.id.text1, items);

            new AlertDialog.Builder(context)
                    .setAdapter(adapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            deleteDialog();
                        }
                    })
                    .show();

            return true;
        }

        private void deleteDialog() {

            AlertDialog.Builder alert = new AlertDialog.Builder(context);
            alert.setMessage("Are you sure you want to delete this message?");
            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    deleteSMS(data.get(getAdapterPosition()).getId(), getAdapterPosition());

                }

            });
            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            alert.create();
            alert.show();
        }
    }

    public void deleteSMS(long messageId, int position) {

        long affected = context.getContentResolver().delete(
                Uri.parse("content://sms/" + messageId), null, null);

        if (affected != 0) {

            data.remove(position);
            notifyItemRemoved(position);

        }

    }
}
