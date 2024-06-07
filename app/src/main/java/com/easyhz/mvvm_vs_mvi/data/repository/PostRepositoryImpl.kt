package com.easyhz.mvvm_vs_mvi.data.repository

import com.easyhz.mvvm_vs_mvi.data.api.ExampleService
import com.easyhz.mvvm_vs_mvi.domain.model.entity.ExamplePost
import com.easyhz.mvvm_vs_mvi.domain.model.response.PostResponse
import com.easyhz.mvvm_vs_mvi.domain.model.response.PostResponse.Companion.toEntity
import com.easyhz.mvvm_vs_mvi.domain.repository.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val exampleService: ExampleService
): PostRepository {
    override suspend fun getPost(): Result<List<ExamplePost>> = exampleService.getPost().map { list -> list.map { item -> item.toEntity() } }
}