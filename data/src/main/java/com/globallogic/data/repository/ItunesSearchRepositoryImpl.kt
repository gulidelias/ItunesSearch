package com.globallogic.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.globallogic.data.remote.api.ItunesSearchApi
import com.globallogic.data.utils.toAlbum
import com.globallogic.data.utils.toSong
import com.globallogic.domain.entities.Album
import com.globallogic.domain.entities.AlbumWithSongs
import com.globallogic.domain.entities.Song
import com.globallogic.domain.repository.BaseResponse
import com.globallogic.domain.repository.ItunesSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import kotlin.math.E

class ItunesSearchRepositoryImpl(private val itunesSearchService: ItunesSearchApi) :
    ItunesSearchRepository {

    override fun getSearchResult(
        searchWord: String,
        pageSize: Int,
    ): Flow<PagingData<Song>> {
            return Pager(
                config = PagingConfig(
                    pageSize = pageSize,
                    enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    SearchResultPagingSource(
                        searchWord = searchWord,
                        itunesSearchService = itunesSearchService,
                        pageSize = pageSize
                    )
                }
            ).flow
        }


    override suspend fun getSongsByAlbumId(
        albumId: Int,
        entityType: String
    ): BaseResponse<AlbumWithSongs> =
        withContext(Dispatchers.IO) {
            try {
                val response = itunesSearchService.getSongByAlbumId(albumId, entityType)
                if (response.isSuccessful) {
                    response.body()?.results?.let { listAlbumWithSongsDTO ->
                        var album: Album? = null
                        val songs = mutableListOf<Song>()
                        listAlbumWithSongsDTO.forEach { item ->
                            item.toAlbum()?.let { album = it }
                            item.toSong()?.let { songs.add(it) }
                        }

                        val albumWithSongs = AlbumWithSongs(album, songs)
                        return@withContext BaseResponse.Success(albumWithSongs)
                    } ?: kotlin.run {
                        return@withContext BaseResponse.Failure(Exception(response.message()))
                    }
                } else {
                    return@withContext BaseResponse.Failure(Exception(response.message()))
                }
            } catch (exception: Exception) {
                return@withContext BaseResponse.Failure(Exception(exception))
            }
        }
}
