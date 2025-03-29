package com.english.newsapp.data.repository

import com.english.newsapp.data.local.dao.NewsDao
import com.english.newsapp.data.local.entities.ArticleEntity
import com.english.newsapp.data.model.Source
import com.english.newsapp.data.remote.api.NewsApiService
import com.english.newsapp.data.remote.model.Article
import com.english.newsapp.data.remote.model.request.EverythingQueryBuilder
import com.english.newsapp.data.remote.model.request.HeadlinesQueryBuilder
import com.english.newsapp.data.remote.model.response.ArticleResponse
import com.english.newsapp.utils.constants.API_KEY
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val api: NewsApiService,
    private val dao: NewsDao
) {

    suspend fun getEverything(query: String, isRefreshing: Boolean): List<Article> {
        return fetchArticles(
            query,
            isRefreshing
        ) { api.getEverything(EverythingQueryBuilder().query(query).apiKey(API_KEY).build()) }
    }

    suspend fun getHeadlines(country: String, isRefreshing: Boolean): List<Article> {
        return fetchArticles(
            country,
            isRefreshing
        ) { api.getTopHeadlines(HeadlinesQueryBuilder().country(country).apiKey(API_KEY).build()) }
    }

    private suspend inline fun fetchArticles(
        category: String,
        isRefreshing: Boolean,
        crossinline apiCall: suspend () -> Response<ArticleResponse>
    ): List<Article> = withContext(Dispatchers.IO) {
        val cachedArticles = dao.getArticlesByCategory(category).map { it.toArticle() }

        if (cachedArticles.isNotEmpty() && !isRefreshing) return@withContext cachedArticles

        val response = apiCall()
        if (response.isSuccessful) {
            val articles = response.body()?.articles ?: emptyList()
            dao.run {
                clearArticlesByCategory(category)
                insertArticles(articles.mapToEntities(category))
            }
            return@withContext articles
        } else {
            throw Exception(
                "Failed to fetch news: ${
                    response.errorBody()?.string() ?: "Unknown error"
                }"
            )
        }
    }

    private fun List<Article>.mapToEntities(category: String): List<ArticleEntity> = map {
        ArticleEntity(
            url = it.url,
            source = Gson().toJson(it.source),
            title = it.title,
            description = it.description.orEmpty(),
            urlToImage = it.urlToImage,
            publishedAt = it.publishedAt,
            content = it.content.orEmpty(),
            author = it.author,
            category = category
        )
    }

    private fun ArticleEntity.toArticle(): Article = Article(
        source = Gson().fromJson(source, Source::class.java),
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content
    )
}