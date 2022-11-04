package com.globallogic.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globallogic.domain.entities.Song
import com.globallogic.domain.repository.BaseResponse
import com.globallogic.domain.repository.ItunesSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class GetSongsByAlbumUseCaseTest {
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
                repository.getSongByAlbumId(
                    albumId = ALBUM_ID,
                    entityType = ENTITY_TYPE
                )
            )
                .thenReturn(SEARCH_RESPONSE)

            // WHEN
            val result = GetSongsByAlbumUseCase(repository).invoke(
                albumId = ALBUM_ID,
                entityType = ENTITY_TYPE
            )
            testDispatcher.scheduler.advanceUntilIdle()
            // THEN

            verify(repository).getSongByAlbumId(
                albumId = ALBUM_ID,
                entityType = ENTITY_TYPE
            )
            assert(result is BaseResponse.Success)
            assert((result as BaseResponse.Success<Song>).data == SONG)
        }
    }

    @Test
    fun testUseCaseFailure() {
        runTest {
            // GIVEN
            `when`(
                repository.getSongByAlbumId(
                    entityType = ENTITY_TYPE,
                    albumId = ALBUM_ID
                )
            ).thenReturn(BaseResponse.Failure(EXCEPTION))

            // WHEN
            val result = GetSongsByAlbumUseCase(repository).invoke(
                entityType = ENTITY_TYPE,
                albumId = ALBUM_ID
            )

            testDispatcher.scheduler.advanceUntilIdle()
            // THEN
            verify(repository).getSongByAlbumId(
                entityType = ENTITY_TYPE,
                albumId = ALBUM_ID
            )
            assert(result is BaseResponse.Failure)
            assert((result as BaseResponse.Failure<Song>).exception == EXCEPTION)
        }
    }

    companion object {
        val SONG = Song(
            artistId = 1,
            artistName = "artist name",
            artworkUrl100 = "url",
            collectionId = 2,
            collectionName = "album name",
            collectionViewUrl = "url",
            kind = "song",
            previewUrl = "url",
            trackId = 1,
            trackName = "song name",
            trackNumber = 2,
            trackViewUrl = "url"
        )
        val SEARCH_RESPONSE = BaseResponse.Success(SONG)
        val EXCEPTION = Exception("Error")
        const val ALBUM_ID = 1
        const val ENTITY_TYPE = "song"
    }
}
