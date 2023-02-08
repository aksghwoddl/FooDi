package com.lee.foodi.di

import android.content.Context
import androidx.room.PrimaryKey
import androidx.room.Room
import com.lee.data.api.rest.RestService
import com.lee.data.api.room.DiaryDAO
import com.lee.data.common.BASE_URL
import com.lee.data.common.CONNECTION_TIME_OUT
import com.lee.data.common.FOOD_TARGET_URL
import com.lee.data.datasource.RemoteDateSource
import com.lee.data.model.local.db.DiaryDatabase
import com.lee.data.repository.FoodiRepositoryImpl
import com.lee.domain.repository.FoodiRepository
import com.lee.data.common.DB_NAME
import com.lee.data.datasource.LocalDataSource
import com.lee.data.datasource.LocalDataSourceImpl
import com.lee.data.datasource.RemoteDataSourceImpl
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

/**
 * Hilt에게 의존성 주입 방법을 알려주는 module object
 * **/
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
            .baseUrl(BASE_URL)
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
            DiaryDatabase::class.java,
            DB_NAME,
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
    fun provideRepository(remoteDateSource: RemoteDateSource, localDateSource: LocalDataSource) : FoodiRepository {
        return FoodiRepositoryImpl(remoteDateSource, localDateSource)
    }

    /**
     * RemoteDataSource를 provide하는 함수
     * **/
    @Provides
    @Singleton
    fun provideRemoteDateSource(resetService : RestService) : RemoteDateSource {
        return RemoteDataSourceImpl(resetService)
    }

    /**
     * LocalDataSource를 provide하는 함수
     * **/
    @Provides
    @Singleton
    fun provideLocalDataSource(diaryDAO: DiaryDAO) : LocalDataSource {
        return LocalDataSourceImpl(diaryDAO)
    }
}