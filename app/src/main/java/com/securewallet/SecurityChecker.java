package com.securewallet;

import android.content.Context;
import android.content.pm.PackageManager;
import java.io.File;

/**
 * Classe de vérification de la sécurité de l'appareil.
 * Détecte si l'appareil est rooté (compromis).
 */
public class SecurityChecker {

    /**
     * Vérifie si l'appareil est rooté.
     * Un appareil rooté représente un risque de sécurité.
     */
    public static boolean isDeviceRooted() {
        return checkRootFiles() || checkSuperUser();
    }

    /**
     * Vérifie la présence de fichiers liés au root.
     */
    private static boolean checkRootFiles() {
        String[] rootPaths = {
                "/system/app/Superuser.apk",
                "/sbin/su",
                "/system/bin/su",
                "/system/xbin/su",
                "/data/local/xbin/su",
                "/data/local/bin/su",
                "/system/sd/xbin/su"
        };
        for (String path : rootPaths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    /**
     * Vérifie la présence de l'application SuperUser.
     */
    private static boolean checkSuperUser() {
        try {
            Process process = Runtime.getRuntime().exec("which su");
            return process != null;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Retourne un message sur l'état de sécurité de l'appareil.
     */
    public static String getSecurityStatus() {
        if (isDeviceRooted()) {
            return "Appareil rooté détecté — risque de sécurité !";
        } else {
            return " Appareil sécurisé";
        }
    }
}