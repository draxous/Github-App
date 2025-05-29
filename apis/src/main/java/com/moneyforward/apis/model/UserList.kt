package com.moneyforward.api.model

data class UserList(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)