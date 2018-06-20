package com.xfinity.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.xfinity.data.daos.CharacterDao
import com.xfinity.data.entities.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}