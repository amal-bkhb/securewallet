package com.securewallet;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wallet.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "cards";
    private static final String COL_ID = "id";
    private static final String COL_HOLDER = "holder";
    private static final String COL_LAST_FOUR = "last_four";
    private static final String COL_USER_ID = "user_id";

    // Mot de passe pour chiffrer la base SQLCipher
    private static final String DB_PASSWORD = "SecureWallet2024";

    private Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
        // Chargement des librairies SQLCipher
        SQLiteDatabase.loadLibs(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_HOLDER + " TEXT NOT NULL, " +
                COL_LAST_FOUR + " TEXT NOT NULL, " +
                COL_USER_ID + " TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Ouvre la base de données avec le mot de passe SQLCipher
    private SQLiteDatabase openDB() {
        return getWritableDatabase(DB_PASSWORD);
    }

    public void saveCard(String holder, String lastFour) {
        try {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String encryptedHolder = CryptoManager.encrypt(holder);
            String encryptedLastFour = CryptoManager.encrypt(lastFour);
            SQLiteDatabase db = openDB();
            ContentValues values = new ContentValues();
            values.put(COL_HOLDER, encryptedHolder);
            values.put(COL_LAST_FOUR, encryptedLastFour);
            values.put(COL_USER_ID, uid);
            db.insert(TABLE_NAME, null, values);
            db.close();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erreur : " + e.getMessage());
        }
    }

    public List<Card> getAllCards() {
        List<Card> cardList = new ArrayList<>();
        try {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            SQLiteDatabase db = openDB();
            Cursor cursor = db.rawQuery(
                    "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_USER_ID + " = ?",
                    new String[]{uid}
            );
            if (cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndex(COL_ID);
                    int holderIndex = cursor.getColumnIndex(COL_HOLDER);
                    int lastFourIndex = cursor.getColumnIndex(COL_LAST_FOUR);
                    if (idIndex != -1 && holderIndex != -1 && lastFourIndex != -1) {
                        int id = cursor.getInt(idIndex);
                        String holder = CryptoManager.decrypt(cursor.getString(holderIndex));
                        String lastFour = CryptoManager.decrypt(cursor.getString(lastFourIndex));
                        cardList.add(new Card(id, holder, lastFour));
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erreur : " + e.getMessage());
        }
        return cardList;
    }

    public void deleteCard(int id) {
        try {
            SQLiteDatabase db = openDB();
            db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
            db.close();
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Erreur suppression : " + e.getMessage());
        }
    }
}