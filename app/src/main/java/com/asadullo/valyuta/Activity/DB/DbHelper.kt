package com.asadullo.valyuta.Activity.DB

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.asadullo.valyuta.Activity.Models.MainValyuta

@Database(entities = [MainValyuta::class], version = 1)
abstract class DbHelper:RoomDatabase() {

    abstract fun dao():DbInterface

    companion object{
        var instance:DbHelper? = null

        @Synchronized
        fun getIns(context: Context):DbHelper{

            if (instance == null){
                instance = Room.databaseBuilder(
                    context,
                    DbHelper::class.java,
                    "my_db"
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }

            return instance!!
        }
    }
}