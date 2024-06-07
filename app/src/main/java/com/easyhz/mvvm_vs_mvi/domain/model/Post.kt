package com.easyhz.mvvm_vs_mvi.domain.model

data class Post(
    val id: Int,
    val user: User,
    val title: String,
    val content: String,
    val isLiked: Boolean
)
