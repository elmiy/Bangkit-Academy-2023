package com.example.githubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Query("SELECT * FROM UserEntity ORDER BY username DESC")
    fun getUser(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM UserEntity where username = :username")
    fun searchUser(username: String): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM UserEntity where favorite = 1")
    fun getFavoriteUsers(): LiveData<List<UserEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(user: UserEntity)

    @Query("SELECT EXISTS(SELECT * FROM UserEntity WHERE username = :username AND favorite = 1)")
    suspend fun isUserFavorite(username: String): Boolean
}