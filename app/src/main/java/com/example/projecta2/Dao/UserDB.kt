package com.example.projecta2.Dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.projecta2.Entity.UserInfo

@Database(entities = [UserInfo::class], version = 1)
@TypeConverters(Converters::class)
abstract class UserDB : RoomDatabase() {
    abstract fun getDao() : UserDao
}