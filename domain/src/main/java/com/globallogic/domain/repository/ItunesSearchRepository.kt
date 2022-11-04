package com.globallogic.domain.repository

import com.globallogic.domain.entities.Song

interface ItunesSearchRepository {

    suspend fun getSearchResult(
        searchWord: String,
        mediaType: String,
        entityType: String,
        limit: Int
    ): BaseResponse<Song>

    suspend fun getSongByAlbumId(
        albumId: Int,
        entityType: String
    ): BaseResponse<Song>
}
