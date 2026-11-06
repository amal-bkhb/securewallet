package com.securewallet;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import java.security.KeyStore;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import android.util.Base64;

/**
 * Gestionnaire de chiffrement AES-GCM via AndroidKeyStore.
 * Génère, stocke et utilise une clé cryptographique sécurisée.
 */
public class CryptoManager {

    private static final String KEY_ALIAS = "SecureWalletKey";
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String SEPARATOR = ":";

    /**
     * Génère la clé AES dans AndroidKeyStore si elle n'existe pas encore.
     */
    private static void generateKeyIfNeeded() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    ANDROID_KEYSTORE);

            KeyGenParameterSpec keySpec = new KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build();

            keyGenerator.init(keySpec);
            keyGenerator.generateKey();
        }
    }

    /**
     * Récupère la clé secrète depuis AndroidKeyStore.
     */
    private static SecretKey getSecretKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);
        return (SecretKey) keyStore.getKey(KEY_ALIAS, null);
    }

    /**
     * Chiffre une chaîne de caractères avec AES-GCM.
     * Retourne "IV:DonnéesChiffrées" encodé en Base64.
     */
    public static String encrypt(String data) throws Exception {
        generateKeyIfNeeded();
        SecretKey secretKey = getSecretKey();

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedData = cipher.doFinal(data.getBytes());
        byte[] iv = cipher.getIV();

        String ivBase64 = Base64.encodeToString(iv, Base64.DEFAULT);
        String dataBase64 = Base64.encodeToString(encryptedData, Base64.DEFAULT);

        return ivBase64 + SEPARATOR + dataBase64;
    }

    /**
     * Déchiffre une chaîne chiffrée avec AES-GCM.
     * Attend le format "IV:DonnéesChiffrées" encodé en Base64.
     */
    public static String decrypt(String encryptedText) throws Exception {
        SecretKey secretKey = getSecretKey();

        String[] parts = encryptedText.split(SEPARATOR);
        byte[] iv = Base64.decode(parts[0], Base64.DEFAULT);
        byte[] encryptedData = Base64.decode(parts[1], Base64.DEFAULT);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

        byte[] decryptedData = cipher.doFinal(encryptedData);
        return new String(decryptedData);
    }
}