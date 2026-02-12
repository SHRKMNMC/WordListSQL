package com.android.example.wordlistsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WordListOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = WordListOpenHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 2; // incrementado por nueva columna
    private static final String DATABASE_NAME = "wordlist";
    private static final String WORD_LIST_TABLE = "word_entries";

    public static final String KEY_ID = "_id";
    public static final String KEY_WORD = "word";
    public static final String KEY_DEFINITION = "definition";

    private static final String[] COLUMNS = { KEY_ID, KEY_WORD, KEY_DEFINITION };

    private static final String WORD_LIST_TABLE_CREATE =
            "CREATE TABLE " + WORD_LIST_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_WORD + " TEXT, " +
                    KEY_DEFINITION + " TEXT );";

    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    public WordListOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(WORD_LIST_TABLE_CREATE);
        fillDatabaseWithData(db);
    }

    private void fillDatabaseWithData(SQLiteDatabase db) {
        String[] words = {"Android", "Adapter", "ListView", "AsyncTask",
                "Android Studio", "SQLiteDatabase", "SQLOpenHelper",
                "Data model", "ViewHolder","Android Performance",
                "OnClickListener"};

        ContentValues values = new ContentValues();

        for (String word : words) {
            values.put(KEY_WORD, word);
            values.put(KEY_DEFINITION, "Sin definición"); // valor inicial
            db.insert(WORD_LIST_TABLE, null, values);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", destroying old data");
        db.execSQL("DROP TABLE IF EXISTS " + WORD_LIST_TABLE);
        onCreate(db);
    }

    // ---------------- QUERY ----------------

    public WordItem query(int position) {
        String query = "SELECT * FROM " + WORD_LIST_TABLE +
                " ORDER BY " + KEY_WORD + " ASC " +
                " LIMIT " + position + ",1";

        Cursor cursor = null;
        WordItem entry = new WordItem();

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }

            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();

            entry.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            entry.setWord(cursor.getString(cursor.getColumnIndex(KEY_WORD)));
            entry.setDefinition(cursor.getString(cursor.getColumnIndex(KEY_DEFINITION)));

        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }

        return entry;
    }

    // ---------------- COUNT ----------------

    public long count() {
        if (mReadableDB == null) {
            mReadableDB = getReadableDatabase();
        }
        return DatabaseUtils.queryNumEntries(mReadableDB, WORD_LIST_TABLE);
    }

    // ---------------- INSERT ----------------

    public long insert(String word, String definition) {
        long newId = 0;
        ContentValues values = new ContentValues();
        values.put(KEY_WORD, word);
        values.put(KEY_DEFINITION, definition);

        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            newId = mWritableDB.insert(WORD_LIST_TABLE, null, values);

        } catch (Exception e) {
            Log.d(TAG, "INSERT EXCEPTION: " + e.getMessage());
        }

        return newId;
    }

    // ---------------- DELETE ----------------

    public int delete(int id) {
        int deleted = 0;

        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }

            deleted = mWritableDB.delete(
                    WORD_LIST_TABLE,
                    KEY_ID + " = ?",
                    new String[]{String.valueOf(id)}
            );

        } catch (Exception e) {
            Log.d(TAG, "DELETE EXCEPTION: " + e.getMessage());
        }

        return deleted;
    }

    // ---------------- UPDATE ----------------

    public int update(int id, String word, String definition) {
        int updated = -1;

        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }

            ContentValues values = new ContentValues();
            values.put(KEY_WORD, word);
            values.put(KEY_DEFINITION, definition);

            updated = mWritableDB.update(
                    WORD_LIST_TABLE,
                    values,
                    KEY_ID + " = ?",
                    new String[]{String.valueOf(id)}
            );

        } catch (Exception e) {
            Log.d(TAG, "UPDATE EXCEPTION: " + e.getMessage());
        }

        return updated;
    }

    //EL SEARCH
    public Cursor search(String searchString) {

        String[] columns = { KEY_WORD };

        // Añadir % para búsqueda parcial
        String like = "%" + searchString + "%";

        // WHERE word LIKE ?
        String selection = KEY_WORD + " LIKE ?";

        String[] selectionArgs = { like };

        Cursor cursor = null;

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }

            cursor = mReadableDB.query(
                    WORD_LIST_TABLE,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    KEY_WORD + " ASC"
            );

        } catch (Exception e) {
            Log.d(TAG, "SEARCH EXCEPTION: " + e.getMessage());
        }

        return cursor;
    }


}
