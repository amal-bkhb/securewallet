package com.securewallet;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<Card> cartes;

    public CardAdapter(List<Card> cartes) {
        this.cartes = cartes;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Card card = cartes.get(position);
        holder.tvNomTitulaire.setText(card.getHolder());
        holder.tvNumeroMasque.setText("**** " + card.getLastFour());

        // Clic sur une carte → ouvrir CardDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), CardDetailActivity.class);
            intent.putExtra("card_id", card.getId());
            intent.putExtra("card_holder", card.getHolder());
            intent.putExtra("card_last_four", card.getLastFour());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return cartes != null ? cartes.size() : 0;
    }

    public void updateData(List<Card> nouvelles) {
        this.cartes = nouvelles;
        notifyDataSetChanged();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        final TextView tvNomTitulaire;
        final TextView tvNumeroMasque;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNomTitulaire = itemView.findViewById(R.id.tvNomTitulaire);
            tvNumeroMasque = itemView.findViewById(R.id.tvNumeroMasque);
        }
    }
}