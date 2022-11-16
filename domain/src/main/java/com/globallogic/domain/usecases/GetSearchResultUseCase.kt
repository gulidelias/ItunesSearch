package com.globallogic.domain.usecases

import com.globallogic.domain.repository.ItunesSearchRepository


class GetSearchResultUseCase(private val itunesSearchRepository: ItunesSearchRepository) {

    operator fun invoke(
        searchWord: String,
    ) = itunesSearchRepository.getSearchResult(
        searchWord
    )
}
