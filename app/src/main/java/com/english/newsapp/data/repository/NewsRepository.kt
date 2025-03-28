package com.english.newsapp.data.repository

import android.util.Log
import com.english.newsapp.data.local.dao.NewsDao
import com.english.newsapp.data.local.entities.ArticleEntity
import com.english.newsapp.data.model.Source
import com.english.newsapp.data.remote.api.NewsApiService
import com.english.newsapp.data.remote.model.Article
import com.english.newsapp.data.remote.model.request.EverythingQueryBuilder
import com.english.newsapp.data.remote.model.request.HeadlinesQueryBuilder
import com.english.newsapp.data.remote.model.response.ArticleResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val api: NewsApiService,
    private val dao: NewsDao
) {

    suspend fun getEverything(
        queryBuilder: EverythingQueryBuilder,
        category: String
    ): List<Article> = withContext(Dispatchers.IO) {
        fetchArticles(category) { api.getEverything(queryBuilder.build()) }
    }

    suspend fun getHeadlines(
        queryBuilder: HeadlinesQueryBuilder,
        category: String
    ): List<Article> = withContext(Dispatchers.IO) {
        fetchArticles(category) { api.getTopHeadlines(queryBuilder.build()) }
    }

    private suspend inline fun fetchArticles(
        category: String,
        apiCall: () -> Response<ArticleResponse>
    ): List<Article> {
        // Get cached entities from database
        val cachedEntities = dao.getArticlesByCategory(category)
        if (cachedEntities.isNotEmpty()) {
            // Convert cached ArticleEntity list to Article list
            return cachedEntities.map { entity ->
                Article(
                    source = Gson().fromJson(entity.source, Source::class.java),
                    author = entity.author,
                    title = entity.title,
                    description = entity.description,
                    url = entity.url,
                    urlToImage = entity.urlToImage,
                    publishedAt = entity.publishedAt,
                    content = entity.content
                )
            }
        }

        // Fetch from API if no cache
        return apiCall().let { response ->
            Log.d("error***", "response: $response")
            if (response.isSuccessful && response.body() != null) {
                val articles = response.body()!!.articles
                // Convert API Articles to ArticleEntity for storage
                val entities = articles.mapToEntities(category)
                // Store in database
                dao.clearArticlesByCategory(category)
                dao.insertArticles(entities)
                // Return original Articles from API
                articles
            } else {
                throw Exception("Failed to fetch news: ${response.errorBody()?.string()}")
            }
        }
    }

    // Convert List<Article> to List<ArticleEntity> for database storage
    private fun List<Article>.mapToEntities(category: String): List<ArticleEntity> =
        map {
            ArticleEntity(
                url = it.url,
                source = Gson().toJson(it.source),
                title = it.title,
                description = it.description ?: "",
                urlToImage = it.urlToImage,
                publishedAt = it.publishedAt,
                content = it.content ?: "",
                author = it.author,
                category = category
            )
        }

}