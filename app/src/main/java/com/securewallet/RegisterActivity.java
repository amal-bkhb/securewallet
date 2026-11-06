package com.securewallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        EditText etEmail = findViewById(R.id.etEmailRegister);
        EditText etPassword = findViewById(R.id.etPasswordRegister);
        Button btnCreerCompte = findViewById(R.id.btnCreerCompte);
        TextView tvDejaCompte = findViewById(R.id.tvDejaCompte);

        // Inscription
        btnCreerCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String mdp = etPassword.getText().toString().trim();

                if (email.isEmpty() || mdp.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Champs obligatoires", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mdp.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Mot de passe minimum 6 caractères", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, mdp)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Compte créé ✅", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Erreur : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        // Retour au login
        tvDejaCompte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}