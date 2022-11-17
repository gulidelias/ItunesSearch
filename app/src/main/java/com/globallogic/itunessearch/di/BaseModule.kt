package com.globallogic.itunessearch.di

import com.globallogic.data.di.createItunesRepository
import com.globallogic.domain.usecases.GetSearchResultUseCase
import com.globallogic.domain.usecases.GetSongsByAlbumUseCase
import com.globallogic.itunessearch.ui.viewmodel.DetailViewModel
import com.globallogic.itunessearch.ui.viewmodel.MainViewModel
import org.koin.dsl.module

val baseModule = module {

    factory { MainViewModel(get()) }

    factory { DetailViewModel(get()) }

    single { createItunesRepository(get()) }

    single { GetSearchResultUseCase(get()) }

    single { GetSongsByAlbumUseCase(get()) }

}
