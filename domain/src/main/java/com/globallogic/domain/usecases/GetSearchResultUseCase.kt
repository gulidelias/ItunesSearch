package com.globallogic.domain.usecases

import com.globallogic.domain.repository1567705492.ItunesSearchRepository


class GetSearchResultUseCase(private val itunesSearchRepository:ItunesSearchRepository) {

    suspend operator fun invoke(
        searchWord: String,
        mediaType: String = MEDIA_TYPE,
        entityType: String = ENTITY_TYPE,
        limit: Int = RESULTS_LIMIT
    ) = itunesSearchRepository.getSearchResult(
        searchWord,
        mediaType,
        entityType,
        limit
    )

    companion object {
        const val RESULTS_LIMIT = 20
        const val MEDIA_TYPE = "music"
        const val ENTITY_TYPE = "song"
    }
}
