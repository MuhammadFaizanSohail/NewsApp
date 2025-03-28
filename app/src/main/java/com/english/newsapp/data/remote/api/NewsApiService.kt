package com.english.newsapp.data.remote.api

import com.english.newsapp.data.remote.model.response.ArticleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface NewsApiService {
    @GET("everything")
    suspend fun getEverything(@QueryMap options: Map<String, String>): Response<ArticleResponse>

    @GET("top-headlines")
    suspend fun getTopHeadlines(@QueryMap options: Map<String, String>): Response<ArticleResponse>
}