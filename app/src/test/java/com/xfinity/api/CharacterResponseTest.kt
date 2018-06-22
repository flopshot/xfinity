package com.xfinity.api

import com.xfinity.api.apiresponses.CharacterResponse
import com.xfinity.api.apiresponses.IconResponse
import com.xfinity.data.AppDatabase
import com.xfinity.data.daos.CharacterDao
import com.xfinity.data.entities.CharacterEntity
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharacterResponseTest {

    @Mock
    lateinit var appDb: AppDatabase
    @Mock
    lateinit var characterDao: CharacterDao

    @Test
    fun responseToCharacterEntity() {
        val description
           = """
                Marge Simpson - Marjorie Jacqueline \"Marge\" Simpson is a fictional character in the
                American animated sitcom The Simpsons and part of the eponymous family. She is voiced
                 by Julie Kavner and first appeared on television in The Tracey Ullman Show short
                 \"Good Night\" on April 19, 1987.
            """.trimIndent()

        val iconResponse = IconResponse("https://duckduckgo.com/i/b75bd67d.png")
        val characterResponse = CharacterResponse(iconResponse, description)
        val characterEntity = CharacterEntity(description, iconResponse.pictureUrl, "Marge Simpson", false)

        `when`(appDb.characterDao()).thenReturn(characterDao)
        `when`(characterDao.getIsFavorite(description)).thenReturn(false)

        assertEquals(characterResponse.characterEntityFromResponse(appDb), characterEntity)
    }

    @Test
    fun responseToCharacterEntityNoName() {
        val description
                = """
                No name etc09b
            """.trimIndent()

        val iconResponse = IconResponse("https://duckduckgo.com/i/b75bd67d.png")
        val characterResponse = CharacterResponse(iconResponse, description)
        val characterEntity = CharacterEntity(description, iconResponse.pictureUrl, "No Name", false)

        `when`(appDb.characterDao()).thenReturn(characterDao)
        `when`(characterDao.getIsFavorite(characterResponse.description)).thenReturn(false)

        assertEquals(characterResponse.characterEntityFromResponse(appDb), characterEntity)
    }
}