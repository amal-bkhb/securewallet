package com.securewallet;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
/**
 * Activité permettant d'ajouter une nouvelle carte bancaire.
 * Activité permettant d'ajouter une nouvelle carte bancaire à la base de données.
 */
public class AddCardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 1. Appel du constructeur parent
        super.onCreate(savedInstanceState);

        // 2. Définition du layout XML (doit être fait avant findViewById)
        // 2. Définition du fichier de mise en page (layout)
        setContentView(R.layout.activity_add_card);

        // 3. Liaison avec le champ de saisie du nom du titulaire
        EditText etNom = findViewById(R.id.etNomTitulaire);

        // 4. Liaison avec le champ de saisie du numéro de carte
        EditText etNumero = findViewById(R.id.etNumero);

        // 5. Liaison avec le bouton de sauvegarde
        Button btnSauvegarder = findViewById(R.id.btnSauvegarder);

        // 6. Initialisation de l'helper de base de données
        // 6. Initialisation de la base de données locale
        DatabaseHelper db = new DatabaseHelper(this);

        // 7. Définition de l'action du clic sur le bouton de sauvegarde
        // 7. Définition de l'écouteur de clic pour le bouton de sauvegarde
        btnSauvegarder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lecture et nettoyage des entrées de l'utilisateur
                // Lecture et nettoyage des entrées textuelles
                String nom = etNom.getText().toString().trim();
                String numero = etNumero.getText().toString().trim();
                // Validation : vérifie si l'un des champs est vide
                // Validation : si l'un des deux champs obligatoires est vide
                if (nom.isEmpty() || numero.isEmpty()) {
                    Toast.makeText(AddCardActivity.this, "Champs obligatoires", Toast.LENGTH_SHORT).show();
                }
                // Validation : vérifie que le numéro contient exactement 16 chiffres
                // Validation : vérification de la longueur exacte du numéro (16 chiffres requis)
                else if (numero.length() != 16) {
                    Toast.makeText(AddCardActivity.this, "Le numéro doit contenir 16 chiffres", Toast.LENGTH_SHORT).show();
                }
                // Si tout est valide, sauvegarde de la carte
                // Enregistrement de la carte si les entrées sont valides
                else {
                    // Extraction des 4 derniers chiffres
                    // Extraction des 4 derniers chiffres (les index 12 à 15 de la chaîne de longueur 16)
                    String lastFour = numero.substring(12);

                    // Enregistrement dans la base de données SQLite
                    // Sauvegarde dans la base de données SQLite
                    db.saveCard(nom, lastFour);

                    // Message de confirmation et fermeture de l'activité
                    // Notification utilisateur
                    Toast.makeText(AddCardActivity.this, "Carte ajoutée avec succès", Toast.LENGTH_SHORT).show();

                    // Fermeture de l'activité actuelle et retour automatique à l'activité appelante (MainActivity)
                    finish();
                }
            }
        });
    }
}
