package com.easyhz.mvvm_vs_mvi.domain.model.response

import com.easyhz.mvvm_vs_mvi.domain.model.entity.ExamplePost
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostResponse(
    val id: Int,
    @Json(name = "userId")
    val userId: Int,
    @Json(name = "title")
    val title: String,
    @Json(name = "body")
    val body: String,
) {
    companion object {
        fun PostResponse.toEntity(): ExamplePost = ExamplePost(id, userId, title, body)
    }
}