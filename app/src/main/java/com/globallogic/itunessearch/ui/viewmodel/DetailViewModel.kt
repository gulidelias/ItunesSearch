package com.globallogic.itunessearch.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.globallogic.domain.entities.Song
import com.globallogic.domain.repository.BaseResponse
import com.globallogic.domain.usecases.GetSongsByAlbumUseCase
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kotlinx.coroutines.launch

class DetailViewModel(
    private val getSongsByAlbumUseCase: GetSongsByAlbumUseCase,
    private val exoPlayer: ExoPlayer
) : ViewModel() {

    private val _liveDataError = MutableLiveData<String>()
    val liveDataError: LiveData<String>
        get() = _liveDataError

    private val _liveDataListOfSong = MutableLiveData<List<Song>>()
    val liveDataListOfSong: LiveData<List<Song>>
        get() = _liveDataListOfSong

    fun getSongsByAlbum(albumId: Int) {
        viewModelScope.launch {
            when (val response = getSongsByAlbumUseCase.invoke(albumId)) {
                is BaseResponse.Success -> {
                    _liveDataListOfSong.postValue(response.data.songs)
                }
                is BaseResponse.Failure -> {
                    _liveDataError.postValue("error")
                }
            }
        }
    }

    fun onStop() {
        exoPlayer.stop()
        exoPlayer.seekToDefaultPosition()
    }

    fun onSongClicked(url: String) {
        val mediaItem = MediaItem.fromUri(url)
        exoPlayer.prepare()
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.play()

    }
}
