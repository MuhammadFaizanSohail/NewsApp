package com.english.newsapp.di.databasemodule

import android.content.Context
import com.english.newsapp.data.local.dao.NewsDao
import com.english.newsapp.data.local.database.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): NewsDatabase {
        return NewsDatabase.getInstance(appContext)
    }

    @Provides
    fun provideDao(database: NewsDatabase): NewsDao {
        return database.newsDao()
    }
}
