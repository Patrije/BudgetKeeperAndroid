package com.example.pati.retrofitappintro.repository;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.pati.retrofitappintro.model.Category;

import java.util.concurrent.Executors;

@Database(entities = {Category.class}, version = 4)
public abstract class CategoryDatabase extends RoomDatabase {
    public abstract CategoryDao categoryDao();

    private static volatile CategoryDatabase INSTANCE;

    public static CategoryDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CategoryDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), CategoryDatabase.class, "category_database.db").fallbackToDestructiveMigration().addCallback(new RoomDatabase.Callback() {
                        public void onCreate(SupportSQLiteDatabase db) {
                            Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    new CategoryRepository((Application) context).insertCategory(new Category(1, "Name"));
                                }
                            });
                        } }).build();
                }
            }
        }
        return INSTANCE;
    }

}
