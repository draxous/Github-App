package com.moneyforward.apis.model

/**
 * Repository owner information.
 *
 * @property avatar_url Owner's avatar URL.
 * @property events_url Owner's events URL.
 * @property followers_url Owner's followers URL.
 * @property following_url Owner's following URL.
 * @property gists_url Owner's gists URL.
 * @property gravatar_id Owner's gravatar ID.
 * @property html_url Owner's HTML URL.
 * @property id Owner's ID.
 * @property login Owner's login name.
 * @property node_id Owner's node ID.
 * @property organizations_url Owner's organizations URL.
 * @property received_events_url Owner's received events URL.
 * @property repos_url Owner's repositories URL.
 * @property site_admin Whether the owner is a site admin.
 * @property starred_url Owner's starred URL.
 * @property subscriptions_url Owner's subscriptions URL.
 * @property type Owner's type.
 * @property url Owner's URL.
 * @property user_view_type Owner's user view type.
 */
data class Owner(
    /** Owner's avatar URL. */
    val avatar_url: String? = null,
    /** Owner's events URL. */
    val events_url: String? = null,
    /** Owner's followers URL. */
    val followers_url: String? = null,
    /** Owner's following URL. */
    val following_url: String? = null,
    /** Owner's gists URL. */
    val gists_url: String? = null,
    /** Owner's gravatar ID. */
    val gravatar_id: String? = null,
    /** Owner's HTML URL. */
    val html_url: String? = null,
    /** Owner's ID. */
    val id: Int? = null,
    /** Owner's login name. */
    val login: String? = null,
    /** Owner's node ID. */
    val node_id: String? = null,
    /** Owner's organizations URL. */
    val organizations_url: String? = null,
    /** Owner's received events URL. */
    val received_events_url: String? = null,
    /** Owner's repositories URL. */
    val repos_url: String? = null,
    /** Whether the owner is a site admin. */
    val site_admin: Boolean? = null,
    /** Owner's starred URL. */
    val starred_url: String? = null,
    /** Owner's subscriptions URL. */
    val subscriptions_url: String? = null,
    /** Owner's type. */
    val type: String? = null,
    /** Owner's URL. */
    val url: String? = null,
    /** Owner's user view type. */
    val user_view_type: String? = null
)