package com.android.example.wordlistsql;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditWordActivity extends AppCompatActivity {

    private static final int NO_ID = -99;

    private EditText mEditWordView;
    private EditText mEditDefinitionView;

    // Extras que esta Activity devuelve
    public static final String EXTRA_REPLY_WORD = "REPLY_WORD";
    public static final String EXTRA_REPLY_DEFINITION = "REPLY_DEFINITION";

    int mId = MainActivity.WORD_ADD;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_word);

        mEditWordView = findViewById(R.id.edit_word);
        mEditDefinitionView = findViewById(R.id.edit_definition);

        // Recibir datos enviados desde el Adapter
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int id = extras.getInt(WordListAdapter.EXTRA_ID, NO_ID);
            String word = extras.getString(WordListAdapter.EXTRA_WORD, "");
            String definition = extras.getString(WordListAdapter.EXTRA_DEFINITION, "");

            if (id != NO_ID) {
                mId = id;
                mEditWordView.setText(word);
                mEditDefinitionView.setText(definition);
            }
        }
    }

    // Guardar cambios
    public void returnReply(View view) {

        String word = mEditWordView.getText().toString();
        String definition = mEditDefinitionView.getText().toString();

        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY_WORD, word);
        replyIntent.putExtra(EXTRA_REPLY_DEFINITION, definition);
        replyIntent.putExtra(WordListAdapter.EXTRA_ID, mId);

        setResult(RESULT_OK, replyIntent);
        finish();
    }
}
