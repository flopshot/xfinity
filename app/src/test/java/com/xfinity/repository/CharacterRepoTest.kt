package com.xfinity.repository

import com.xfinity.api.ApiResource
import com.xfinity.api.CharacterViewerApi
import com.xfinity.api.Status
import com.xfinity.api.apiresponses.CharacterResponse
import com.xfinity.api.apiresponses.CharactersResponse
import com.xfinity.api.apiresponses.IconResponse
import com.xfinity.data.AppDatabase
import com.xfinity.data.daos.CharacterDao
import com.xfinity.data.entities.CharacterEntity
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharacterRepoTest {

    @Mock
    lateinit var appDb: AppDatabase
    @Mock
    lateinit var api: CharacterViewerApi
    @InjectMocks
    lateinit var characterRepo: CharacterRepository

    @Mock
    lateinit var characterDao: CharacterDao

    @Test
    fun getCharactersFromNetwork() {
        val character1 = CharacterEntity(
                "foo1 - foo1 is cool",
                "foo1.com",
                "foo1",
                false)

        val character2 = CharacterEntity(
                "foo2 - foo2 is not cool",
                "foo2com",
                "foo2",
                false)

        val characterResponse1 = CharacterResponse(IconResponse(character1.pictureUrl), character1.description)
        val characterResponse2 = CharacterResponse(IconResponse(character2.pictureUrl), character2.description)
        val responseCharacters = CharactersResponse(listOf(characterResponse1, characterResponse2))

        val characterList = listOf(character1, character2)
        val dbData = Flowable.just(characterList)
        val responseObservable = Observable.just(responseCharacters)

        `when`(appDb.characterDao()).thenReturn(characterDao)
        `when`(characterDao.getCharactersFlowable()).thenReturn(dbData)
        `when`(api.getCharacterList()).thenReturn(responseObservable)

        val testSubscriber = TestObserver<ApiResource<List<CharacterEntity>>>()
        val observable
                = characterRepo.getCharactersObservable(CharacterRepository.FILTER_TYPE_NONE)

        observable.subscribe(testSubscriber)
        testSubscriber.awaitCount(2)
        testSubscriber.assertNoErrors()
        testSubscriber.assertValueAt(0) { characterApiResource ->
            characterApiResource.status === Status.LOADING
                    && characterApiResource.data != null
                    && characterApiResource.data == characterList
        }

        testSubscriber.assertValueAt(1) { characterApiResource ->
            characterApiResource.status === Status.SUCCESS
                    && characterApiResource.data != null
                    && characterApiResource.data == characterList
        }
    }
}