package com.example.venteimmo.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.venteimmo.R;
import com.example.venteimmo.models.Annonce;
import com.example.venteimmo.utils.ItemClickListener;

import java.util.ArrayList;

public class AnnonceAdapter extends RecyclerView.Adapter<AnnonceAdapter.AnnonceViewHolder> {

    private Context context;
    private ArrayList<Annonce> proprietes;
    private ItemClickListener onItemClickListener;

    public AnnonceAdapter(Context context, ArrayList<Annonce> proprietes, ItemClickListener itemClickListener) {
        this.context = context;
        this.proprietes = proprietes;
        this.onItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public AnnonceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_annonce, viewGroup, false);

        return new AnnonceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnnonceViewHolder proprieteViewHolder, int i) {
        Annonce item = this.proprietes.get(i);

        proprieteViewHolder.tTitre.setText(item.getTitre());
        proprieteViewHolder.tDate.setText(item.getFormattedDate());
        proprieteViewHolder.tPrix.setText(item.getFormattedPrice());
        proprieteViewHolder.tVille.setText(item.getVille());
        Glide.with(this.context).load(item.getImages().get(0)).into(proprieteViewHolder.iImage);
    }

    @Override
    public int getItemCount() {
        return this.proprietes.size();
    }

    class AnnonceViewHolder extends RecyclerView.ViewHolder {
        ImageView iImage;
        TextView tTitre, tVille, tPrix, tDate;

        AnnonceViewHolder(@NonNull View itemView) {
            super(itemView);

            this.iImage = itemView.findViewById(R.id.proprieteImage);
            this.tTitre = itemView.findViewById(R.id.titre);
            this.tVille = itemView.findViewById(R.id.ville);
            this.tPrix = itemView.findViewById(R.id.prix);
            this.tDate = itemView.findViewById(R.id.date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(v, getAdapterPosition(), iImage);
                }
            });
        }
    }
}
