package com.securewallet;

/**
 * Modèle représentant une carte bancaire.
 * Cette classe stocke les informations basiques d'une carte.
 */
public class Card {
    // Identifiant unique de la carte dans la base de données
    private final int id;
    // Nom du titulaire de la carte
    private final String holder;
    // Les 4 derniers chiffres du numéro de carte
    private final String lastFour;

    /**
     * Constructeur pour initialiser une instance de carte.
     *
     * @param id       L'identifiant de la carte
     * @param holder   Le nom du titulaire
     * @param lastFour Les 4 derniers chiffres du numéro de carte
     */
    public Card(int id, String holder, String lastFour) {
        this.id = id;
        this.holder = holder;
        this.lastFour = lastFour;
    }

    /**
     * Récupère l'identifiant unique de la carte.
     *
     * @return L'identifiant de la carte
     */
    public int getId() {
        return id;
    }

    /**
     * Récupère le nom du titulaire de la carte.
     *
     * @return Le nom du titulaire
     */
    public String getHolder() {
        return holder;
    }

    /**
     * Récupère les 4 derniers chiffres du numéro de carte.
     *
     * @return Les 4 derniers chiffres sous forme de chaîne
     */
    public String getLastFour() {
        return lastFour;
    }
}