package com.example.course.db

import androidx.room.*
import androidx.room.Dao
import io.reactivex.rxjava3.core.Flowable


@Dao
interface DaoSec {

    @Query("select * from users")
    fun getAllUser(): List<UserDataSec>

    @Query("select * from users")
    fun getAllUserSize(): List<UserDataSec>

    @Insert
    fun addUser(userData: UserDataSec)

    @Delete
    fun deletUser(userData: UserDataSec)

    @Update
    fun editUser(userData: UserDataSec)


}