package com.globallogic.data.repository

import com.globallogic.data.remote.api.ItunesSearchApi
import com.globallogic.data.utils.toAlbum
import com.globallogic.data.utils.toDomainSong
import com.globallogic.data.utils.toSong
import com.globallogic.domain.entities.Album
import com.globallogic.domain.entities.AlbumWithSongs
import com.globallogic.domain.entities.Song
import com.globallogic.domain.repository.BaseResponse
import com.globallogic.domain.repository1567705492.ItunesSearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItunesSearchRepositoryImpl(private val itunesSearchService: ItunesSearchApi) :
    ItunesSearchRepository {

    override suspend fun getSearchResult(
        searchWord: String,
        mediaType: String,
        entityType: String,
        limit: Int
    ): BaseResponse<List<Song>> =
        withContext(Dispatchers.IO) {
            val response =
                itunesSearchService.getSearchResults(searchWord, mediaType, entityType, limit)
            if (response.isSuccessful) {
                response.body()?.results?.let { listSongDTO ->
                    val searchResultList = listSongDTO.map {
                        it.toDomainSong()
                    }
                    // map to entity and add to data base then map and return Song
                    return@withContext BaseResponse.Success(searchResultList)
                } ?: kotlin.run {
                    return@withContext BaseResponse.Failure(Exception(response.message()))
                }
            } else {
                return@withContext BaseResponse.Failure(Exception(response.message()))
            }
        }

    override suspend fun getSongsByAlbumId(
        albumId: Int,
        entityType: String
    ): BaseResponse<AlbumWithSongs> =
        withContext(Dispatchers.IO) {
            val response =
                itunesSearchService.getSongByAlbumId(albumId, entityType)
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
        }
}
