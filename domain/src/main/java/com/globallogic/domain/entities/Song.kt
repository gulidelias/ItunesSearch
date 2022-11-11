package com.globallogic.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    val artistId: Int?,
    val artistName: String?,
    val imageCover: String?,
    val collectionId: Int?,
    val collectionName: String?,
    val trackId: Int?,
    val trackName: String?,
    val trackViewUrl: String?
):Parcelable