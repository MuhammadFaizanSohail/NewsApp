package com.english.newsapp.data.remote.model.request

class HeadlinesQueryBuilder {
    private val queryMap = mutableMapOf<String, String>()
    fun country(country: String) = apply { queryMap["country"] = country }
    fun apiKey(apiKey: String) = apply { queryMap["apiKey"] = apiKey }
    fun build(): Map<String, String> = queryMap
}