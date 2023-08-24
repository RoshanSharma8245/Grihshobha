package com.grihshobha.delhipress_grih.models.response.stories_response

data class StoriesResponse(
    val code: Int?,
    val `data`: List<Data?>?,
    val success: Int?,
    val today: String?
)