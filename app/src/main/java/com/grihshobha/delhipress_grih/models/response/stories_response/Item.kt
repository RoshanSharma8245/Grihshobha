package com.grihshobha.delhipress_grih.models.response.stories_response

data class Item(
    val audio: Boolean?,
    val author: String?,
    val author_nice: String?,
    val categories: List<Category?>?,
    val episodic_post: List<EpisodicPost?>?,
    val image: String?,
    val location: String?,
    val next_post_id: Int?,
    val parent_post_id: String?,
    val post_date: String?,
    val post_id: Int?,
    val post_tags: List<PostTag?>?,
    val prev_post_id: Int?,
    val story_content: List<StoryContent?>?,
    val subhead: String?,
    val title: String?,
    val web_url: String?
)