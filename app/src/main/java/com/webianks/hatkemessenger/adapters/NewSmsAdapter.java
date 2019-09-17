package com.webianks.hatkemessenger.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.webianks.hatkemessenger.R;
import com.webianks.hatkemessenger.Smspp;
import com.webianks.hatkemessenger.utils.ColorGeneratorModified;

import java.util.List;

public class NewSmsAdapter extends RecyclerView.Adapter<com.webianks.hatkemessenger.adapters.NewSmsAdapter.MyViewHolder> {

    private ColorGeneratorModified generator;
    private Context context;
    private List<Smspp> sms;
    private int color;

    public NewSmsAdapter(Context context, List<Smspp> sms, int color) {

        this.context = context;
        this.sms = sms;
        this.color = color;

        if (color == 0)
            generator = ColorGeneratorModified.MATERIAL;
    }

    @Override
    public com.webianks.hatkemessenger.adapters.NewSmsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.single_sms_detailed, parent, false);
        com.webianks.hatkemessenger.adapters.NewSmsAdapter.MyViewHolder myHolder = new com.webianks.hatkemessenger.adapters.NewSmsAdapter.MyViewHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(com.webianks.hatkemessenger.adapters.NewSmsAdapter.MyViewHolder holder, int position) {

        holder.message.setText(sms.get(position).getSms());
        String name = sms.get(position).getReceiverNumber();

        if (color == 0) {
            if (generator != null)
                color = generator.getColor(name);
        }
        holder.image.setColorFilter(color);


    }

    @Override
    public int getItemCount() {
        return sms.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView message;
        private ImageView image;
        private TextView time;

        public MyViewHolder(View itemView) {
            super(itemView);

            message = (TextView) itemView.findViewById(R.id.message);
            image = (ImageView) itemView.findViewById(R.id.smsImage);
            time = (TextView) itemView.findViewById(R.id.time);

        }

    }

}
