package com.english.newsapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.english.newsapp.data.local.entities.ArticleEntity

@Dao
interface NewsDao {
    @Query("SELECT * FROM articles WHERE category = :category")
    suspend fun getArticlesByCategory(category: String): List<ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<ArticleEntity>)

    @Query("DELETE FROM articles WHERE category = :category")
    suspend fun clearArticlesByCategory(category: String)
}