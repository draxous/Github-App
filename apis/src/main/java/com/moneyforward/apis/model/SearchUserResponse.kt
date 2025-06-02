package com.moneyforward.apis.model

/**
 * ユーザー検索のレスポンス
 *
 * @param incomplete_results 検索結果が不完全であるかどうかを示すフラグ
 * @param items 検索結果のユーザーリスト
 * @param total_count 検索結果の総数
 */
data class SearchUserResponse(
    val incomplete_results: Boolean? = null, // ktlint-disable parameter-wrapping
    val items: List<Item>? = null, // ktlint-disable parameter-wrapping
    val total_count: Int? = null // ktlint-disable parameter-wrapping
)