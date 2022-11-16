package com.globallogic.itunessearch.ui.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.globallogic.domain.entities.Song
import com.globallogic.domain.usecases.GetSearchResultUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val getSearchResultUseCase: GetSearchResultUseCase) : ViewModel() {

    private var _liveDataSong = MutableLiveData<PagingData<Song>>()
    val liveDataSong: LiveData<PagingData<Song>>
        get() = _liveDataSong

    fun getSearchResult(searchWord: String): Flow<PagingData<Song>> {
        return getSearchResultUseCase.invoke(searchWord).cachedIn(viewModelScope)
    }
}
