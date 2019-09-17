package com.webianks.hatkemessenger.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.webianks.hatkemessenger.Groupe;
import com.webianks.hatkemessenger.R;
import com.webianks.hatkemessenger.utils.ColorGeneratorModified;

import java.util.List;
import java.util.Random;

/**
 * Created by 4B6555S .
 */

public class GroupesAdapter extends RecyclerView.Adapter<GroupesAdapter.MyViewHolder> {

    private ColorGeneratorModified generator = ColorGeneratorModified.MATERIAL;
    private Context context;
    private List<Groupe> groupes;
    private OnItemClickListener monItemClick;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        monItemClick = listener;
    }

    public GroupesAdapter(Context context, List<Groupe> groupes) {
        this.context = context;
        this.groupes = groupes;
    }

    @Override
    public GroupesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cardviewgroups, parent, false);
        GroupesAdapter.MyViewHolder myHolder = new GroupesAdapter.MyViewHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(GroupesAdapter.MyViewHolder holder, int position) {

        holder.titreGroupe.setText(groupes.get(position).getNom());

        TextDrawable textDrawable = TextDrawable.builder()
            .beginConfig()
            .textColor(Color.WHITE)
            .toUpperCase()
            .fontSize(30)
            .endConfig()
            .buildRound(groupes.get(position).getNom().substring(0, 1), generator.getColor(groupes.get(position).getNom()));

        holder.icon.setImageDrawable(textDrawable);
    }


    @Override
    public int getItemCount() {
        return groupes.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView diffusion;
        private TextView titreGroupe;
        private ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);

//            diffusion = (TextView) itemView.findViewById(R.id.diffusion);
            titreGroupe = itemView.findViewById(R.id.nomgroupe);
            icon = itemView.findViewById(R.id.icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                if (monItemClick != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        monItemClick.onItemClick(position);
                    }
                }
                }
            });
        }
    }
}
