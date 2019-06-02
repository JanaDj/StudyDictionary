package com.example.meangirl.dictionary.SQLIte;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Dictionary.db";
    public static int DB_VERSION = 1;
    public static String DB_FILEPATH = "/data/data/com.example.meangirl.dictionary.SQLIte/databases/" + DB_NAME;

    public static String DATABASE_CREATION =
            "CREATE TABLE "+ Contract.TermsSchema.TABLE_NAME + " (" +
                    Contract.TermsSchema._ID + " INTEGER PRIMARY KEY autoincrement NOT NULL, "
                    + Contract.TermsSchema.FIELD_TERM + " TEXT, "
                    + Contract.TermsSchema.FIELD_DEFINITION + " TEXT" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATION);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Contract.TermsSchema.TABLE_NAME);
        onCreate(db);
    }
    /**
     * Copies the database file at the specified location over the current
     * internal application database.
     * */
    public boolean importDatabase(String dbPath) throws IOException {

        // Close the SQLiteOpenHelper so it will commit the created empty
        // database to internal storage.
        close();
        File newDb = new File(dbPath);
        File oldDb = new File(DB_FILEPATH);
        if (newDb.exists()) {
            FileUtils.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));
            // Access the copied database so SQLiteHelper will cache it and mark
            // it as created.
            getWritableDatabase().close();
            return true;
        }
        return false;
    }
}

