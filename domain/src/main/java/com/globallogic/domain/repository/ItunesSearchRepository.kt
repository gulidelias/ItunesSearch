package com.globallogic.domain.repository

import androidx.paging.PagingData
import com.globallogic.domain.entities.AlbumWithSongs
import com.globallogic.domain.entities.Song
import kotlinx.coroutines.flow.Flow

interface ItunesSearchRepository {

    fun getSearchResult(
        searchWord: String,
        pageSize: Int = 20,
    ): Flow<PagingData<Song>>

    suspend fun getSongsByAlbumId(
        albumId: Int,
        entityType: String
    ): BaseResponse<AlbumWithSongs>
}
