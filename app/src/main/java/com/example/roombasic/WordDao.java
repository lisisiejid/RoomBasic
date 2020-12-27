package com.example.roombasic;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao  //访问数据库的接口
public interface WordDao {//数据库的增删查改
    @Insert
    void insertWords(Word...words);//可以插入多个

    @Update
    void UpdateWords(Word...words);//改

    @Delete
    void DeleteWords(Word...words);//删

    @Query("DELETE FROM WORD")
    void deleteAllWords();//清空

    @Query("SELECT * FROM WORD ORDER BY ID DESC")//desc表示降序排列
//    List<Word> getAllWords();//查
    LiveData<List<Word>> getAllWordsLive();

    @Query("SELECT * FROM WORD WHERE english_word LIKE :patten ORDER BY ID DESC")
    LiveData<List<Word>>findWordWithPatten (String patten);
}
