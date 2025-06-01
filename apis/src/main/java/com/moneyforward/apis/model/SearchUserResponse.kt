package com.moneyforward.apis.model

data class SearchUserResponse(
    val incomplete_results: Boolean? = null,
    val items: List<Item>? = null,
    val total_count: Int? = null
)