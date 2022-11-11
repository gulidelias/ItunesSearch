package com.globallogic.data.utils

import com.globallogic.data.remote.dto.AlbumWithSongsDTO
import com.globallogic.data.remote.dto.SongDTO
import com.globallogic.data.remote.dto.WRAPPER_TYPE_ALBUM
import com.globallogic.data.remote.dto.WRAPPER_TYPE_SONG
import com.globallogic.domain.entities.Album
import com.globallogic.domain.entities.Song

fun SongDTO.toDomainSong(): Song {
    return Song(
        artistId = artistId,
        artistName = artistName,
        imageCover = artworkUrl100,
        trackId = trackId,
        trackName = trackName,
        trackViewUrl = trackViewUrl,
        collectionName = collectionName,
        collectionId = collectionId
    )
}

fun AlbumWithSongsDTO.toSong(): Song? {
    if (wrapperType != WRAPPER_TYPE_SONG) return null
    return Song(
        artistId = artistId,
        artistName = artistName,
        imageCover = artworkUrl100,
        collectionId = collectionId,
        collectionName = collectionName,
        trackId = trackId,
        trackName = trackName,
        trackViewUrl = trackViewUrl

    )
}

fun AlbumWithSongsDTO.toAlbum(): Album? {
    if (wrapperType != WRAPPER_TYPE_ALBUM) return null
    return Album(
        artistId = artistId,
        artistName = artistName,
        artworkUrl100 = artworkUrl100,
        collectionId = collectionId,
        collectionName = collectionName,
        wrapperType = wrapperType
    )
}
