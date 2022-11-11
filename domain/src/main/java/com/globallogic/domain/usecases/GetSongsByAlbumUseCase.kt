package com.globallogic.domain.usecases

import com.globallogic.domain.repository1567705492.ItunesSearchRepository


class GetSongsByAlbumUseCase(private val itunesSearchRepository: ItunesSearchRepository) {

    suspend operator fun invoke(
        albumId: Int,
        entityType: String = ENTITY_TYPE
    ) = itunesSearchRepository.getSongsByAlbumId(
        albumId,
        entityType
    )

    companion object {
        const val ENTITY_TYPE = "song"
    }
}
