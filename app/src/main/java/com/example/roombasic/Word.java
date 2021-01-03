package com.example.roombasic;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity //实体
public class Word {
    @PrimaryKey(autoGenerate = true)//自动生成的主键
    private int id;

    @ColumnInfo(name = "english_word")//字段意思
    private String word;
    @ColumnInfo(name = "chinese_word")
    private String ChineseMeaning;
    @ColumnInfo(name = "chinese_invisible")
    private boolean chineseInvisible;

    public Word(String word, String ChineseMeaning) {
        this.word = word;
        this.ChineseMeaning = ChineseMeaning;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setWord(String word) {
        this.word = word;
    }


    public int getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    String getChineseMeaning() {
        return ChineseMeaning;
    }

    boolean isChineseInvisible() {
        return chineseInvisible;
    }

    void setChineseInvisible(boolean chineseInvisible) {
        this.chineseInvisible = chineseInvisible;
    }

}
