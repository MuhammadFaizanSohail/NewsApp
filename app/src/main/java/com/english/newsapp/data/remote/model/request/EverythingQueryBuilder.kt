package com.english.newsapp.data.remote.model.request

class EverythingQueryBuilder {
    private val queryMap = mutableMapOf<String, String>()
    fun query(q: String) = apply { queryMap["q"] = q }
    fun apiKey(apiKey: String) = apply { queryMap["apiKey"] = apiKey }
    fun build(): Map<String, String> = queryMap
}