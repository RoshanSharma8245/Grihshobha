package com.grihshobha.delhipress_grih.models.response.stories_response

data class PostTag(
    val count: Int?,
    val description: String?,
    val filter: String?,
    val name: String?,
    val parent: Int?,
    val slug: String?,
    val taxonomy: String?,
    val term_group: Int?,
    val term_id: Int?,
    val term_taxonomy_id: Int?
)