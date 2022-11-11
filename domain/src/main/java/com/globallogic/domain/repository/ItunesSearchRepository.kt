package com.globallogic.domain.repository1567705492

import com.globallogic.domain.entities.AlbumWithSongs
import com.globallogic.domain.entities.Song
import com.globallogic.domain.repository.BaseResponse

interface ItunesSearchRepository {

    suspend fun getSearchResult(
        searchWord: String,
        mediaType: String,
        entityType: String,
        limit: Int
    ): BaseResponse<List<Song>>

    suspend fun getSongsByAlbumId(
        albumId: Int,
        entityType: String
    ): BaseResponse<AlbumWithSongs>

}