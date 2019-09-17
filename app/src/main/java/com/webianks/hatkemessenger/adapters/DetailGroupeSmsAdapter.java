package com.webianks.hatkemessenger.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.webianks.hatkemessenger.R;
import com.webianks.hatkemessenger.Smspg;
import com.webianks.hatkemessenger.utils.ColorGeneratorModified;

import java.util.List;

public class DetailGroupeSmsAdapter extends RecyclerView.Adapter<DetailGroupeSmsAdapter.MyViewHolder> {

    private ColorGeneratorModified generator;
    private Context context;
    private List<Smspg> smspg;

    public DetailGroupeSmsAdapter(Context context, List<Smspg> smspg) {
        this.context = context;
        this.smspg = smspg;
        generator = ColorGeneratorModified.MATERIAL;
    }

    @Override
    public DetailGroupeSmsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cardviewgroupesms, parent, false);
        DetailGroupeSmsAdapter.MyViewHolder myHolder = new DetailGroupeSmsAdapter.MyViewHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(final DetailGroupeSmsAdapter.MyViewHolder holder, final int position) {
        final Smspg smsActuel = smspg.get(position);
        holder.sms.setText(smsActuel.getSms());
        holder.cover.setColorFilter(generator.getColor(smsActuel.getSms()));

    }

    @Override
    public int getItemCount() {
        return smspg.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView sms;
        private ImageView cover;

        public MyViewHolder(View itemView) {
            super(itemView);

            sms = (TextView) itemView.findViewById(R.id.message);
            cover = (ImageView) itemView.findViewById(R.id.smsImage);
        }

    }

}
