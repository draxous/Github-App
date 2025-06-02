package com.moneyforward.apis.model

/**
 * Represents a user's profile information.
 *
 * @property avatar_url The URL of the user's avatar.
 * @property bio The user's biography.
 * @property blog The URL of the user's blog.
 * @property company The user's company.
 * @property created_at The date and time when the user's account was created.
 * @property email The user's email address.
 * @property events_url The URL for the user's events.
 * @property followers The number of followers the user has.
 * @property followers_url The URL for the user's followers.
 * @property following The number of users the user is following.
 * @property following_url The URL for the users the user is following.
 * @property gists_url The URL for the user's gists.
 * @property gravatar_id The user's Gravatar ID.
 * @property hireable Indicates whether the user is hireable.
 * @property html_url The URL of the user's HTML profile page.
 * @property id The unique identifier for the user.
 * @property location The user's location.
 * @property login The user's login username.
 * @property name The user's full name.
 * @property node_id The node ID of the user.
 * @property organizations_url The URL for the user's organizations.
 * @property public_gists The number of public gists the user has.
 * @property public_repos The number of public repositories the user has.
 * @property received_events_url The URL for events received by the user.
 * @property repos_url The URL for the user's repositories.
 * @property site_admin Indicates whether the user is a site administrator.
 * @property starred_url The URL for the user's starred repositories.
 * @property subscriptions_url The URL for the user's subscriptions.
 * @property twitter_username The user's Twitter username.
 * @property type The type of user (e.g., "User", "Organization").
 * @property updated_at The date and time when the user's profile was last updated.
 * @property url The URL of the user's API profile.
 * @property user_view_type The view type for the user.
 */
data class Profile(
    /** The URL of the user's avatar. */
    val avatar_url: String? = null,
    /** The user's biography. */
    val bio: String? = null,
    /** The URL of the user's blog. */
    val blog: String? = null,
    /** The user's company. */
    val company: String? = null,
    /** The date and time when the user's account was created. */
    val created_at: String? = null,
    /** The user's email address. */
    val email: String? = null,
    /** The URL for the user's events. */
    val events_url: String? = null,
    /** The number of followers the user has. */
    val followers: Int? = null,
    /** The URL for the user's followers. */
    val followers_url: String? = null,
    /** The number of users the user is following. */
    val following: Int? = null,
    /** The URL for the users the user is following. */
    val following_url: String? = null,
    /** The URL for the user's gists. */
    val gists_url: String? = null,
    /** The user's Gravatar ID. */
    val gravatar_id: String? = null,
    /** Indicates whether the user is hireable. */
    val hireable: String? = null,
    /** The URL of the user's HTML profile page. */
    val html_url: String? = null,
    /** The unique identifier for the user. */
    val id: Int? = null,
    /** The user's location. */
    val location: String? = null,
    /** The user's login username. */
    val login: String? = null,
    /** The user's full name. */
    val name: String? = null,
    /** The node ID of the user. */
    val node_id: String? = null,
    /** The URL for the user's organizations. */
    val organizations_url: String? = null,
    /** The number of public gists the user has. */
    val public_gists: Int? = null,
    /** The number of public repositories the user has. */
    val public_repos: Int? = null,
    /** The URL for events received by the user. */
    val received_events_url: String? = null,
    /** The URL for the user's repositories. */
    val repos_url: String? = null,
    /** Indicates whether the user is a site administrator. */
    val site_admin: Boolean? = null,
    /** The URL for the user's starred repositories. */
    val starred_url: String? = null,
    /** The URL for the user's subscriptions. */
    val subscriptions_url: String? = null,
    /** The user's Twitter username. */
    val twitter_username: String? = null,
    /** The type of user (e.g., "User", "Organization"). */
    val type: String? = null,
    /** The date and time when the user's profile was last updated. */
    val updated_at: String? = null,
    /** The URL of the user's API profile. */
    val url: String? = null,
    /** The view type for the user. */
    val user_view_type: String? = null
)