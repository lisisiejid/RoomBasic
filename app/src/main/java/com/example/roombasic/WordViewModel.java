package com.example.roombasic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WordViewModel extends AndroidViewModel {
    WordRepository wordRepository;

    public WordViewModel(@NonNull Application application) {
        super(application);
        wordRepository = new WordRepository(application);
    }

    public LiveData<List<Word>> getAllWordsLive() {
        return wordRepository.getAllWordsLive();
    }
    public  LiveData<List<Word>> findWordWithPatten(String patten){ return wordRepository.findWordWithParren(patten);}

    /*建立接口*/
    void insert_interface(Word...words){
        wordRepository.insert_interface(words);
    }
    void delete_interface(Word...words){
        wordRepository.delete_interface(words);
    }
    void update_interface(Word...words){
        wordRepository.update_interface(words);
    }
    void clear_interface(){
        wordRepository.clear_interface();
    }

}
