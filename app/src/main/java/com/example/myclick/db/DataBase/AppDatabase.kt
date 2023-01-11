package com.example.course.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDaos(): Dao

    companion object {

        private var appDataBase: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase{
            if (appDataBase == null) {
                appDataBase = Room.databaseBuilder(context, AppDatabase::class.java, "my_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return appDataBase!!
        }
    }
}