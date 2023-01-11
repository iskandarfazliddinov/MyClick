package com.example.course.db

import androidx.room.*
import androidx.room.Dao


@Dao
interface Dao2 {

    @Query("select * from users")
    fun getAllUser(): List<UserData2>


    @Insert
    fun addUser(userData: UserData2)

    @Delete
    fun deletUser(userData: UserData2)

    @Update
    fun editUser(userData: UserData2)

    @Query("SELECT SUM(summa) AS sum_quantity FROM users")
    fun getSumms():Int

}