package com.globallogic.itunessearch.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.globallogic.domain.entities.Song
import com.globallogic.domain.usecases.GetSearchResultUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val getSearchResultUseCase: GetSearchResultUseCase) : ViewModel() {

    fun getSearchResult(searchWord: String): Flow<PagingData<Song>> {
        return getSearchResultUseCase.invoke(searchWord)
    }

}
