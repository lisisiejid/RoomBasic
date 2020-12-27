package com.example.roombasic;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

//singleton(使得数据库单一调用)，同时调用不太好
@Database(entities = {Word.class},version = 5,exportSchema = false)//entities字段，version版本(字段改变版本就要变)
public abstract class WordDatabase extends RoomDatabase {
    public abstract  WordDao getWordDao();//若有多个entitiy则写多个Dao
    /*singleton*/
    private static WordDatabase INSTANCE;
    static synchronized WordDatabase getDatabase(Context context){
        if(INSTANCE==null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),WordDatabase.class,"word_database")
//                    .allowMainThreadQueries()
//                    .fallbackToDestructiveMigration()//破环式的迁移，会清空之前的数据，相当于重新建了一个数据库
                    .addMigrations(MIGRATION_4_5)//温和型的迁移，参数为一个Migration的常量
                    .build();
        }
        return INSTANCE;
    }
    /*添加字段*/
    static final Migration MIGRATION_2_3 = new Migration(2,3) {//参数为版本几到几。如这里的2到3
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE word ADD COLUMN bar_data INTEGER NOT NULL DEFAULT 1");//这个数据库中没有bool值，用integer来存储bool
        }
    };
    /*删除字段*/
    static final Migration MIGRATION_3_4 = new Migration(3,4) {//参数为版本几到几。如这里的2到3
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE word_temp (id INTEGER PRIMARY KEY NOT NULL,chinese_word TEXT,english_word TEXT)");
            database.execSQL("INSERT INTO word_temp(id,chinese_word,english_word) SELECT id,chinese_word,english_word FROM word");
            database.execSQL("DROP TABLE word");
            database.execSQL("ALTER TABLE word_temp RENAME TO word");
        }
    };

    static final Migration MIGRATION_4_5 = new Migration(4,5) {//添加中文显示字段
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE word ADD COLUMN chinese_invisible INTEGER NOT NULL DEFAULT 0");//这个数据库中没有bool值，用integer来存储bool
        }
    };
}
