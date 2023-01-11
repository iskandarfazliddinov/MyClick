package com.example.course.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserDataSec::class], version = 1)
abstract class AppDatabaseSec : RoomDatabase() {

    abstract fun userDao(): DaoSec

    companion object {

        private var appDataBase: AppDatabaseSec? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabaseSec{
            if (appDataBase == null) {
                appDataBase = Room.databaseBuilder(context, AppDatabaseSec::class.java, "my_dbsec")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return appDataBase!!
        }
    }
}