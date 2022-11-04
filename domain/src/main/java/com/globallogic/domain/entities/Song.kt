package com.globallogic.domain.entities

data class Song(
    val artistId: Int,
    val artistName: String,
    val artworkUrl100: String,
    val collectionId: Int,
    val collectionName: String,
    val collectionViewUrl: String,
    val kind: String,
    val previewUrl: String,
    val trackId: Int,
    val trackName: String,
    val trackNumber: Int,
    val trackViewUrl: String,
)