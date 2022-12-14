package com.globallogic.domain.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.globallogic.domain.entities.Album
import com.globallogic.domain.entities.AlbumWithSongs
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
                repository.getSongsByAlbumId(
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

            verify(repository).getSongsByAlbumId(
                albumId = ALBUM_ID,
                entityType = ENTITY_TYPE
            )
            assert(result is BaseResponse.Success)
            assert((result as BaseResponse.Success<AlbumWithSongs>).data == albumWithSongs)
        }
    }

    @Test
    fun testUseCaseFailure() {
        runTest {
            // GIVEN
            `when`(
                repository.getSongsByAlbumId(
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
            verify(repository).getSongsByAlbumId(
                entityType = ENTITY_TYPE,
                albumId = ALBUM_ID
            )
            assert(result is BaseResponse.Failure)
            assert((result as BaseResponse.Failure<AlbumWithSongs>).exception == EXCEPTION)
        }
    }

    companion object {
        private val listOfSong = listOf(
            Song(
                artistId = 1,
                artistName = "artist name",
                collectionId = 2,
                collectionName = "album name",
                trackId = 1,
                trackName = "song name",
                imageCover = "url",
                previewUrl = "url"
            )
        )
        private val album = Album(2, "artist name", "url", 2, "album name", "collection")

        val albumWithSongs = AlbumWithSongs(album, listOfSong)
        val SEARCH_RESPONSE = BaseResponse.Success(albumWithSongs)
        val EXCEPTION = Exception("Error")
        const val ALBUM_ID = 1
        const val ENTITY_TYPE = "song"
    }
}
