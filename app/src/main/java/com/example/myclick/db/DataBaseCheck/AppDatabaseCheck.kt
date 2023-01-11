package com.example.course.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserDataCheck::class], version = 1)
abstract class AppDatabaseCheck : RoomDatabase() {

    abstract fun userDaos(): DaoCheck

    companion object {

        private var appDataBase: AppDatabaseCheck? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabaseCheck {
            if (appDataBase == null) {
                appDataBase = Room.databaseBuilder(context, AppDatabaseCheck::class.java, "my_dbcheck")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return appDataBase!!
        }
    }
}