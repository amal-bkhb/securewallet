package com.securewallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper mDbHelper;
    private CardAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Afficher l'email de l'utilisateur connecté
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        Toast.makeText(this, "Bienvenue " + email, Toast.LENGTH_LONG).show();

        // Vérification de la sécurité de l'appareil
        String securityStatus = SecurityChecker.getSecurityStatus();
        Toast.makeText(this, securityStatus, Toast.LENGTH_LONG).show();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton fab = findViewById(R.id.fabAjouter);
        Button btnDeconnexion = findViewById(R.id.btnDeconnexion);

        DatabaseHelper db = new DatabaseHelper(this);
        this.mDbHelper = db;

        List<Card> cartes = db.getAllCards();
        CardAdapter adapter = new CardAdapter(cartes);
        this.mAdapter = adapter;

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddCardActivity.class));
            }
        });

        btnDeconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDbHelper != null && mAdapter != null) {
            mAdapter.updateData(mDbHelper.getAllCards());
        }
    }
}