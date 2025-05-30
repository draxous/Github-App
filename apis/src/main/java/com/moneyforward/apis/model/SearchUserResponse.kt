package com.moneyforward.apis.model

data class SearchUserResponse(
    val incomplete_results: Boolean?,
    val items: List<Item>?,
    val total_count: Int?
)