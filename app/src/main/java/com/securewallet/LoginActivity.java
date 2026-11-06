package com.securewallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Si déjà connecté → aller directement à MainActivity
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        EditText etEmail = findViewById(R.id.etEmail);
        EditText etMotDePasse = findViewById(R.id.etMotDePasse);
        Button btnConnecter = findViewById(R.id.btnConnecter);
        Button btnInscrire = findViewById(R.id.btnInscrire);

        // Connexion
        btnConnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String mdp = etMotDePasse.getText().toString().trim();

                if (email.isEmpty() || mdp.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Champs obligatoires", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, mdp)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Connexion réussie ✅", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Identifiants incorrects ❌", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // Inscription
        // Inscription → aller vers RegisterActivity
        btnInscrire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}