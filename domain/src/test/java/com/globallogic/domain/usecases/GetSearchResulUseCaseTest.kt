package com.globallogic.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.globallogic.domain.entities.Song
import com.globallogic.domain.repository.BaseResponse
import com.globallogic.domain.repository.ItunesSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class GetSearchResulUseCaseTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var repository: ItunesSearchRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun testUseCaseSuccess() {
        runTest {
            // GIVEN
            `when`(
                repository.getSearchResult(
                    searchWord = anyString(),
                )
            ).thenReturn(
                SEARCH_RESPONSE
            )
            // WHEN
            val result = GetSearchResultUseCase(repository).invoke(
                searchWord = anyString(),
            )
            testDispatcher.scheduler.advanceUntilIdle()
            // THEN
            verify(repository).getSearchResult(
                searchWord = anyString(),
            )
        }
    }

    @Test
    fun testUseCaseFailure() {
        runTest {
            // GIVEN
            `when`(
                repository.getSearchResult(
                    searchWord = anyString(),
                    entityType = anyString(),
                    mediaType = anyString(),
                    limit = anyInt()
                )
            ).thenReturn(BaseResponse.Failure(EXCEPTION))

            // WHEN
            val result = GetSearchResultUseCase(repository).invoke(
                searchWord = anyString(),
                entityType = anyString(),
                mediaType = anyString(),
                limit = anyInt()
            )

            testDispatcher.scheduler.advanceUntilIdle()
            // THEN
            verify(repository).getSearchResult(
                searchWord = anyString(),
                entityType = anyString(),
                mediaType = anyString(),
                limit = anyInt()
            )
            assert(result is BaseResponse.Failure)
            assert((result as BaseResponse.Failure<Song>).exception == EXCEPTION)
        }
    }

    companion object {
        val EXCEPTION = Exception("Error")
        val SONG = Song(
            artistId = 1,
            artistName = "artist name",
            collectionId = 2,
            collectionName = "album name",
            trackId = 1,
            trackName = "song name",
            imageCover = "url",
            previewUrl = "url"
        )
        val SEARCH_RESPONSE = Flow<PagingData<Song>>
    }
}
