package com.asadullo.valyuta.Activity.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.asadullo.valyuta.Activity.Models.MainValyuta

@Dao
interface DbInterface {
    @Insert
    fun add(user: MainValyuta)

    @Query("select * from MainValyuta")
    fun get():List<MainValyuta>
}