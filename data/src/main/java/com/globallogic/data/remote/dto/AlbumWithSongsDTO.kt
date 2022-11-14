package com.globallogic.data.remote.dto

data class AlbumWithSongsDTO(
    val amgArtistId: Int?,
    val artistId: Int?,
    val artistName: String?,
    val artistViewUrl: String?,
    val artworkUrl100: String?,
    val artworkUrl60: String?,
    val artworkUrl30: String?,
    val collectionCensoredName: String?,
    val collectionExplicitness: String?,
    val collectionId: Int?,
    val collectionName: String?,
    val collectionPrice: Double?,
    val collectionType: String?,
    val collectionViewUrl: String?,
    val copyright: String?,
    val country: String?,
    val currency: String?,
    val discCount: Int?,
    val discNumber: Int?,
    val primaryGenreName: String?,
    val releaseDate: String?,
    val trackCount: Int?,
    val isStreamable: Boolean?,
    val kind: String?,
    val previewUrl: String?,
    val trackCensoredName: String?,
    val trackExplicitness: String?,
    val trackId: Int?,
    val trackName: String?,
    val trackNumber: Int?,
    val trackPrice: Double?,
    val trackTimeMillis: Int?,
    val trackViewUrl: String?,
    val wrapperType: String
)

const val WRAPPER_TYPE_ALBUM = "collection"
const val WRAPPER_TYPE_SONG = "track"
