package com.android.example.wordlistsql;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SearchActivity extends AppCompatActivity {

    private TextView mTextView;
    private EditText mEditWordView;
    private WordListOpenHelper mDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mEditWordView = findViewById(R.id.search_word);
        mTextView = findViewById(R.id.search_result);
        mDB = new WordListOpenHelper(this);
    }

    public void showResult(View view) {

        String word = mEditWordView.getText().toString();
        mTextView.setText("Result for \"" + word + "\":\n\n");

        Cursor cursor = mDB.search(word);

        // Si no hay resultados → mostrar diálogo
        if (cursor == null || cursor.getCount() == 0) {

            new AlertDialog.Builder(this)
                    .setTitle("Sin resultados")
                    .setMessage("No se encontraron coincidencias para \"" + word + "\".")
                    .setPositiveButton("OK", null)
                    .show();

            mTextView.append("No results found.");
            return;
        }

        // Si hay resultados → procesarlos
        cursor.moveToFirst();

        do {
            int index = cursor.getColumnIndex(WordListOpenHelper.KEY_WORD);
            String result = cursor.getString(index);
            mTextView.append(result + "\n");

        } while (cursor.moveToNext());

        cursor.close();
    }



}
