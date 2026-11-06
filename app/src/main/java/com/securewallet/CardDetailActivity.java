package com.securewallet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CardDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        // Récupération des données envoyées depuis MainActivity
        int cardId = getIntent().getIntExtra("card_id", -1);
        String holder = getIntent().getStringExtra("card_holder");
        String lastFour = getIntent().getStringExtra("card_last_four");

        // Affichage des détails
        TextView tvHolder = findViewById(R.id.tvDetailHolder);
        TextView tvNumero = findViewById(R.id.tvDetailNumero);
        Button btnSupprimer = findViewById(R.id.btnSupprimer);

        tvHolder.setText("Titulaire : " + holder);
        tvNumero.setText("Numéro : **** **** **** " + lastFour);

        // Suppression de la carte
        btnSupprimer.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(this);
            db.deleteCard(cardId);
            Toast.makeText(this, "Carte supprimée", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}