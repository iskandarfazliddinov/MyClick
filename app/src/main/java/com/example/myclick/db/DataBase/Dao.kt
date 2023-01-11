package com.example.course.db

import androidx.room.*
import androidx.room.Dao
import io.reactivex.rxjava3.core.Flowable


@Dao
interface Dao {

    @Query("select * from users")
    fun getAllUser(): Flowable<List<UserData>>

    @Query("select * from users")
    fun getAllUserSize(): List<UserData>

    @Insert
    fun addUser(userData: UserData)

    @Delete
    fun deletUser(userData: UserData)

    @Update
    fun editUser(userData: UserData)

    @Query("select * from Users where id =:id ")
    fun getUserById(id:Int):UserData


}