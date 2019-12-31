package revoult.test.maulik.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit
import retrofit2.converter.gson.GsonConverterFactory
import revoult.test.maulik.repository.RateRepository
import javax.inject.Singleton

/**
 * We are providing different methods for injecting retrofit object
 */
@Module
class APIModule constructor(baseURL: String) {
    var baseURL: String? = ""

    init {
        this.baseURL = baseURL
    }

    /**
     * Okhttp3 provider method
     */
    @Singleton
    @Provides
    fun provideOKHttpClient(): OkHttpClient {
        var loggingInterceptor=HttpLoggingInterceptor()
        loggingInterceptor.level=HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
                .readTimeout(1200, TimeUnit.SECONDS)
                .connectTimeout(1200, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .build()
    }

    /**
     * Provide gson convertor factory
     */
    @Singleton
    @Provides
    fun provideGSON(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    /**
     * Provide retrofit object
     */
    @Singleton
    @Provides
    fun provideRetrofit(gsonConverterFactory: GsonConverterFactory, okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
    }

    @Provides
    fun provideRetroRepository(): RateRepository {
        return RateRepository()
    }

}