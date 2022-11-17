package com.globallogic.data.di

import com.globallogic.data.remote.api.ItunesSearchApi
import com.globallogic.data.repository.ItunesSearchRepositoryImpl
import com.globallogic.data.repository.SearchResultPagingSource
import com.globallogic.domain.repository.ItunesSearchRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

private const val TIME_OUT = 30L
private const val BASE_URL = "https://itunes.apple.com/"

val networkModule = module {
    single { createService(get()) }

    single { createRetrofit(get(), BASE_URL) }

    single { createOkHttpClient(get()) }

    single { createLoggingInterceptor() }

}

fun createLoggingInterceptor(): HttpLoggingInterceptor {
    val loggingInterceptor = HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    return loggingInterceptor
}

fun createOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(TIME_OUT, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor).build()
}

fun createRetrofit(okHttpClient: OkHttpClient, url: String): Retrofit {
    val moshi by lazy {
        val moshiBuilder = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
        moshiBuilder.build()
    }

    return Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
}

fun createService(retrofit: Retrofit): ItunesSearchApi {
    return retrofit.create(ItunesSearchApi::class.java)
}

fun createItunesRepository(itunesSearchService: ItunesSearchApi): ItunesSearchRepository {
    return ItunesSearchRepositoryImpl(itunesSearchService)
}
