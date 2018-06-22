package com.xfinity.data.daos

import android.arch.persistence.room.*
import com.xfinity.data.entities.CharacterEntity
import io.reactivex.Flowable

@Dao
interface CharacterDao {

    @Query("SELECT * FROM CharacterEntity ORDER BY name")
    fun getCharactersFlowable(): Flowable<List<CharacterEntity>>

    @Query("SELECT * FROM CharacterEntity WHERE isFavorite = 1 ORDER BY name")
    fun getFavoriteCharactersFlowable(): Flowable<List<CharacterEntity>>

    @Query("SELECT * " +
            "FROM CharacterEntity " +
            "WHERE name LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' " +
            "ORDER BY name")
    fun getSearchCharactersFlowable(query: String): Flowable<List<CharacterEntity>>

    @Query(GET_CHARACTER_BY_PK_QUERY_STRING)
    fun getCharacterFlowable(description: String): Flowable<CharacterEntity>

    @Query("SELECT isFavorite FROM CharacterEntity WHERE description = :description")
    fun getIsFavorite(description: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(characters: List<CharacterEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(character: CharacterEntity): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(character: CharacterEntity)

    @Query(GET_CHARACTER_BY_PK_QUERY_STRING)
    fun getCharacter(description: String): CharacterEntity

    companion object {
        private const val GET_CHARACTER_BY_PK_QUERY_STRING: String
                = "SELECT * FROM CharacterEntity WHERE description = :description"
    }
}