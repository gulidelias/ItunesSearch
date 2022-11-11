package com.globallogic.data.remote.api

import com.globallogic.data.remote.dto.AlbumWithSongsDTO
import com.globallogic.data.remote.dto.ResponseDTO
import com.globallogic.data.remote.dto.SongDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesSearchApi {

    @GET("search")
    suspend fun getSearchResults(
        @Query("term") searchTerm: String,
        @Query("media") mediaType: String,
        @Query("entity") entity: String,
        @Query("limit") limit: Int
    ): Response<ResponseDTO<SongDTO>>

    @GET("lookup")
    suspend fun getSongByAlbumId(
        @Query("id") albumId: Int,
        @Query("entity") entity: String
    ): Response<ResponseDTO<AlbumWithSongsDTO>>
}
