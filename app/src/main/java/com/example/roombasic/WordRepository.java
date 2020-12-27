package com.example.roombasic;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

/*数据库的仓库类*/
public class WordRepository {
    LiveData<List<Word>> AllWordsLive;
    WordDao wordDao;

    public WordRepository(Context context) {
        WordDatabase wordDatabase = WordDatabase.getDatabase(context.getApplicationContext());
        wordDao = wordDatabase.getWordDao();
        AllWordsLive = wordDao.getAllWordsLive();
    }

    public LiveData<List<Word>> getAllWordsLive() {
        return AllWordsLive;
    }
    public LiveData<List<Word>> findWordWithParren(String patten){
        return wordDao.findWordWithPatten("%"+patten+"%");//模糊匹配
    }


    /*建立接口*/
    void insert_interface(Word...words){
        new InsertAsyncTask(wordDao).execute(words);
    }
    void delete_interface(Word...words){
        new DeleteAsyncTask(wordDao).execute(words);
    }
    void update_interface(Word...words){
        new UpdateAsyncTask(wordDao).execute(words);
    }
    void clear_interface(){
        new ClearAsyncTask(wordDao).execute();
    }


    /*副线程运行数据库的操作*/
    static class InsertAsyncTask extends AsyncTask<Word,Void,Void> {//第一个参数是操作的内容，第二个是运行的状态，第三个是返回的结果
        private WordDao wordDao;

        public InsertAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }
        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insertWords(words);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Word,Void,Void>{
        private WordDao wordDao;

        public DeleteAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }
        @Override
        protected Void doInBackground(Word... words) {
            wordDao.DeleteWords(words);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Word,Void,Void>{
        private WordDao wordDao;

        public UpdateAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }
        @Override
        protected Void doInBackground(Word... words) {
            wordDao.UpdateWords(words);
            return null;
        }
    }

    static class ClearAsyncTask extends AsyncTask<Void,Void,Void>{
        private WordDao wordDao;

        public ClearAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteAllWords();
            return null;
        }
    }
}
