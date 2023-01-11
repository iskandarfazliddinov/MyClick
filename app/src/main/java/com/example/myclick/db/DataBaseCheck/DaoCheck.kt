package com.example.course.db

import androidx.room.*
import androidx.room.Dao


@Dao
interface DaoCheck {

    @Query("select * from users")
    fun getAllUser(): UserDataCheck

    @Query("select * from users")
    fun getAll(): List<UserDataCheck>

    @Insert
    fun addUser(userData: UserDataCheck)

    @Delete
    fun deletUser(userData: UserDataCheck)

    @Update
    fun editUser(userData: UserDataCheck)

    @Query("select * from Users where id =:id ")
    fun getUserById(id:Int):UserDataCheck




}