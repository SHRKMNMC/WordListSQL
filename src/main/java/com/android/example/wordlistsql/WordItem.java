package com.android.example.wordlistsql;

public class WordItem {

    private int mId;
    private String mWord;
    private String mDefinition;

    public WordItem() {}

    public int getId() { return mId; }
    public void setId(int id) { this.mId = id; }

    public String getWord() { return mWord; }
    public void setWord(String word) { this.mWord = word; }

    public String getDefinition() { return mDefinition; }
    public void setDefinition(String definition) { this.mDefinition = definition; }
}
