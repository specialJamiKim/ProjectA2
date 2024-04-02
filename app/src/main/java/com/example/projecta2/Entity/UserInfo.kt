package com.example.projecta2.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//
@Entity
data class UserInfo(
    @PrimaryKey val Id: Long?,
    @ColumnInfo val email : String?,
    @ColumnInfo val name: String?,
    @ColumnInfo val password: String?,
    @ColumnInfo val phoneNumber: String?,
    @ColumnInfo val gender: String?,
    @ColumnInfo val address: String?,
    @ColumnInfo val joinDate: String?,
    @ColumnInfo val role: List<String>,
    @ColumnInfo val birthDate: String
)