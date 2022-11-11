package com.globallogic.itunessearch.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globallogic.domain.entities.Song
import com.globallogic.domain.repository.BaseResponse
import com.globallogic.domain.usecases.GetSearchResultUseCase
import kotlinx.coroutines.launch

class MainViewModel(private val getSearchResultUseCase: GetSearchResultUseCase) : ViewModel() {

    private val _liveDataSong = MutableLiveData<List<Song>>()
    val liveDataSong: LiveData<List<Song>>
        get() = _liveDataSong

    private var _liveDataLoading = MutableLiveData<Boolean>()
    val liveDataLoading: LiveData<Boolean>
        get() = _liveDataLoading

    fun getSearchResult(searchWord: String) {
        viewModelScope.launch {
            getSearchResultUseCase.invoke(searchWord).let {
                if (it is BaseResponse.Success) {
                    _liveDataSong.postValue(it.data)
                } else {
                }
            }
        }
    }
}
