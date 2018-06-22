package com.xfinity.api

import com.xfinity.api.apiresponses.CharacterResponse
import com.xfinity.api.apiresponses.CharactersResponse
import com.xfinity.data.AppDatabase
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharactersResponseTest {

    @Mock
    lateinit var characterResponse1: CharacterResponse
    @Mock
    lateinit var characterResponse2: CharacterResponse
    @Mock
    private lateinit var appDb: AppDatabase

    @Test
    fun saveResponses() {
        val charactersResponse = CharactersResponse(listOf(characterResponse1, characterResponse2))

        `when`(characterResponse1.saveResponseToDb(appDb)).thenReturn(1L)
        `when`(characterResponse2.saveResponseToDb(appDb)).thenReturn(1L)

        assertEquals(charactersResponse.saveResponseToDb(appDb), 2L)
    }

}