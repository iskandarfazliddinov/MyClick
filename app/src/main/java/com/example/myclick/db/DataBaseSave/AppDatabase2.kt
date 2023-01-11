package com.example.course.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserData2::class], version = 1)
abstract class AppDatabase2 : RoomDatabase() {

    abstract fun userDaos(): Dao2

    companion object {

        private var appDataBase: AppDatabase2? = null

        @Synchronized
        fun getInstance2(context: Context): AppDatabase2{
            if (appDataBase == null) {
                appDataBase = Room.databaseBuilder(context, AppDatabase2::class.java, "my_dbs")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return appDataBase!!
        }
    }
}