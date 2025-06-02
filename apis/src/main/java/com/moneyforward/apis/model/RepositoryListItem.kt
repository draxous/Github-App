package com.moneyforward.apis.model

/**
 * RepositoryListItem
 *
 * @property allow_forking 
 * @property archive_url 
 * @property archived 
 * @property assignees_url 
 * @property blobs_url 
 * @property branches_url 
 * @property clone_url 
 * @property collaborators_url 
 * @property comments_url 
 * @property commits_url 
 * @property compare_url 
 * @property contents_url 
 * @property contributors_url 
 * @property created_at 
 * @property default_branch 
 * @property deployments_url 
 * @property description 
 * @property disabled 
 * @property downloads_url 
 * @property events_url 
 * @property fork 
 * @property forks 
 * @property forks_count 
 * @property forks_url 
 * @property full_name 
 * @property git_commits_url 
 * @property git_refs_url 
 * @property git_tags_url 
 * @property git_url 
 * @property has_discussions 
 * @property has_downloads 
 * @property has_issues 
 * @property has_pages 
 * @property has_projects 
 * @property has_wiki 
 * @property homepage 
 * @property hooks_url 
 * @property html_url 
 * @property id 
 * @property is_template 
 * @property issue_comment_url 
 * @property issue_events_url 
 * @property issues_url 
 * @property keys_url 
 * @property labels_url 
 * @property language 
 * @property languages_url 
 * @property license 
 * @property merges_url 
 * @property milestones_url 
 * @property mirror_url 
 * @property name 
 * @property node_id 
 * @property notifications_url 
 * @property open_issues 
 * @property open_issues_count 
 * @property owner 
 * @property `private` 
 * @property pulls_url 
 * @property pushed_at 
 * @property releases_url 
 * @property size 
 * @property ssh_url 
 * @property stargazers_count 
 * @property stargazers_url 
 * @property statuses_url 
 * @property subscribers_url 
 * @property subscription_url 
 * @property svn_url 
 * @property tags_url 
 * @property teams_url 
 * @property topics 
 * @property trees_url 
 * @property updated_at 
 * @property url 
 * @property visibility 
 * @property watchers 
 * @property watchers_count 
 * @property web_commit_signoff_required 
 */
data class RepositoryListItem(
    val allow_forking: Boolean? = null,
    val archive_url: String? = null,
    val archived: Boolean? = null,
    val assignees_url: String? = null,
    val blobs_url: String? = null, // String?
    val branches_url: String? = null, // String?
    val clone_url: String? = null, // String?
    val collaborators_url: String? = null, // String?
    val comments_url: String? = null, // String?
    val commits_url: String? = null, // String?
    val compare_url: String? = null, // String?
    val contents_url: String? = null, // String?
    val contributors_url: String? = null, // String?
    val created_at: String? = null,
    val default_branch: String? = null,
    val deployments_url: String? = null,
    val description: String? = null,
    val disabled: Boolean? = null,
    val downloads_url: String? = null,
    val events_url: String? = null,
    val fork: Boolean? = null,
    val forks: Int? = null,
    val forks_count: Int? = null,
    val forks_url: String? = null,
    val full_name: String? = null,
    val git_commits_url: String? = null,
    val git_refs_url: String? = null,
    val git_tags_url: String? = null,
    val git_url: String? = null,
    val has_discussions: Boolean? = null,
    val has_downloads: Boolean? = null,
    val has_issues: Boolean? = null,
    val has_pages: Boolean? = null,
    val has_projects: Boolean? = null,
    val has_wiki: Boolean? = null,
    val homepage: String? = null,
    val hooks_url: String? = null,
    val html_url: String? = null,
    val id: Int? = null,
    val is_template: Boolean? = null,
    val issue_comment_url: String? = null,
    val issue_events_url: String? = null,
    val issues_url: String? = null,
    val keys_url: String? = null,
    val labels_url: String? = null,
    val language: String? = null,
    val languages_url: String? = null,
    val license: License? = null,
    val merges_url: String? = null,
    val milestones_url: String? = null,
    val mirror_url: Any? = null,
    val name: String? = null,
    val node_id: String? = null,
    val notifications_url: String? = null,
    val open_issues: Int? = null,
    val open_issues_count: Int? = null,
    val owner: Owner? = null,
    val `private`: Boolean? = null,
    val pulls_url: String? = null,
    val pushed_at: String? = null,
    val releases_url: String? = null,
    val size: Int? = null,
    val ssh_url: String? = null,
    val stargazers_count: Int? = null,
    val stargazers_url: String? = null,
    val statuses_url: String? = null,
    val subscribers_url: String? = null,
    val subscription_url: String? = null,
    val svn_url: String? = null,
    val tags_url: String? = null,
    val teams_url: String? = null,
    val topics: List<Any>? = null,
    val trees_url: String? = null,
    val updated_at: String? = null,
    val url: String? = null,
    val visibility: String? = null,
    val watchers: Int? = null,
    val watchers_count: Int? = null,
    val web_commit_signoff_required: Boolean? = null
)