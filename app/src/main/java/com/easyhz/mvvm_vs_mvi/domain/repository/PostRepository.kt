package com.easyhz.mvvm_vs_mvi.domain.repository

import com.easyhz.mvvm_vs_mvi.domain.model.entity.ExamplePost
import com.easyhz.mvvm_vs_mvi.domain.model.response.PostResponse

interface PostRepository {

    suspend fun getPost(): Result<List<ExamplePost>>
}