package com.english.newsapp.data.local.database

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.english.newsapp.data.local.dao.NewsDao
import com.english.newsapp.data.local.entities.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    companion object {
        const val DATABASE_NAME = "news_database"

        @Volatile
        private var INSTANCE: NewsDatabase? = null

        fun getInstance(context: Context): NewsDatabase {
            return INSTANCE ?: synchronized(this) {
                val contextA = ContextCompat.createDeviceProtectedStorageContext(context)
                    ?: context.applicationContext
                val instance = Room.databaseBuilder(
                    contextA,
                    NewsDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }

}