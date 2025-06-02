package com.moneyforward.apis.model

/**
 * Represents an item, likely a user or organization.
 *
 * @property avatar_url The URL for the item's avatar.
 * @property events_url The URL for the item's events.
 * @property followers_url The URL for the item's followers.
 * @property following_url The URL for the users that the item is following.
 * @property gists_url The URL for the item's gists.
 * @property gravatar_id The Gravatar ID for the item.
 * @property html_url The HTML URL for the item.
 * @property id The unique identifier for the item.
 * @property login The login name of the item.
 * @property node_id The node ID for the item.
 * @property organizations_url The URL for the item's organizations.
 * @property received_events_url The URL for the events received by the item.
 * @property repos_url The URL for the item's repositories.
 * @property score The score associated with the item, often used in search results.
 * @property site_admin Indicates if the item is a site administrator.
 * @property starred_url The URL for the items starred by this item.
 * @property subscriptions_url The URL for the item's subscriptions.
 * @property type The type of the item (e.g., "User", "Organization").
 * @property url The API URL for the item.
 * @property user_view_type The view type for the user.
 */
data class Item(
    /** The URL for the item's avatar. */
    val avatar_url: String? = null,
    /** The URL for the item's events. */
    val events_url: String? = null,
    /** The URL for the item's followers. */
    val followers_url: String? = null,
    /** The URL for the users that the item is following. */
    val following_url: String? = null,
    /** The URL for the item's gists. */
    val gists_url: String? = null,
    /** The Gravatar ID for the item. */
    val gravatar_id: String? = null,
    /** The HTML URL for the item. */
    val html_url: String? = null,
    /** The unique identifier for the item. */
    val id: Int? = null,
    /** The login name of the item. */
    val login: String? = null,
    /** The node ID for the item. */
    val node_id: String? = null,
    /** The URL for the item's organizations. */
    val organizations_url: String? = null,
    /** The URL for the events received by the item. */
    val received_events_url: String? = null,
    /** The URL for the item's repositories. */
    val repos_url: String? = null,
    /** The score associated with the item, often used in search results. */
    val score: Double? = null,
    /** Indicates if the item is a site administrator. */
    val site_admin: Boolean? = null,
    /** The URL for the items starred by this item. */
    val starred_url: String? = null,
    /** The URL for the item's subscriptions. */
    val subscriptions_url: String? = null,
    /** The type of the item (e.g., "User", "Organization"). */
    val type: String? = null,
    /** The API URL for the item. */
    val url: String? = null,
    /** The view type for the user. */
    val user_view_type: String? = null
)