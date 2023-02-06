package com.lee.foodi.di

import android.content.Context
import androidx.room.Room
import com.lee.foodi.common.CONNECTION_TIME_OUT
import com.lee.foodi.common.DB_NAME
import com.lee.foodi.common.FOOD_TARGET_URL
import com.lee.foodi.data.repository.FoodiRepositoryImpl
import com.lee.foodi.data.rest.RestService
import com.lee.foodi.data.room.dao.DiaryDAO
import com.lee.foodi.data.room.db.DiaryDatabase
import com.lee.foodi.domain.FoodiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    /**
     * OkHttpClient를 provide하는 함수
     * **/
    @Provides
    @Singleton
    fun provideOkHttpClient() : OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level= HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.MILLISECONDS)
            .build()
    }

    /**
     * Retrofit을 provide하는 함수
     * **/
    @Provides
    @Singleton
    fun provideRestService(okHttpClient: OkHttpClient): RestService {
        val retrofit = Retrofit.Builder()
            .baseUrl(FOOD_TARGET_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(RestService::class.java)
    }

    /**
     * RoomDB provide하는 함수
     * **/
    @Provides
    @Singleton
    fun provideDiaryDatabase(@ApplicationContext context : Context) : DiaryDatabase {
        return Room.databaseBuilder(
            context ,
            DiaryDatabase::class.java ,
            DB_NAME ,
        ).build()
    }

    /**
     * DiaryDAO provide하는 함수
     * **/
    @Provides
    @Singleton
    fun provideDiaryDao(database : DiaryDatabase) : DiaryDAO {
        return database.diaryDao()
    }

    /**
     * Repository provide하는 함수
     * **/
    @Provides
    @Singleton
    fun provideRepository(restService: RestService , diaryDAO: DiaryDAO) : FoodiRepository{
        return FoodiRepositoryImpl(restService , diaryDAO)
    }
}