package com.easyhz.mvvm_vs_mvi.data.api

import com.easyhz.mvvm_vs_mvi.domain.model.response.PostResponse
import retrofit2.http.GET

interface ExampleService {

    @GET("/posts")
    suspend fun getPost() : Result<List<PostResponse>>
}