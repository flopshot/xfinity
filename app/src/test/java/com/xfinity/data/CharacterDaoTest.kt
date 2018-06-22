package com.xfinity.data

import android.arch.persistence.room.Room
import com.xfinity.data.entities.CharacterEntity
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment


@RunWith(RobolectricTestRunner::class)
class CharacterDaoTest {

    private lateinit var appDb: AppDatabase
    private lateinit var character1: CharacterEntity
    private lateinit var character2: CharacterEntity

    @Before
    fun initDbAndSetup() {
        appDb = Room.inMemoryDatabaseBuilder(RuntimeEnvironment.application,AppDatabase::class.java)
                        .allowMainThreadQueries()
                        .build()

        character1 = CharacterEntity(
                "foo1 - foo1 is cool",
                "foo1.com",
                "foo1",
                true)

        character2 = CharacterEntity(
                "foo2 - foo2 is not cool",
                "foo2com",
                "foo2",
                false)

    }

    @After
    fun closeDb() {
        appDb.close()
    }

    @Test
    fun insertAndGetCharacter() {
        appDb.characterDao().insert(character1)
        assertEquals(character1, appDb.characterDao().getCharacter(character1.description))
    }

    @Test
    fun insertListOfCharacters() {
        val characterList = listOf(character1, character2)
        appDb.characterDao().insertList(characterList)
        for (character in characterList) {
            assertEquals(character, appDb.characterDao().getCharacter(character.description))
        }
    }

    @Test
    fun updateCharacter() {
        appDb.characterDao().insert(character1)
        character1.name = "foo1 bar1"
        character1.isFavorite = false
        character1.pictureUrl = "foo1bar1.com"
        appDb.characterDao().update(character1)
        assertEquals(character1, appDb.characterDao().getCharacter(character1.description))
    }

    @Test
    fun getIsFavorite() {
        appDb.characterDao().insert(character1)
        assertEquals(character1.isFavorite, appDb.characterDao().getIsFavorite(character1.description))
    }

    @Test
    fun getCharacterFlowable() {
        appDb.characterDao().insert(character1)

        val testObserver
                =  appDb
                    .characterDao()
                    .getCharacterFlowable(character1.description)
                    .test()

        testObserver.awaitCount(1)
        testObserver
                .assertNoErrors()
                .assertValue(character1)
    }

    @Test
    fun getCharactersFlowable() {
        val characterList = listOf(character1, character2)
        appDb.characterDao().insertList(characterList)

        val testObserver
                =  appDb
                    .characterDao()
                    .getCharactersFlowable()
                    .test()
        testObserver.awaitCount(1)
        testObserver
                .assertNoErrors()
                .assertValue(characterList)
    }

    @Test
    fun getFavoriteCharactersFlowable() {
        val characterList = listOf(character1, character2)
        appDb.characterDao().insertList(characterList)

        val testObserver
                =  appDb
                    .characterDao()
                    .getFavoriteCharactersFlowable()
                    .test()

        testObserver.awaitCount(1)
        testObserver
                .assertNoErrors()
                .assertValue(listOf(character1))
    }

    @Test
    fun getSearchCharactersFlowable() {
        val characterList = listOf(character1, character2)
        appDb.characterDao().insertList(characterList)

        val testObserver
                =  appDb
                .characterDao()
                .getSearchCharactersFlowable("1")
                .test()

        testObserver.awaitCount(1)
        testObserver
                .assertNoErrors()
                .assertValue(listOf(character1))
    }

    @Test
    fun validDescriptionSearchWithCharactersFlowable() {
        val characterList = listOf(character1, character2)
        appDb.characterDao().insertList(characterList)

        val testObserver
                =  appDb
                .characterDao()
                .getSearchCharactersFlowable("not cool")
                .test()

        testObserver.awaitCount(1)
        testObserver
                .assertNoErrors()
                .assertValue(listOf(character2))
    }

}