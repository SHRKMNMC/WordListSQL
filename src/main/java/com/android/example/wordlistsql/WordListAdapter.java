package com.android.example.wordlistsql;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_WORD = "WORD";
    public static final String EXTRA_DEFINITION = "DEFINITION";
    public static final String EXTRA_POSITION = "POSITION";

    private final LayoutInflater mInflater;
    private WordListOpenHelper mDB;
    private Context mContext;

    class WordViewHolder extends RecyclerView.ViewHolder {
        public final TextView wordItemView;
        public final TextView definitionItemView;
        Button delete_button;
        Button edit_button;

        public WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.word);
            definitionItemView = itemView.findViewById(R.id.definition);
            delete_button = itemView.findViewById(R.id.delete_button);
            edit_button = itemView.findViewById(R.id.edit_button);
        }
    }

    public WordListAdapter(Context context, WordListOpenHelper db) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mDB = db;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.wordlist_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {

        WordItem current = mDB.query(position);
        holder.wordItemView.setText(current.getWord());
        holder.definitionItemView.setText(current.getDefinition());

        // DELETE con confirmación
        holder.delete_button.setOnClickListener(v -> {

            new AlertDialog.Builder(mContext)
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Seguro que quieres borrar \"" + current.getWord() + "\"?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        mDB.delete(current.getId());
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        // EDIT
        holder.edit_button.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, EditWordActivity.class);
            intent.putExtra(EXTRA_ID, current.getId());
            intent.putExtra(EXTRA_POSITION, holder.getAdapterPosition());
            intent.putExtra(EXTRA_WORD, current.getWord());
            intent.putExtra(EXTRA_DEFINITION, current.getDefinition());
            ((Activity) mContext).startActivityForResult(intent, MainActivity.WORD_EDIT);
        });
    }

    @Override
    public int getItemCount() {
        return (int) mDB.count();
    }
}
