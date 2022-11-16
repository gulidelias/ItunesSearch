package com.globallogic.itunessearch.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globallogic.domain.entities.Song
import com.globallogic.domain.repository.BaseResponse
import com.globallogic.domain.usecases.GetSongsByAlbumUseCase
import kotlinx.coroutines.launch

class DetailViewModel(private val getSongsByAlbumUseCase: GetSongsByAlbumUseCase) : ViewModel() {


    private val _liveDataListOfSong = MutableLiveData<List<Song>>()
    val liveDataListOfSong: LiveData<List<Song>>
        get() = _liveDataListOfSong

    fun getSongsByAlbum(albumId: Int) {
        viewModelScope.launch {
            getSongsByAlbumUseCase.invoke(albumId).let {
                when (it) {
                    is BaseResponse.Success -> {
                        _liveDataListOfSong.postValue(it.data.songs)
                    }
                    else -> {
                    }
                }
            }
        }
    }
}
