package com.xfinity.data.daos

import android.arch.persistence.room.*
import com.xfinity.data.entities.CharacterEntity
import io.reactivex.Flowable

@Dao
interface CharacterDao {

    @Query("SELECT * FROM CharacterEntity")
    fun getCharactersFlowable(): Flowable<List<CharacterEntity>>

    @Query("SELECT * FROM CharacterEntity WHERE isFavorite = 1")
    fun getFavoriteCharactersFlowable(): Flowable<List<CharacterEntity>>

    @Query("SELECT * FROM CharacterEntity WHERE description = :description")
    fun getCharacterFlowable(description: String): Flowable<CharacterEntity>

    @Query("SELECT * FROM CharacterEntity WHERE name LIKE '%' || :query || '%'")
    fun getSerachCharactersFlowable(query: String): Flowable<List<CharacterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(characters: List<CharacterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(character: CharacterEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(character: CharacterEntity)

    @Query("SELECT isFavorite FROM CharacterEntity WHERE description = :description")
    fun getIsFavorite(description: String): Boolean

}