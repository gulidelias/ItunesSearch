package com.globallogic.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.globallogic.data.remote.api.ItunesSearchApi
import com.globallogic.data.utils.toDomainSong
import com.globallogic.domain.entities.Song
import retrofit2.HttpException
import java.io.IOException

class SearchResultPagingSource(
    private val itunesSearchService: ItunesSearchApi,
    private val searchWord: String,
    private val pageSize: Int
) : PagingSource<Int, Song>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Song> {
        val offset = params.key ?: STARTING_PAGE_OFFSET
        val limit = pageSize - 1

        return try {
            val response = itunesSearchService.getSearchResults(
                searchWord = searchWord,
                mediaType = MEDIA_TYPE_MUSIC,
                entity = ENTITY_SONG,
                limit = limit,
                offset = offset
            )
            var listOfSongs = mutableListOf<Song>()
            if (response.isSuccessful) {
                response.body()?.results?.let { listOfSongDTO ->
                    listOfSongs = listOfSongDTO.map { it.toDomainSong() } as MutableList<Song>
                }
            }

            val nextKey = if (listOfSongs.isEmpty()) null else offset + pageSize
            val prevKey = if (offset == STARTING_PAGE_OFFSET) null else offset

            LoadResult.Page(data = listOfSongs, nextKey = nextKey, prevKey = prevKey)
        } catch (exception: IOException) {
             LoadResult.Error(exception)
        } catch (exception: HttpException) {
             LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Song>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(pageSize)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(pageSize)
        }
    }

    companion object {
        private const val STARTING_PAGE_OFFSET = 0
        private const val MEDIA_TYPE_MUSIC = "music"
        private const val ENTITY_SONG = "song"

    }
}
